package com.burpsuiteclone.api.montoya.http;

public interface Http {
    Object sendRequest(Object request);
    Object createRequestEngine(Object options);
    Object cookieJar();
    void registerHttpHandler(Object handler);
    void registerHttpResponseHandler(Object handler);
    void unregisterHttpHandler(Object handler);
    void unregisterHttpResponseHandler(Object handler);
    boolean isHttp2Available();
    Object utilities();
}
