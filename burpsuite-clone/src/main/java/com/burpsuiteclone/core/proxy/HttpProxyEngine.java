package com.burpsuiteclone.core.proxy;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class HttpProxyEngine {
    private static final int DEFAULT_PORT = 8080;
    private ProxyOptions options = new ProxyOptions();
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private volatile boolean running = false;
    private final List<ProxyHistoryEntry> history = new CopyOnWriteArrayList<>();
    private final AtomicInteger historyIdCounter = new AtomicInteger(1);
    private final AtomicInteger interceptIdCounter = new AtomicInteger(1);
    private final Map<Integer, InterceptedRequest> interceptedRequests = new ConcurrentHashMap<>();
    private final List<ProxyListener> listeners = new CopyOnWriteArrayList<>();
    private Consumer<ProxyHistoryEntry> onHistoryUpdate;
    private Consumer<InterceptedRequest> onIntercept;

    public static class InterceptedRequest {
        public final int id;
        public final ProxyHistoryEntry entry;
        public volatile String requestHeaders;
        public volatile String requestBody;
        public volatile boolean forward = false;
        public volatile boolean drop = false;
        public volatile boolean edited = false;

        public InterceptedRequest(int id, ProxyHistoryEntry entry, String headers, String body) {
            this.id = id;
            this.entry = entry;
            this.requestHeaders = headers;
            this.requestBody = body;
        }
    }

    public interface ProxyListener {
        void onRequest(ProxyHistoryEntry entry, String method, String url, String headers, String body);
        void onResponse(ProxyHistoryEntry entry, int statusCode, String headers, String body);
        void onIntercept(InterceptedRequest request);
        void onError(String error);
        void onStateChange(boolean running);
    }

    public HttpProxyEngine() {}

    public HttpProxyEngine(ProxyOptions options) {
        this.options = options;
    }

    public void addListener(ProxyListener listener) { listeners.add(listener); }
    public void removeListener(ProxyListener listener) { listeners.remove(listener); }
    public void setOnHistoryUpdate(Consumer<ProxyHistoryEntry> cb) { this.onHistoryUpdate = cb; }
    public void setOnIntercept(Consumer<InterceptedRequest> cb) { this.onIntercept = cb; }

    public void start() throws IOException { start(options.getListenPort()); }

    public void start(int port) throws IOException {
        options.setListenPort(port);
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(options.getListenAddress(), port));
        executor = Executors.newCachedThreadPool();
        running = true;
        executor.submit(this::acceptLoop);
        notifyStateChange(true);
    }

    public void stop() {
        running = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        if (executor != null) executor.shutdownNow();
        notifyStateChange(false);
    }

    public boolean isRunning() { return running; }
    public int getPort() { return options.getListenPort(); }
    public ProxyOptions getOptions() { return options; }
    public void setOptions(ProxyOptions options) { this.options = options; }
    public List<ProxyHistoryEntry> getHistory() { return new ArrayList<>(history); }
    public void clearHistory() { history.clear(); }

    public void forwardIntercepted(int id) {
        InterceptedRequest req = interceptedRequests.get(id);
        if (req != null) { req.forward = true; req.edited = true; }
    }

    public void dropIntercepted(int id) {
        InterceptedRequest req = interceptedRequests.get(id);
        if (req != null) req.drop = true;
    }

    public void updateInterceptedRequest(int id, String headers, String body) {
        InterceptedRequest req = interceptedRequests.get(id);
        if (req != null) { req.requestHeaders = headers; req.requestBody = body; req.edited = true; }
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket client = serverSocket.accept();
                executor.submit(() -> handleClient(client));
            } catch (IOException e) {
                if (running) notifyError(e.getMessage());
            }
        }
    }

    private void handleClient(Socket client) {
        ProxyHistoryEntry entry = null;
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) { client.close(); return; }

            String[] parts = requestLine.split(" ", 3);
            String method = parts[0];
            String url = parts.length > 1 ? parts[1] : "/";
            String version = parts.length > 2 ? parts[2] : "HTTP/1.1";

            StringBuilder headers = new StringBuilder();
            String line;
            int contentLength = 0;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                headers.append(line).append("\r\n");
                String lower = line.toLowerCase();
                if (lower.startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.substring(15).trim());
                }
            }

            StringBuilder body = new StringBuilder();
            if (contentLength > 0) {
                char[] buf = new char[contentLength];
                int read = 0;
                while (read < contentLength) {
                    int r = reader.read(buf, read, contentLength - read);
                    if (r == -1) break;
                    read += r;
                }
                body.append(buf, 0, read);
            }

            // Parse host
            String targetHost = null; int targetPort = 80; boolean isHttps = false;
            for (String h : headers.toString().split("\r\n")) {
                if (h.toLowerCase().startsWith("host:")) {
                    String hostVal = h.substring(5).trim();
                    if (hostVal.contains(":")) {
                        String[] hp = hostVal.split(":", 2);
                        targetHost = hp[0]; targetPort = Integer.parseInt(hp[1]);
                    } else { targetHost = hostVal; }
                    break;
                }
            }

            // Apply match & replace rules to request
            String reqHeaders = headers.toString();
            String reqBody = body.toString();
            for (ProxyMatchReplaceRule rule : options.getMatchReplaceRules()) {
                if (rule.getType() == ProxyMatchReplaceRule.RuleType.REQUEST_HEADER)
                    reqHeaders = rule.apply(reqHeaders, ProxyMatchReplaceRule.RuleType.REQUEST_HEADER);
                else if (rule.getType() == ProxyMatchReplaceRule.RuleType.REQUEST_BODY)
                    reqBody = rule.apply(reqBody, ProxyMatchReplaceRule.RuleType.REQUEST_BODY);
                else if (rule.getType() == ProxyMatchReplaceRule.RuleType.REQUEST_FIRST_LINE)
                    requestLine = rule.apply(requestLine, ProxyMatchReplaceRule.RuleType.REQUEST_FIRST_LINE);
            }

            if (options.isRemoveUnsupportedAcceptEncoding()) {
                reqHeaders = reqHeaders.replaceAll("(?i)Accept-Encoding: [^\\r\\n]*\\r\\n", "");
            }

            // Create history entry
            entry = new ProxyHistoryEntry(historyIdCounter.getAndIncrement());
            entry.setMethod(method);
            entry.setUrl(url);
            entry.setHost(targetHost != null ? targetHost : "");
            entry.setPort(targetPort);
            entry.setTls(isHttps);
            entry.setRequestHeaders(reqHeaders);
            entry.setRequestBody(reqBody);
            entry.setListenerPort(options.getListenPort());
            if (targetHost != null) {
                try { entry.setIp(InetAddress.getByName(targetHost).getHostAddress()); } catch (Exception ignored) {}
            }
            // Extract params from URL
            if (url.contains("?")) {
                entry.setParams(url.substring(url.indexOf("?") + 1));
                entry.setPath(url.substring(0, url.indexOf("?")));
            } else { entry.setPath(url); entry.setParams(""); }
            // Extract extension
            String path = entry.getPath();
            if (path.contains(".")) {
                String ext = path.substring(path.lastIndexOf(".") + 1);
                if (ext.length() <= 5 && !ext.contains("/")) entry.setExtension(ext);
            }
            // Extract cookies
            for (String h : reqHeaders.split("\r\n")) {
                if (h.toLowerCase().startsWith("cookie:")) { entry.setCookies(h.substring(7).trim()); break; }
            }

            // Notify request
            for (ProxyListener l : listeners) l.onRequest(entry, method, url, reqHeaders, reqBody);

            // Intercept check
            if (options.isInterceptRequests()) {
                InterceptedRequest intercepted = new InterceptedRequest(interceptIdCounter.getAndIncrement(), entry, reqHeaders, reqBody);
                interceptedRequests.put(intercepted.id, intercepted);
                if (onIntercept != null) onIntercept.accept(intercepted);
                for (ProxyListener l : listeners) l.onIntercept(intercepted);

                // Wait for forward/drop decision
                while (!intercepted.forward && !intercepted.drop && running) {
                    Thread.sleep(50);
                }
                if (intercepted.drop) {
                    client.close();
                    return;
                }
                reqHeaders = intercepted.requestHeaders;
                reqBody = intercepted.requestBody;
                entry.setEdited(intercepted.edited);
                if (intercepted.edited) entry.setEdited(true);
            }

            // Handle CONNECT (HTTPS tunnel)
            if (method.equalsIgnoreCase("CONNECT")) {
                handleConnect(client, targetHost, targetPort, out, entry);
                return;
            }

            if (targetHost == null) { sendError(out, 400, "Bad Request"); return; }

            // Forward to target
            try (Socket target = new Socket(targetHost, targetPort)) {
                target.setSoTimeout(30000);
                OutputStream targetOut = target.getOutputStream();
                InputStream targetIn = target.getInputStream();

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(targetOut, "UTF-8"), true);
                pw.print(method + " " + url + " " + version + "\r\n");
                pw.print(reqHeaders);
                if (!reqHeaders.endsWith("\r\n")) pw.print("\r\n");
                pw.print("\r\n");
                if (reqBody.length() > 0) pw.print(reqBody);
                pw.flush();

                // Read response
                ByteArrayOutputStream responseBuf = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int n;
                // Read headers first
                StringBuilder respHeaders = new StringBuilder();
                // Read byte by byte until \r\n\r\n
                int state = 0;
                ByteArrayOutputStream headerBuf = new ByteArrayOutputStream();
                boolean headersDone = false;
                while (!headersDone) {
                    int b = targetIn.read();
                    if (b == -1) break;
                    headerBuf.write(b);
                    // Check for \r\n\r\n
                    byte[] hb = headerBuf.toByteArray();
                    if (hb.length >= 4 && hb[hb.length-4]=='\r' && hb[hb.length-3]=='\n' && hb[hb.length-2]=='\r' && hb[hb.length-1]=='\n') {
                        headersDone = true;
                    }
                }
                respHeaders.append(headerBuf.toString("UTF-8"));

                // Parse content length
                int respContentLength = -1;
                boolean chunked = false;
                for (String h : respHeaders.toString().split("\r\n")) {
                    String hl = h.toLowerCase();
                    if (hl.startsWith("content-length:")) respContentLength = Integer.parseInt(h.substring(15).trim());
                    if (hl.startsWith("transfer-encoding:") && hl.contains("chunked")) chunked = true;
                }

                // Apply match & replace to response
                String respHeadersStr = respHeaders.toString();
                for (ProxyMatchReplaceRule rule : options.getMatchReplaceRules()) {
                    if (rule.getType() == ProxyMatchReplaceRule.RuleType.RESPONSE_HEADER)
                        respHeadersStr = rule.apply(respHeadersStr, ProxyMatchReplaceRule.RuleType.RESPONSE_HEADER);
                    else if (rule.getType() == ProxyMatchReplaceRule.RuleType.RESPONSE_FIRST_LINE)
                        respHeadersStr = rule.apply(respHeadersStr, ProxyMatchReplaceRule.RuleType.RESPONSE_FIRST_LINE);
                }

                // Intercept response check
                if (options.isInterceptResponses()) {
                    // For simplicity, forward headers then body
                }

                // Send headers to client
                out.write(respHeadersStr.getBytes("UTF-8"));
                out.flush();

                // Read and forward body
                if (chunked) {
                    // Forward chunked
                    while (true) {
                        // Read chunk size line
                        StringBuilder chunkSizeLine = new StringBuilder();
                        int cb;
                        while ((cb = targetIn.read()) != -1) {
                            chunkSizeLine.append((char) cb);
                            if (chunkSizeLine.toString().endsWith("\r\n")) break;
                        }
                        out.write(chunkSizeLine.toString().getBytes("UTF-8"));
                        out.flush();
                        String sizeStr = chunkSizeLine.toString().trim();
                        int chunkSize = Integer.parseInt(sizeStr.split(";")[0].trim(), 16);
                        if (chunkSize == 0) {
                            // Read trailing \r\n
                            out.write("\r\n".getBytes());
                            out.flush();
                            break;
                        }
                        byte[] chunkBuf = new byte[chunkSize + 2];
                        int totalRead = 0;
                        while (totalRead < chunkSize + 2) {
                            int r = targetIn.read(chunkBuf, totalRead, chunkSize + 2 - totalRead);
                            if (r == -1) break;
                            totalRead += r;
                        }
                        out.write(chunkBuf, 0, totalRead);
                        out.flush();
                        responseBuf.write(chunkBuf, 0, totalRead);
                    }
                } else if (respContentLength > 0) {
                    byte[] bodyBuf = new byte[8192];
                    int remaining = respContentLength;
                    while (remaining > 0) {
                        int toRead = Math.min(buffer.length, remaining);
                        int r = targetIn.read(bodyBuf, 0, toRead);
                        if (r == -1) break;
                        out.write(bodyBuf, 0, r);
                        out.flush();
                        responseBuf.write(bodyBuf, 0, r);
                        remaining -= r;
                    }
                } else {
                    // Read until close
                    while ((n = targetIn.read(buffer)) != -1) {
                        out.write(buffer, 0, n);
                        out.flush();
                        responseBuf.write(buffer, 0, n);
                    }
                }

                // Update history entry
                String fullResponse = responseBuf.toString("UTF-8");
                String[] respParts = fullResponse.split("\r\n", 2);
                entry.setStatusCode(parseStatusCode(respParts.length > 0 ? respParts[0] : ""));
                entry.setResponseHeaders(respParts.length > 1 ? respParts[1].split("\r\n\r\n")[0] : "");
                entry.setResponseBody(respParts.length > 1 && respParts[1].contains("\r\n\r\n") ? respParts[1].substring(respParts[1].indexOf("\r\n\r\n") + 4) : "");
                entry.setResponseLength(responseBuf.size());

                // Detect mime type
                for (String h : (entry.getResponseHeaders() != null ? entry.getResponseHeaders() : "").split("\r\n")) {
                    if (h.toLowerCase().startsWith("content-type:")) {
                        entry.setMimeType(h.substring(13).trim().split(";")[0].trim());
                        break;
                    }
                }

                history.add(entry);
                if (onHistoryUpdate != null) onHistoryUpdate.accept(entry);
                for (ProxyListener l : listeners) l.onResponse(entry, entry.getStatusCode(), entry.getResponseHeaders(), entry.getResponseBody());
            }
        } catch (Exception e) {
            if (listener_error(e.getMessage())) {}
            notifyError(e.getMessage());
            try { if (entry != null) { history.add(entry); if (onHistoryUpdate != null) onHistoryUpdate.accept(entry); } } catch (Exception ignored) {}
        } finally {
            try { client.close(); } catch (IOException ignored) {}
            if (entry != null) interceptedRequests.remove(entry.getId());
        }
    }

    private void handleConnect(Socket client, String host, int port, OutputStream out, ProxyHistoryEntry entry) throws IOException {
        try (Socket target = new Socket(host, port)) {
            out.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
            out.flush();
            InputStream clientIn = client.getInputStream();
            OutputStream clientOut = client.getOutputStream();
            InputStream targetIn = target.getInputStream();
            OutputStream targetOut = target.getOutputStream();

            Thread t1 = new Thread(() -> tunnel(clientIn, targetOut, entry));
            Thread t2 = new Thread(() -> tunnel(targetIn, clientOut, entry));
            t1.setDaemon(true); t2.setDaemon(true);
            t1.start(); t2.start();
            t1.join(); t2.join();
        } catch (Exception e) { notifyError(e.getMessage()); }
    }

    private void tunnel(InputStream in, OutputStream out, ProxyHistoryEntry entry) {
        byte[] buf = new byte[8192];
        int n;
        try {
            while ((n = in.read(buf)) != -1) { out.write(buf, 0, n); out.flush(); }
        } catch (IOException ignored) {}
    }

    private void sendError(OutputStream out, int code, String msg) throws IOException {
        String response = "HTTP/1.1 " + code + " " + msg + "\r\nContent-Length: 0\r\n\r\n";
        out.write(response.getBytes());
        out.flush();
    }

    private int parseStatusCode(String statusLine) {
        try {
            String[] parts = statusLine.split(" ", 3);
            return parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        } catch (Exception e) { return 0; }
    }

    private boolean listener_error(String msg) { return false; }

    private void notifyError(String msg) {
        for (ProxyListener l : listeners) l.onError(msg);
    }

    private void notifyStateChange(boolean running) {
        for (ProxyListener l : listeners) l.onStateChange(running);
    }
}

