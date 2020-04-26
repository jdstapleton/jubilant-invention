package com.jamesstapleton.com.bems.model;

import java.util.Map;

public class DocumentContext {
    private final Map<String, Object> ctx;

    public DocumentContext(Map<String, Object> ctx) {
        this.ctx = Map.copyOf(ctx);
    }

    public Map<String, Object> getCtx() {
        return ctx;
    }
}
