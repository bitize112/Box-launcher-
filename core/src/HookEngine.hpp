#pragma once

#include <vector>

class HookEngine {
public:
    static HookEngine& getInstance();

    bool installHook(
        const char* moduleName,
        const char* pattern,
        const char* mask,
        void* callback,
        void** original
    );

    void uninstallAll();

private:
    struct HookRecord {
        void* targetAddress;
        void* callback;
        void* original;
    };

    HookEngine() = default;
    HookEngine(const HookEngine&) = delete;
    HookEngine& operator=(const HookEngine&) = delete;

    std::vector<HookRecord> hooks_;
};
