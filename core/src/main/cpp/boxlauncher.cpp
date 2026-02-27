#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_boxlauncher_android_MainActivity_stringFromNative(JNIEnv* env, jobject /* this */) {
    std::string msg = "Hello from native libboxlauncher.so";
    return env->NewStringUTF(msg.c_str());
}
