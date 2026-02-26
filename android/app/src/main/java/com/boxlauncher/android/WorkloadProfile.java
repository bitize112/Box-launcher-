package com.boxlauncher.android;

public enum WorkloadProfile {
    BALANCED("Balanced"),
    HEAVY_INSTRUMENTATION("Heavy Instrumentation"),
    PERFORMANCE_CRITICAL("Performance Critical"),
    PRODUCTION_SAFE("Production Safe");

    private final String label;

    WorkloadProfile(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
