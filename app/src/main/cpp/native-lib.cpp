#include <jni.h>
#include <string>
#include <vector>

extern "C" {
    JNIEXPORT jstring
    JNICALL
    Java_com_example_satohjohn_kotlinsample_activity_MainActivity_stringFromJNI(
            JNIEnv *env,
            jobject /* this */) {
        std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
    }

    JNIEXPORT jstring
    JNICALL
    Java_com_example_satohjohn_kotlinsample_activity_MainActivity_testJNI(
            JNIEnv * env ,
            jobject obj,
            jstring word /* this */) {

        char buf[256];
        const char* src = env->GetStringUTFChars(word, 0);
        strcpy(buf, src);
        strcat(buf, "fuga");
        env->ReleaseStringUTFChars(word, src);

        return env -> NewStringUTF(buf) ;
    }
}