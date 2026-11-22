package com.burpsuiteclone.core.proxy;

/**
 * Intercepting proxy server.
 */
public class ProxyServer {
    private final int port;
    private boolean running = false;

    public ProxyServer(int port) {
        this.port = port;
    }

    public void start() {
        running = true;
        System.out.println("Proxy server started on port " + port);
    }

    public void stop() {
        running = false;
    }

    public int getPort() { return port; }
    public boolean isRunning() { return running; }
}
