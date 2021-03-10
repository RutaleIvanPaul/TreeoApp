//
// Created by Ivan Paul on 10/03/2021.
//
#include <jni.h>
#include <string>

extern "C"
jstring
Java_org_fairventures_treeo_services_ApiService_baseUrlFromJNI(
        JNIEnv* env,
        jclass clazz) {
    std::string baseURL = "https://treeo-webapp-develop.xyz/";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_fairventures_treeo_services_ApiService_00024Companion_baseUrlFromJNI(JNIEnv *env,
                                                                              jobject thiz) {
    std::string baseURL = "https://ferventdyanmics.com/";
    return env->NewStringUTF(baseURL.c_str());
}