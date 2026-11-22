package com.burpsuiteclone.core.sitemap;


/**
 * Site Map — tree of discovered URLs and resources.
 */
public interface SiteMapEngine {
    SiteMapNode addNode(String url, Object requestResponse);
    SiteMapNode getNode(String url);
    java.util.List<SiteMapNode> getChildren(String url);
    void removeNode(String url);
    java.util.List<SiteMapNode> search(String query);
}

interface SiteMapNode {
    String url();
    String method();
    int statusCode();
    int responseLength();
    java.util.List<SiteMapNode> children();
    Object requestResponse();
    boolean isInScope();
}
