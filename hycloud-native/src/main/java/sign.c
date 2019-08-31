#include <jni.h>
#include <stdio.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <ctype.h>
#include <unistd.h>
#include <pbc/pbc.h>
#include <pbc/pbc_test.h>
#include "SignUtil.h"


#ifndef MAX_PATH
#define MAX_PATH 256
#endif

const char * base64char = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

char * base64_encode( const unsigned char * bindata, char * base64, int binlength )
{
    int i, j;
    unsigned char current;

    for ( i = 0, j = 0 ; i < binlength ; i += 3 )
    {
        current = (bindata[i] >> 2) ;
        current &= (unsigned char)0x3F;
        base64[j++] = base64char[(int)current];

        current = ( (unsigned char)(bindata[i] << 4 ) ) & ( (unsigned char)0x30 ) ;
        if ( i + 1 >= binlength )
        {
            base64[j++] = base64char[(int)current];
            base64[j++] = '=';
            base64[j++] = '=';
            break;
        }
        current |= ( (unsigned char)(bindata[i+1] >> 4) ) & ( (unsigned char) 0x0F );
        base64[j++] = base64char[(int)current];

        current = ( (unsigned char)(bindata[i+1] << 2) ) & ( (unsigned char)0x3C ) ;
        if ( i + 2 >= binlength )
        {
            base64[j++] = base64char[(int)current];
            base64[j++] = '=';
            break;
        }
        current |= ( (unsigned char)(bindata[i+2] >> 6) ) & ( (unsigned char) 0x03 );
        base64[j++] = base64char[(int)current];

        current = ( (unsigned char)bindata[i+2] ) & ( (unsigned char)0x3F ) ;
        base64[j++] = base64char[(int)current];
    }
    base64[j] = '\0';
    return base64;
}

int base64_decode( const char * base64, unsigned char * bindata )
{
    int i, j;
    unsigned char k;
    unsigned char temp[4];
    for ( i = 0, j = 0; base64[i] != '\0' ; i += 4 )
    {
        memset( temp, 0xFF, sizeof(temp) );
        for ( k = 0 ; k < 64 ; k ++ )
        {
            if ( base64char[k] == base64[i] )
                temp[0]= k;
        }
        for ( k = 0 ; k < 64 ; k ++ )
        {
            if ( base64char[k] == base64[i+1] )
                temp[1]= k;
        }
        for ( k = 0 ; k < 64 ; k ++ )
        {
            if ( base64char[k] == base64[i+2] )
                temp[2]= k;
        }
        for ( k = 0 ; k < 64 ; k ++ )
        {
            if ( base64char[k] == base64[i+3] )
                temp[3]= k;
        }

        bindata[j++] = ((unsigned char)(((unsigned char)(temp[0] << 2))&0xFC)) |
                ((unsigned char)((unsigned char)(temp[1]>>4)&0x03));
        if ( base64[i+2] == '=' )
            break;

        bindata[j++] = ((unsigned char)(((unsigned char)(temp[1] << 4))&0xF0)) |
                ((unsigned char)((unsigned char)(temp[2]>>2)&0x0F));
        if ( base64[i+3] == '=' )
            break;

        bindata[j++] = ((unsigned char)(((unsigned char)(temp[2] << 6))&0xF0)) |
                ((unsigned char)(temp[3]&0x3F));
    }
    return j;
}

void encode(FILE * fp_in, FILE * fp_out)
{
    unsigned char bindata[2050];
    char base64[4096];
    size_t bytes;
    while ( !feof( fp_in ) )
    {
        bytes = fread( bindata, 1, 2049, fp_in );
        base64_encode( bindata, base64, bytes );
        fprintf( fp_out, "%s", base64 );
    }
}

void decode(FILE * fp_in, FILE * fp_out)
{
    int i;
    unsigned char bindata[2050];
    char base64[4096];
    size_t bytes;
    while ( !feof( fp_in ) )
    {
        for ( i = 0 ; i < 2048 ; i ++ )
        {
            base64[i] = fgetc(fp_in);
            if ( base64[i] == EOF )
                break;
            else if ( base64[i] == '\n' || base64[i] == '\r' )
                i --;
        }
        bytes = base64_decode( base64, bindata );
        fwrite( bindata, bytes, 1, fp_out );
    }
}

