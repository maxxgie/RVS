package com.burpsuiteclone.core.repeater;


/**
 * Repeater — manual request replay and modification.
 */
public interface RepeaterEngine {
    RepeaterTab createTab(String name, Object requestResponse);
    void sendRequest(RepeaterTab tab);
    void closeTab(RepeaterTab tab);
    java.util.List<RepeaterTab> getTabs();
}

interface RepeaterTab {
    String name();
    Object requestResponse();
    void setRequestResponse(Object rr);
    Object lastResponse();
    boolean isModified();
}
