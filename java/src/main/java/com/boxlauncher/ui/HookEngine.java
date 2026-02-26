package com.boxlauncher.ui;

public enum HookEngine {
    FRIDA_GUM("frida-gum", "Frida-Gum", "Complex hooks, dynamic instrumentation, heavier runtime"),
    AND64INLINEHOOK("and64inlinehook", "And64InlineHook", "Lightweight inline hooks, high performance"),
    SHADOWHOOK("shadowhook", "ShadowHook", "Production-grade stability, safer default for release");

    private final String id;
    private final String label;
    private final String summary;

    HookEngine(String id, String label, String summary) {
        this.id = id;
        this.label = label;
        this.summary = summary;
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public String summary() {
        return summary;
    }

    @Override
    public String toString() {
        return label;
    }
}