void help(const char * filepath)
{
    fprintf( stderr, "Usage: %s [-d] [input_filename] [-o output_filepath]\n", filepath );
    fprintf( stderr, "\t-d\tdecode data\n" );
    fprintf( stderr, "\t-o\toutput filepath\n\n" );
}


void ByteToHexStr(const unsigned char *source, char *dest, int sourceLen)

{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i++)
    {
        highByte = source[i] >> 4;
        lowByte = source[i] & 0x0f;

        highByte += 0x30;

        if (highByte > 0x39)
            dest[i * 2] = highByte + 0x07;
        else
            dest[i * 2] = highByte;

        lowByte += 0x30;
        if (lowByte > 0x39)
            dest[i * 2 + 1] = lowByte + 0x07;
        else
            dest[i * 2 + 1] = lowByte;
    }
    return;
}

void Hex2Str(const char *sSrc, char *sDest, int nSrcLen)
{
    int i;
    char szTmp[3];

    for (i = 0; i < nSrcLen; i++)
    {
        sprintf(szTmp, "%02X", (unsigned char)sSrc[i]);
        memcpy(&sDest[i * 2], szTmp, 2);
    }
    return;
}

//十六进制字符串转换为字节流
//同时为了将十六进制字符串回复成字节流
void HexStrToByte(const char *source, unsigned char *dest, int sourceLen)
{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i += 2)
    {
        highByte = toupper(source[i]);
        lowByte = toupper(source[i + 1]);

        if (highByte > 0x39)
            highByte -= 0x37;
        else
            highByte -= 0x30;

        if (lowByte > 0x39)
            lowByte -= 0x37;
        else
            lowByte -= 0x30;

        dest[i / 2] = (highByte << 4) | lowByte;
    }
    return;
}






void pbc_demo_pairing_init_me(JNIEnv *env, jclass jcls,pairing_t pairing)
{
//    jclass cls = (*env)->FindClass(env,"get_param_path");
//    jmethodID mid;
//    mid = (*env)->GetStaticMethodID(env,cls, "get_relative_path", "()Ljava/lang/String;"); //查找java方法
//    char *mysize=NULL;
//    if (mid != 0) {
//
//        jstring size = (jstring)(*env)->CallStaticObjectMethod(env,cls, mid,NULL); //调用ava方法
//
//        mysize = (char*)(*env)->GetStringUTFChars(env,size, NULL); //转换jstring为char用于显示
//
//        (*env)->ReleaseStringUTFChars(env, size, 0);
//    } else {
//        printf("%s\n","method not find");
//    }
//
//    char buf[80]={'\0'};
//    getcwd(buf,80);
//    printf("current work dir is %s\n",buf);

    char s[16384];
    FILE *fp = NULL;
     fp = fopen("a.param", "r");
    if (!fp)
        pbc_die("error opening \n");

    size_t count = fread(s, 1, 16384, fp);
    if (!count)
        pbc_die("input error");

    fclose(fp);

    if (pairing_init_set_buf(pairing, s, count))
        pbc_die("pairing init failed\n");
}

