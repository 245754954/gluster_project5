/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class cn_edu_nudt_hycloudserver_util_DispatchHandler */

#ifndef _Included_cn_edu_nudt_hycloudserver_util_DispatchHandler
#define _Included_cn_edu_nudt_hycloudserver_util_DispatchHandler
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     cn_edu_nudt_hycloudserver_util_DispatchHandler
 * Method:    greeting
 * Signature: (ZID)V
 */
JNIEXPORT void JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_greeting
  (JNIEnv *, jclass, jboolean, jint, jdouble);

/*
 * Class:     cn_edu_nudt_hycloudserver_util_DispatchHandler
 * Method:    getInt
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_getInt
  (JNIEnv *, jclass);

/*
 * Class:     cn_edu_nudt_hycloudserver_util_DispatchHandler
 * Method:    getString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_getString
  (JNIEnv *, jclass);

/*
 * Class:     cn_edu_nudt_hycloudserver_util_DispatchHandler
 * Method:    get_hash_with_blocknumber_and_challenge
 * Signature: (IILjava/lang/String;Ljava/lang/String;II)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_get_1hash_1with_1blocknumber_1and_1challenge
  (JNIEnv *, jclass, jint, jint, jstring, jstring, jint, jint,jint);

#ifdef __cplusplus
}
#endif
#endif
