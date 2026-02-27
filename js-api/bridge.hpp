#pragma once

#include <jni.h>

extern "C" JNIEXPORT jboolean JNICALL Java_com_boxlauncher_NativeBridge_installHook(
    JNIEnv* env,
    jobject thiz,
    jstring module,
    jstring pattern,
    jstring mask
);

extern "C" JNIEXPORT void JNICALL Java_com_boxlauncher_NativeBridge_uninstallAll(
    JNIEnv* env,
    jobject thiz
);