void sign(JNIEnv *env, jclass jcls,char *message,int len,const char *in_x,const char *in_p,char *out_w_str,char *out_y_str){

    pairing_t pairing;


    //生命一系列的变量
    element_t P, Y, M, W;
    element_t x;
    element_t T1, T2;
    int byte;



    pbc_demo_pairing_init_me(env,jcls,pairing);
    element_init_G1(P, pairing);
    //将变量temp1初始化为群G1中的元素
    element_init_G1(Y, pairing);
    //将变量Q初始化为群G2中的元素
    element_init_G1(M, pairing);
    //将变量temp2初始化为群G2中的元素
    element_init_G1(W, pairing);
    //将变量x初始化为群GT中的元素
    element_init_Zr(x, pairing);
    //将变量y初始化为群GT中的元素
    element_init_GT(T1, pairing);
    //将a初始化未环Zr中的元素
    element_init_GT(T2, pairing);
    //判断是否是对称配对
    if (!pairing_is_symmetric(pairing))
    {
        fprintf(stderr, "only works with symmetic pairing\n");
        exit(1);
    }
    //printf("system setup\n");
    //解析得到参数P
    int n0 =  element_length_in_bytes_x_only(P);//计算需要多大的值用于保存压缩数据的大小
    unsigned char *p_data = pbc_malloc(n0);
    base64_decode(in_p,p_data);
    element_from_bytes_x_only(P, p_data);//解压
    //element_printf("the value of P = %B\n",P);

    //解析得到密钥x
    mpz_t t4;
    mpz_init(t4);
    mpz_init_set_str(t4,in_x,10);
   // gmp_printf("value of T4 = %Zd\n", t4);
    element_set_mpz(x,t4);
   // element_printf("value of x = %B\n",x);
    mpz_clear(t4);


    element_mul_zn(Y, P, x);



    //签名
    //printf("sign phase\n");
    element_from_hash(M, message,len);
    element_mul_zn(W, M, x);




    int n1 = element_length_in_bytes_x_only(W);
    unsigned char *w_data = pbc_malloc(n1);
    element_to_bytes_x_only(w_data, W);
    base64_encode(w_data,out_w_str,n1);

   // printf("the value of out_w_str %s\n", out_w_str);




    int n2 = element_length_in_bytes_x_only(Y);
    unsigned char *y_data = pbc_malloc(n2);
    element_to_bytes_x_only(y_data, Y);
    base64_encode(y_data,out_y_str,n2);
    //printf("the value of out_y_str %s\n", out_y_str);






    pbc_free(w_data);
    pbc_free(y_data);
    pbc_free(p_data);


    element_clear(P);
    element_clear(Y);
    element_clear(M);
    element_clear(W);
    element_clear(x);
    element_clear(T1);
    element_clear(T2);
    pairing_clear(pairing);


}

int verify(JNIEnv *env, jclass jcls,char *y_str,char *p_str,char *w_str,char *m_str){

        pairing_t pairing;
       //参数
       //pbc_param_t par;
       // initialization

       //生命一系列的变量
       element_t P, Y, M, W;

       element_t T1, T2;

       pbc_demo_pairing_init_me(env,jcls,pairing);

       //将变量P初始化为群G1中的元素
       element_init_G1(P, pairing);

       //将变量temp1初始化为群G1中的元素
       element_init_G1(Y, pairing);
       //将变量Q初始化为群G2中的元素
       element_init_G1(M, pairing);
       //将变量temp2初始化为群G2中的元素
       element_init_G1(W, pairing);
       //将变量x初始化为群GT中的元素
       //将变量y初始化为群GT中的元素
       element_init_GT(T1, pairing);
       //将a初始化未环Zr中的元素
       element_init_GT(T2, pairing);

       int n0 = element_length_in_bytes_x_only(P); //计算需要多大的值用于保存压缩数据的大小
       unsigned char *p_data = pbc_malloc(n0);

       base64_decode(p_str, p_data);
       //printf("verify p_str %s\n", p_str);

       element_from_bytes_x_only(P, p_data); //解压
      // element_printf("verify p_str decompressed = %B\n", P);

       if (!pairing_is_symmetric(pairing))
       {
           fprintf(stderr, "only works with symmetic pairing\n");
           exit(1);
       }

       int n1 = element_length_in_bytes_x_only(W); //计算需要多大的值用于保存压缩数据的大小
       unsigned char *w_data = pbc_malloc(n1);
       //printf("verify w_str %s\n", w_str);

       base64_decode(w_str, w_data);

       element_from_bytes_x_only(W, w_data); //解压
       //element_printf("verify w_str decompressed = %B\n", W);

       int n2 = element_length_in_bytes_x_only(Y); //计算需要多大的值用于保存压缩数据的大小
       unsigned char *y_data = pbc_malloc(n2);
       //printf("verify y_str %s\n", y_str);

       base64_decode(y_str, y_data);
       element_from_bytes_x_only(Y, y_data); //解压
      // element_printf("verify y_str decompressed = %B\n", Y);


        int n3 = element_length_in_bytes_x_only(M); //计算需要多大的值用于保存压缩数据的大小
       unsigned char *m_data = pbc_malloc(n3);
       //printf("verify y_str %s\n", m_str);

       base64_decode(m_str, m_data);
       element_from_bytes_x_only(M, m_data); //解压
      // element_printf("verify y_str decompressed = %B\n", M);

       //element_printf("the value of M= %B\n", M);
       pairing_apply(T1, P, W, pairing);
       pairing_apply(T2, Y, M, pairing);

       pbc_free(w_data);
       pbc_free(y_data);
       pbc_free(p_data);
       pbc_free(m_data);
       int flag = 0;
       if (!element_cmp(T1, T2))
       {

           printf("the signature is valid\n");

       }
       else
       {

           element_invert(T1, T1);
           if (!element_cmp(T1, T2))
           {

               printf("the signature is valid\n");
             //  printf("signature verifies on second guess\n");

           }
           else
           {
               printf("signature does not verify\n");
               flag =1;
           }
       }

       element_clear(P);
       element_clear(Y);
       element_clear(M);
       element_clear(W);
       element_clear(T1);
       element_clear(T2);
       pairing_clear(pairing);

       if(flag){
            return 1;
       }

       return 0;

}


