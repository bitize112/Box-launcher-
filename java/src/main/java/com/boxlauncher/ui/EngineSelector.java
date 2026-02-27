package com.boxlauncher.ui;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

public final class EngineSelector {
    private EngineSelector() {
    }

    public static HookEngine choose(HookEngine preferred, WorkloadProfile workload) {
        if (preferred != null) {
            return preferred;
        }

        var scores = new EnumMap<HookEngine, Double>(HookEngine.class);
        for (HookEngine engine : HookEngine.values()) {
            scores.put(engine, score(engine, workload));
        }

        return scores.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }

    private static double score(HookEngine engine, WorkloadProfile workload) {
        return switch (workload) {
            case HEAVY_INSTRUMENTATION -> switch (engine) {
                case FRIDA_GUM -> 9.6;
                case SHADOWHOOK -> 7.6;
                case AND64INLINEHOOK -> 6.5;
            };
            case PERFORMANCE_CRITICAL -> switch (engine) {
                case AND64INLINEHOOK -> 9.7;
                case SHADOWHOOK -> 8.1;
                case FRIDA_GUM -> 5.3;
            };
            case PRODUCTION_SAFE -> switch (engine) {
                case SHADOWHOOK -> 9.8;
                case AND64INLINEHOOK -> 7.1;
                case FRIDA_GUM -> 6.0;
            };
            case BALANCED -> switch (engine) {
                case SHADOWHOOK -> 8.8;
                case AND64INLINEHOOK -> 8.0;
                case FRIDA_GUM -> 7.0;
            };
        };
    }
}
