#include <jni.h>
#include <stdio.h>
#include<stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/xattr.h>
#include <sys/types.h>
#include <fcntl.h>
#include <getchecksumattr.h>
#include <time.h>
#include "cn_edu_nudt_hycloudserver_util_DispatchHandler.h"


#define DEFAULT_VOLUME "stripe3"
#define SERVER_ADDRESS "127.0.0.1"


void testget(){
    char key[16] = "trusted.b.3.abc";
    char value[200];
    int ret, i;
    printf("The <key,value> on test are:\n");
    for(i = 0; i < 11; i++){
    key[5] = '0'+i;
    ret = getxattr("/opt/stripe_vol2/f.txt", key, value, sizeof(value));

    if(ret != -1)
        printf("<%s,%s>\n", key, value);
    }
}

/*
void get_hash_with_challenge(const char *filename,char *key,char *key_value){

//    printf("the filepath is %s\n",filepath_and_name);
//    printf("the key is %s\n",key);
     glfs_t    *fs2 = NULL;
     int        ret = 0;

     fs2 = glfs_new(DEFAULT_VOLUME);

     if (!fs2) {
        fprintf (stderr, "glfs_new: returned NULL\n");
          return ;
      }
      ret = glfs_set_volfile_server (fs2, "tcp", SERVER_ADDRESS, 24007);
      ret = glfs_set_logging (fs2, "/dev/stderr", 1);
      ret = glfs_init (fs2);
      fprintf (stderr, "glfs_init: returned %d\n", ret);

      char value[1024];
      ssize_t ret1 = glfs_getxattr(fs2,filename,key,value,sizeof(value)/(sizeof(value[0])));

      strcpy(key_value,value);
      printf("the attr is %s\n",value);




   return ;

}*/


int sum ()
{
    int x,y;
    x = 100 ;
    y = 1000;
    x += y;
    return x;
}


JNIEXPORT void JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_greeting
  (JNIEnv *env, jclass c, jboolean b, jint a, jdouble d)
{



    printf("bool %d\n",b);
    printf("a %d\n",a);
    printf("a %lf\n",d);

    testget();
    printf("Hello Native!!\n");
}

/*
 * Class:     cn_edu_nudt_hycloudserver_util_DispatchHandler
 * Method:    getInt
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_getInt
  (JNIEnv *env, jclass c){
    return sum();
  }

/*
 * Class:     cn_edu_nudt_hycloudserver_util_DispatchHandler
 * Method:    getString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_getString
  (JNIEnv *env, jclass obj){

        char buff[100]={0};
        char *str = "hello zfc";

        sprintf(buff,"%s",str);


        return (*env)->NewStringUTF(env, buff);

}

JNIEXPORT jstring JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_get_1hash_1with_1blocknumber_1and_1challenge
  (JNIEnv *env, jclass c, jint blocksize, jint offset, jstring filepath, jstring challenge, jint blocknumber,jint real_size,jint dest_offset){


        const char *filepath1 = NULL;
        const  char *challenge1 = NULL;
        filepath1 = (*env)->GetStringUTFChars(env, filepath, NULL);
            if (filepath1 == NULL)
            {
                printf("out of memory.\n");
                return NULL;
            }

            challenge1 = (*env)->GetStringUTFChars(env, challenge, NULL);
             if (challenge1 == NULL)
                {
                    printf("out of memory.\n");
                    return NULL;
                    }
        printf("the blocksize is %d\n",blocksize);
        printf("the offset is %d\n",offset);
        printf("the filepath is %s\n",filepath1);
        printf("the challenge is %s\n",challenge1);
        printf("the blocknumber is %d\n",blocknumber);
        //生成一个key，传递给服务器端的posix程序，返回他计算好的hash
        char key[128];
        sprintf(key,"trusted.%d.%s.%d.%d.%d.%d",blocknumber,challenge1,blocksize,offset,real_size,dest_offset);
        printf("the key is %s\n",key);

        char key_value[512];

//        get_hash_with_challenge(filepath1,key,key_value);
//        printf("the returned value is %s\n",key_value);
      get_attr(DEFAULT_VOLUME,SERVER_ADDRESS,filepath1,key,key_value);
      printf("the attr is %s\n",key_value);

      (*env)->ReleaseStringUTFChars(env, filepath, filepath1);
      (*env)->ReleaseStringUTFChars(env, challenge, challenge1);
       char buff[100]={0};
       char *str = "hello zfc";
       sprintf(buff,"%s",str);
     return (*env)->NewStringUTF(env, buff);

  }
