package com.burpsuiteclone.core.proxy;

import java.util.Date;

public class ProxyHistoryEntry {
    private int id;
    private String host;
    private int port;
    private boolean tls;
    private String method;
    private String url;
    private String path;
    private String params;
    private boolean edited;
    private int statusCode;
    private int responseLength;
    private String mimeType;
    private String extension;
    private String title;
    private String comment;
    private String ip;
    private String cookies;
    private Date time;
    private String requestHeaders;
    private String requestBody;
    private String responseHeaders;
    private String responseBody;
    private int listenerPort;

    public ProxyHistoryEntry(int id) {
        this.id = id;
        this.time = new Date();
        this.edited = false;
        this.statusCode = 0;
        this.responseLength = 0;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public boolean isTls() { return tls; }
    public void setTls(boolean tls) { this.tls = tls; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }
    public boolean isEdited() { return edited; }
    public void setEdited(boolean edited) { this.edited = edited; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public int getResponseLength() { return responseLength; }
    public void setResponseLength(int responseLength) { this.responseLength = responseLength; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getCookies() { return cookies; }
    public void setCookies(String cookies) { this.cookies = cookies; }
    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }
    public String getRequestHeaders() { return requestHeaders; }
    public void setRequestHeaders(String requestHeaders) { this.requestHeaders = requestHeaders; }
    public String getRequestBody() { return requestBody; }
    public void setRequestBody(String requestBody) { this.requestBody = requestBody; }
    public String getResponseHeaders() { return responseHeaders; }
    public void setResponseHeaders(String responseHeaders) { this.responseHeaders = responseHeaders; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public int getListenerPort() { return listenerPort; }
    public void setListenerPort(int listenerPort) { this.listenerPort = listenerPort; }
}