void generate_param(JNIEnv *env, jclass jcls,char *x_str,char *p_str,char *y_str){
    pairing_t pairing;
    //参数
    element_t P, Y;
    element_t x;

    pbc_demo_pairing_init_me(env,jcls,pairing);
    //将变量P初始化为群G1中的元素
    element_init_G1(P, pairing);
    element_init_G1(Y, pairing);
    element_init_Zr(x, pairing);

     //随机选择G1中的一个元素赋值给P
     element_random(P);
     element_random(x);
     element_mul_zn(Y, P, x);

     mpz_t t3;
     mpz_init(t3);
     element_to_mpz(t3,x);
    // gmp_printf("value of T3 = %Zd\n", t3);
     mpz_get_str(x_str,10,t3);
    // printf("value of x_str  = %s\n",x_str);
     mpz_clear(t3);


    int n1 = element_length_in_bytes_x_only(P);
    unsigned char *p_data = pbc_malloc(n1);
    element_to_bytes_x_only(p_data, P);
    base64_encode(p_data,p_str,n1);
  //  printf("the value of p_str %s\n", p_str);

     int n2 = element_length_in_bytes_x_only(Y);
     unsigned char *y_data = pbc_malloc(n2);
     element_to_bytes_x_only(y_data, Y);
     base64_encode(y_data,y_str,n2);
    // printf("the value of y_str %s\n", y_str);





    pbc_free(y_data);
    pbc_free(p_data);
    element_clear(P);
    element_clear(Y);
    element_clear(x);
    pairing_clear(pairing);
   return ;


}

