#include <stdio.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/xattr.h>
#include <sys/types.h>
#include "cn_edu_nudt_hycloudserver_util_DispatchHandler.h"

void testset(){
    char key[7] = {'u','s','e','r','.','#','\0'};
    char value[2] = {'#','\0'};
    int i, ret;

    for(i = 0; i < 10; i++){
    key[5] = value[0] = '0'+i;
    ret = setxattr("test", key, value, 2, 0);
    }
}

void testlist(){
    char buf[1000];
    int ret, i=0, j = 0;
    printf("The key on test are:\n");
    ret = listxattr("test", buf, 1000);
    while(i < ret){
    printf("%s\n", buf+i);
    i += strlen(buf+i);
    i++;
    }
}

void testremove(){
    char key[7] = "user.2";
    int ret;
    ret = removexattr("test", key);
    printf("%d\n", ret);
}

void testget(){
    char key[16] = "trusted.b.3.abc";
    char value[200];
    int ret, i;
    printf("The <key,value> on test are:\n");
    for(i = 0; i < 11; i++){
    key[5] = '0'+i;
    ret = getxattr("./f.txt", key, value, sizeof(value));
    if(ret != -1)
        printf("<%s,%s>\n", key, value);
    }
}


JNIEXPORT void JNICALL Java_cn_edu_nudt_hycloudserver_util_DispatchHandler_greeting(JNIEnv *env, jclass)
{
    testget();
    printf("Hello Native!!\n");
}