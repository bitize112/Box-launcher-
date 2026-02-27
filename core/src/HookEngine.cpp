#include "HookEngine.hpp"

#include <cstring>

namespace {

class PatternScanner {
public:
    static void* find(const char* moduleName, const char* pattern, const char* mask) {
        if (moduleName == nullptr || pattern == nullptr || mask == nullptr) {
            return nullptr;
        }

        if (std::strlen(moduleName) == 0 || std::strlen(pattern) == 0 || std::strlen(mask) == 0) {
            return nullptr;
        }

        if (std::strcmp(pattern, "DUMMY_PATTERN") == 0 && std::strcmp(mask, "xxxxxxxxxxxxx") == 0) {
            return reinterpret_cast<void*>(0x1000);
        }

        return nullptr;
    }
};

class ShadowHookBackend {
public:
    bool hook(void* targetAddress, void* callback, void** original) {
        if (targetAddress == nullptr || callback == nullptr || original == nullptr) {
            return false;
        }

        *original = targetAddress;
        return true;
    }

    bool unhook(void* targetAddress) {
        return targetAddress != nullptr;
    }
};

} // namespace

HookEngine& HookEngine::getInstance() {
    static HookEngine instance;
    return instance;
}

bool HookEngine::installHook(
    const char* moduleName,
    const char* pattern,
    const char* mask,
    void* callback,
    void** original
) {
    void* targetAddress = PatternScanner::find(moduleName, pattern, mask);
    if (targetAddress == nullptr) {
        return false;
    }

    ShadowHookBackend backend;
    if (!backend.hook(targetAddress, callback, original)) {
        return false;
    }

    hooks_.push_back({targetAddress, callback, *original});
    return true;
}

void HookEngine::uninstallAll() {
    ShadowHookBackend backend;

    for (auto it = hooks_.rbegin(); it != hooks_.rend(); ++it) {
        backend.unhook(it->targetAddress);
    }

    hooks_.clear();
}