/*
int main(int argc, char *argv[])
{

    char *message = "zfc";
    int len = 3;
    char w_str[256]={'\0'};
    char y_str[256]={'\0'};
    char p_str[256]={'\0'};

    sign(message,len,w_str,p_str,y_str);

    verify(message,len,w_str,p_str,y_str);


    pairing_t pairing;
    //参数
    //pbc_param_t par;
    // initialization

    //生命一系列的变量
    element_t P, Y, M, W, Y_temp, W_temp;
    element_t x;
    element_t T1, T2;
    clock_t start, stop;
    int byte;

    //以标准输入的形式，初始化配对类型的变量
    //pbc_demo_pairing_init(pairing, argc, argv);
    //第二个参数是群的阶未r，是40bit的素数，域的阶为q是50bit长的数典型至未160和512
    //pbc_param_init_a_gen(par, 40, 50);
    //pairing_init_pbc_param(pairing, par);

    //将变量P初始化为群G1中的元素

    pbc_demo_pairing_init_me(pairing);
    element_init_G1(P, pairing);
    //将变量temp1初始化为群G1中的元素

    element_init_G1(Y, pairing);
    element_init_G1(Y_temp, pairing);
    element_init_G1(W_temp, pairing);
    //将变量Q初始化为群G2中的元素
    element_init_G1(M, pairing);
    //将变量temp2初始化为群G2中的元素
    element_init_G1(W, pairing);
    //将变量x初始化为群GT中的元素
    element_init_Zr(x, pairing);
    //将变量y初始化为群GT中的元素
    element_init_GT(T1, pairing);
    //将a初始化未环Zr中的元素
    element_init_GT(T2, pairing);
    //判断是否是对称配对
    if (!pairing_is_symmetric(pairing))
    {
        fprintf(stderr, "only works with symmetic pairing\n");
        exit(1);
    }
    printf("BLS Scheme\n");
    printf("system setup\n");
    start = clock();
    //随机选择G1中的一个元素赋值给P
    element_random(P);
    element_random(x);
    element_mul_zn(Y, P, x);
    stop = clock();
    printf("the time of setup phase %fs\n", (double)(stop - start));
    element_printf("P=%B\n", P);
    element_printf("private key is x=%B\n", x);
    element_printf("public key is y=%B\n", Y);

    //签名
    start = clock();
    element_from_hash(M, "messageofsign", 13);
    element_mul_zn(W, M, x);

    stop = clock();
    printf("the time of signing phase %fs\n", (double)(stop - start));
    element_printf("the value of M %B\n", M);
    element_printf("the value of W %B\n", W);

    printf("Verify\n");
    start = clock();
    pairing_apply(T1, P, W, pairing);

    element_from_hash(M, "messageofsign", 13);
    element_printf("the value of M= %B\n", M);
    pairing_apply(T2, Y, M, pairing);

    if (!element_cmp(T1, T2))
    {

        printf("the signature is valid\n");
    }
    else
    {

        printf("the signature is not  valid\n");
    }
    stop = clock();
    printf("the time of verify phase %fs\n", (double)(stop - start));

    element_printf("origin W= %B\n", W);
    int n = element_length_in_bytes_compressed(W);
    unsigned char *w_data = pbc_malloc(n);
    unsigned char *w_data1 = pbc_malloc(n);
    element_to_bytes_compressed(w_data, W);
    printf("the value of W len %d\n", n);
    // char buf[512]={'\0'};
    // char tmp[3]={'\0'};
    // int i=0;
    // printf("coord = ");
    //将以将为什么需要将字符串专程十六进制字符串，：
    //因为c语言中一个字符占8个bit，0-255之间能表示一些特殊的字符，例如问号
    //为了打印的时候不显示特殊的字符，需要将一个字节拆成高四位和低四位，
    //高四位前面补四位0,低四位前面补四个0,这样一个字符，就变成了两个字符，
    //但是打印出来以后就没有一些特殊的符号了，更有利于人们的观察
    // for (i = 0; i < n; i++) {
    //   sprintf(tmp,"%02X", data[i]);//X 表示以十六进制形式输出 02 表示不足两位,前面补0输出
    //   strcat(buf,tmp);
    // }
    // printf("the len of strlen(buf) %ld\n",strlen(buf));
    // printf("the value of str %s\n",buf);
    char w_str[128] = {'\0'};
    //将字节数组转换成十六进制字数组，注意：十六进制字节数组只是字节数组的一个子集
    //字节数组按%c打印出来，仍然还有许多不好认识的字节，但是转成十六进制字节以后就都是可以认识的
    ByteToHexStr(w_data, w_str, n);
    printf("the value of w_str %s\n", w_str);
    printf("\n");

    //unsigned char data1[128]={'\0'};
    HexStrToByte(w_str, w_data1, strlen(w_str));
    element_from_bytes_compressed(W_temp, w_data1); //解压
    element_printf("w_temp decompressed = %B\n", W_temp);

    element_printf("origin Y= %B\n", Y);
    int n1 = element_length_in_bytes_compressed(Y);
    unsigned char *y_data = pbc_malloc(n1);
    unsigned char *y_data1 = pbc_malloc(n1);
    element_to_bytes_compressed(y_data, Y);
    printf("the value of Y len %d\n", n1);

    char y_str[128] = {'\0'};
    //将字节数组转换成十六进制字数组，注意：十六进制字节数组只是字节数组的一个子集
    //字节数组按%c打印出来，仍然还有许多不好认识的字节，但是转成十六进制字节以后就都是可以认识的
    ByteToHexStr(y_data, y_str, n1);
    printf("the value of y_str %s\n", y_str);
    printf("\n");

    //unsigned char data2[128]={'\0'};
    HexStrToByte(y_str, y_data1, strlen(y_str));

    element_from_bytes_compressed(Y_temp, y_data1); //解压
    element_printf("y_temp decompressed = %B\n", Y_temp);

    element_from_hash(M, "messageofsign", 13);

    pairing_apply(T1, P, W_temp, pairing);
    pairing_apply(T2, Y_temp, M, pairing);

    if (!element_cmp(T1, T2))
    {

        printf("the signature is valid\n");
    }
    else
    {

        printf("the signature is not  valid\n");
    }


    element_printf("origin P= %B\n", P);
    int n3 = element_length_in_bytes_compressed(P);
    unsigned char *p_data = pbc_malloc(n3);
    element_to_bytes_compressed(p_data, P);
    printf("the value of Y len %d\n", n3);

    char p_str[128] = {'\0'};
    //将字节数组转换成十六进制字数组，注意：十六进制字节数组只是字节数组的一个子集
    //字节数组按%c打印出来，仍然还有许多不好认识的字节，但是转成十六进制字节以后就都是可以认识的
    ByteToHexStr(p_data, p_str, n);
    printf("the value of p_str %s\n", p_str);
    printf("\n");

    unsigned char data3[128] = {'\0'};
    HexStrToByte(p_str, data3, strlen(p_str));

    element_from_bytes_compressed(P, data3); //解压
    element_printf("p_str decompressed = %B\n", P);


    pbc_free(w_data);
    pbc_free(y_data);

    element_clear(P);
    element_clear(Y_temp);
    element_clear(W_temp);
    element_clear(Y);
    element_clear(M);
    element_clear(W);
    element_clear(x);
    element_clear(T1);
    element_clear(T2);
    pairing_clear(pairing);

    return 0;
}*/




