package com.burpsuiteclone.core.issues;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScanIssue {
    private final URL url;
    private final String issueName;
    private final int issueType;
    private final String severity;
    private final String confidence;
    private final String issueBackground;
    private final String remediationBackground;
    private final String issueDetail;
    private final String remediationDetail;
    private final List<String> httpMessages;
    private final String host;
    private final int port;
    private final String protocol;
    private final long timestamp;

    public ScanIssue(URL url, String issueName, int issueType, String severity, String confidence,
                     String issueBackground, String remediationBackground, String issueDetail,
                     String remediationDetail, List<String> httpMessages, String host, int port, String protocol) {
        this.url = url;
        this.issueName = issueName;
        this.issueType = issueType;
        this.severity = severity;
        this.confidence = confidence;
        this.issueBackground = issueBackground;
        this.remediationBackground = remediationBackground;
        this.issueDetail = issueDetail;
        this.remediationDetail = remediationDetail;
        this.httpMessages = httpMessages != null ? httpMessages : new ArrayList<>();
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.timestamp = System.currentTimeMillis();
    }

    public URL getUrl() { return url; }
    public String getIssueName() { return issueName; }
    public int getIssueType() { return issueType; }
    public String getSeverity() { return severity; }
    public String getConfidence() { return confidence; }
    public String getIssueBackground() { return issueBackground; }
    public String getRemediationBackground() { return remediationBackground; }
    public String getIssueDetail() { return issueDetail; }
    public String getRemediationDetail() { return remediationDetail; }
    public List<String> getHttpMessages() { return httpMessages; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getProtocol() { return protocol; }
    public long getTimestamp() { return timestamp; }

    public static final int SEVERITY_HIGH = 0;
    public static final int SEVERITY_MEDIUM = 1;
    public static final int SEVERITY_LOW = 2;
    public static final int SEVERITY_INFORMATION = 3;

    public static String severityToString(int sev) {
        switch (sev) {
            case SEVERITY_HIGH: return "High";
            case SEVERITY_MEDIUM: return "Medium";
            case SEVERITY_LOW: return "Low";
            case SEVERITY_INFORMATION: return "Information";
            default: return "Unknown";
        }
    }
}
