#include <jni.h>

#if defined(__ANDROID__)
#include <android/log.h>
#else
#include <cstdio>
#define ANDROID_LOG_INFO 4
static int __android_log_print(int /*prio*/, const char* tag, const char* text) {
    std::fprintf(stderr, "%s: %s\n", tag, text);
    return 0;
}
#endif

extern "C"
JNIEXPORT void JNICALL
Java_com_boxlauncher_NativeBridge_nativeInit(JNIEnv* env, jclass clazz) {
    (void) env;
    (void) clazz;
    __android_log_print(ANDROID_LOG_INFO, "BoxLauncher", "BoxLauncher native init");
}
