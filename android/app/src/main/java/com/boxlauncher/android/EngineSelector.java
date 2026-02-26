package com.boxlauncher.android;

public final class EngineSelector {
    private EngineSelector() {}

    public static HookEngine choose(HookEngine preferred, WorkloadProfile profile) {
        if (preferred != null) {
            return preferred;
        }

        return switch (profile) {
            case HEAVY_INSTRUMENTATION -> HookEngine.FRIDA_GUM;
            case PERFORMANCE_CRITICAL -> HookEngine.AND64INLINEHOOK;
            case PRODUCTION_SAFE, BALANCED -> HookEngine.SHADOWHOOK;
        };
    }
}
