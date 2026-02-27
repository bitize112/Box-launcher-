package com.boxlauncher.android;

public enum HookEngine {
    FRIDA_GUM("frida-gum", "Frida-Gum"),
    AND64INLINEHOOK("and64inlinehook", "And64InlineHook"),
    SHADOWHOOK("shadowhook", "ShadowHook");

    private final String id;
    private final String label;

    HookEngine(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