//签名算法
JNIEXPORT jobjectArray JNICALL Java_cn_edu_nudt_hycloudclient_util_SignUtil_Sign
  (JNIEnv *env, jclass jcls, jstring message, jint len, jstring x ,jstring p){

        char w_str[256]={'\0'};
        char y_str[256]={'\0'};
        char p_str[256]={'\0'};
        jobjectArray infos = NULL;
        const char *x1 = (*env)->GetStringUTFChars(env, x, 0);
        const char *p1 = (*env)->GetStringUTFChars(env, p, 0);
        char *m1 = (*env)->GetStringUTFChars(env, message, 0);
        sign(env, jcls,m1,len,x1,p1,w_str,y_str);
//        verify(message1,len1,w_str,p_str,y_str);

        infos = (*env)->NewObjectArray(env,3, (*env)->FindClass(env,"java/lang/String"), NULL);

        jobject str1 = (*env)->NewStringUTF(env,w_str);
        jobject str2 = (*env)->NewStringUTF(env,y_str);
        (*env)->SetObjectArrayElement(env,infos, 0, str1);
        (*env)->SetObjectArrayElement(env,infos, 1, str2);

        return infos;

  }


  JNIEXPORT jobjectArray JNICALL Java_cn_edu_nudt_hycloudclient_util_SignUtil_generate_1x_1and_1p
    (JNIEnv *env, jclass jcls){

        char x_str[256]={'\0'};
        char y_str[256]={'\0'};
        char p_str[256]={'\0'};
        jobjectArray params = NULL;

        generate_param(env, jcls,x_str,p_str,y_str);
        params = (*env)->NewObjectArray(env,3, (*env)->FindClass(env,"java/lang/String"), NULL);

        jobject str1 = (*env)->NewStringUTF(env,x_str);
        jobject str2 = (*env)->NewStringUTF(env,p_str);
        jobject str3 = (*env)->NewStringUTF(env,y_str);
        (*env)->SetObjectArrayElement(env,params, 0, str1);
        (*env)->SetObjectArrayElement(env,params, 1, str2);
        (*env)->SetObjectArrayElement(env,params, 2, str3);

         return params;



    }

JNIEXPORT void JNICALL Java_cn_edu_nudt_hycloudclient_util_SignUtil_greeting
  (JNIEnv *env, jclass jcls) {
     printf("Goodbye World!\n");

 }


/*
 * Class:     cn_edu_nudt_hycloudclient_util_SignUtil
 * Method:    Verify
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_cn_edu_nudt_hycloudclient_util_SignUtil_Verify
  (JNIEnv *env, jclass jcls, jstring y_str, jstring p_str, jstring w_str, jstring m_str){

      char *p_str1 = NULL;
      char *y_str1 = NULL;
      char *w_str1 = NULL;
      char *m_str1 = NULL;

      jboolean isCopy;	// 返回JNI_TRUE表示原字符串的拷贝，返回JNI_FALSE表示返回原字符串的指针
      p_str1 = (*env)->GetStringUTFChars(env, p_str, &isCopy);
      y_str1 = (*env)->GetStringUTFChars(env, y_str, &isCopy);
      w_str1 = (*env)->GetStringUTFChars(env, w_str, &isCopy);
      m_str1 = (*env)->GetStringUTFChars(env, m_str, &isCopy);
     // printf("p_str = %s\n",p_str);


     int flag =  verify(env, jcls,y_str1,p_str1,w_str1,m_str1);
     return flag;

  }


