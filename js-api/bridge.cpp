#include "bridge.hpp"

#include <string>
#include <vector>

#if defined(__ANDROID__)
#include <android/log.h>
#else
#include <cstdio>
#define ANDROID_LOG_INFO 4
#define ANDROID_LOG_WARN 5
#define ANDROID_LOG_ERROR 6
static int __android_log_print(int /*priority*/, const char* tag, const char* format, ...) {
    std::fprintf(stderr, "%s: %s\n", tag, format);
    return 0;
}
#endif

namespace {

constexpr const char* kTag = "BoxLauncherBridge";

struct InstalledHook {
    std::string moduleName;
    std::string pattern;
    std::string mask;
};

class HookEngine {
public:
    static HookEngine& getInstance() {
        static HookEngine instance;
        return instance;
    }

    bool installHook(const char* moduleName, const char* pattern, const char* mask, void* /*callback*/, void** /*original*/) {
        if (moduleName == nullptr || pattern == nullptr || mask == nullptr) {
            return false;
        }

        if (std::string(moduleName).empty() || std::string(pattern).empty() || std::string(mask).empty()) {
            return false;
        }

        hooks_.push_back({moduleName, pattern, mask});
        __android_log_print(ANDROID_LOG_INFO, kTag, "Installed hook for module/pattern");
        return true;
    }

    void uninstallAll() {
        hooks_.clear();
        __android_log_print(ANDROID_LOG_INFO, kTag, "Uninstalled all hooks");
    }

private:
    HookEngine() = default;
    std::vector<InstalledHook> hooks_;
};

std::string fromJString(JNIEnv* env, jstring value) {
    if (env == nullptr || value == nullptr) {
        return {};
    }

    const char* chars = env->GetStringUTFChars(value, nullptr);
    if (chars == nullptr) {
        return {};
    }

    std::string out(chars);
    env->ReleaseStringUTFChars(value, chars);
    return out;
}

} // namespace

extern "C" JNIEXPORT jboolean JNICALL Java_com_boxlauncher_NativeBridge_installHook(
    JNIEnv* env,
    jobject /*thiz*/,
    jstring module,
    jstring pattern,
    jstring mask
) {
    const std::string moduleName = fromJString(env, module);
    const std::string patternValue = fromJString(env, pattern);
    const std::string maskValue = fromJString(env, mask);

    if (moduleName.empty() || patternValue.empty() || maskValue.empty()) {
        __android_log_print(ANDROID_LOG_WARN, kTag, "installHook rejected: module/pattern/mask must be non-empty");
        return JNI_FALSE;
    }

    void* original = nullptr;
    const bool installed = HookEngine::getInstance().installHook(
        moduleName.c_str(),
        patternValue.c_str(),
        maskValue.c_str(),
        nullptr,
        &original
    );

    if (!installed) {
        __android_log_print(ANDROID_LOG_ERROR, kTag, "installHook failed");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

extern "C" JNIEXPORT void JNICALL Java_com_boxlauncher_NativeBridge_uninstallAll(
    JNIEnv* /*env*/,
    jobject /*thiz*/
) {
    HookEngine::getInstance().uninstallAll();
}
