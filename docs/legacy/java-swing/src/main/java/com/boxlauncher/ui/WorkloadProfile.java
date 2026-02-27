package com.boxlauncher.ui;

public enum WorkloadProfile {
    BALANCED("balanced", "Balanced", "General use with stability bias"),
    HEAVY_INSTRUMENTATION("heavyInstrumentation", "Heavy Instrumentation", "Deep and dynamic runtime hooks"),
    PERFORMANCE_CRITICAL("performanceCritical", "Performance Critical", "Low-overhead and high-FPS scenarios"),
    PRODUCTION_SAFE("productionSafe", "Production Safe", "Crash resistance and compatibility first");

    private final String id;
    private final String label;
    private final String description;

    WorkloadProfile(String id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return label;
    }
}
