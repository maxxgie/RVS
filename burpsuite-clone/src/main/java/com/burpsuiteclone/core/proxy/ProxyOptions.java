package com.burpsuiteclone.core.proxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyOptions {
    private int listenPort = 8080;
    private String listenAddress = "127.0.0.1";
    private boolean interceptRequests = true;
    private boolean interceptResponses = false;
    private boolean interceptWebSockets = false;
    private boolean unpackCompressedRequests = true;
    private boolean unpackCompressedResponses = true;
    private boolean removeUnsupportedAcceptEncoding = true;
    private boolean tlsEnabled = false;
    private String tlsCertificatePath = null;
    private List<ProxyMatchReplaceRule> matchReplaceRules = new ArrayList<>();
    private List<String> interceptClientRequestRules = new ArrayList<>();
    private List<String> interceptServerResponseRules = new ArrayList<>();
    private boolean httpHistoryEnabled = true;
    private int maxHistorySize = 100000;

    public int getListenPort() { return listenPort; }
    public void setListenPort(int listenPort) { this.listenPort = listenPort; }
    public String getListenAddress() { return listenAddress; }
    public void setListenAddress(String listenAddress) { this.listenAddress = listenAddress; }
    public boolean isInterceptRequests() { return interceptRequests; }
    public void setInterceptRequests(boolean interceptRequests) { this.interceptRequests = interceptRequests; }
    public boolean isInterceptResponses() { return interceptResponses; }
    public void setInterceptResponses(boolean interceptResponses) { this.interceptResponses = interceptResponses; }
    public boolean isInterceptWebSockets() { return interceptWebSockets; }
    public void setInterceptWebSockets(boolean interceptWebSockets) { this.interceptWebSockets = interceptWebSockets; }
    public boolean isUnpackCompressedRequests() { return unpackCompressedRequests; }
    public void setUnpackCompressedRequests(boolean v) { this.unpackCompressedRequests = v; }
    public boolean isUnpackCompressedResponses() { return unpackCompressedResponses; }
    public void setUnpackCompressedResponses(boolean v) { this.unpackCompressedResponses = v; }
    public boolean isRemoveUnsupportedAcceptEncoding() { return removeUnsupportedAcceptEncoding; }
    public void setRemoveUnsupportedAcceptEncoding(boolean v) { this.removeUnsupportedAcceptEncoding = v; }
    public boolean isTlsEnabled() { return tlsEnabled; }
    public void setTlsEnabled(boolean tlsEnabled) { this.tlsEnabled = tlsEnabled; }
    public String getTlsCertificatePath() { return tlsCertificatePath; }
    public void setTlsCertificatePath(String path) { this.tlsCertificatePath = path; }
    public List<ProxyMatchReplaceRule> getMatchReplaceRules() { return matchReplaceRules; }
    public void setMatchReplaceRules(List<ProxyMatchReplaceRule> rules) { this.matchReplaceRules = rules; }
    public void addMatchReplaceRule(ProxyMatchReplaceRule rule) { matchReplaceRules.add(rule); }
    public boolean isHttpHistoryEnabled() { return httpHistoryEnabled; }
    public void setHttpHistoryEnabled(boolean enabled) { this.httpHistoryEnabled = enabled; }
    public int getMaxHistorySize() { return maxHistorySize; }
    public void setMaxHistorySize(int size) { this.maxHistorySize = size; }
}
