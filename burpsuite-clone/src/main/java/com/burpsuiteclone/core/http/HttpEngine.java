package com.burpsuiteclone.core.http;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Core HTTP engine handling HTTP/1.1 and HTTP/2 requests.
 * Wraps Java's HttpClient with Burp-specific features.
 */
public class HttpEngine {
    private final HttpClient httpClient;
    private final Duration timeout;

    public HttpEngine(Duration timeout) {
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(timeout)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    }

    public HttpResponse<String> send(HttpRequest request) throws Exception {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public java.util.concurrent.CompletableFuture<HttpResponse<String>> sendAsync(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
