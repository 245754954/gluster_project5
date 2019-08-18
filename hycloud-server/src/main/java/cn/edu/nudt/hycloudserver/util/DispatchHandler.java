package cn.edu.nudt.hycloudserver.util;

//这李采用的是JNi的方式来调用c动态库，也可以采用jna的方式

//javac -encoding UTF-8 DispatchHandler
//
//javah -encoding UTF-8  cn.edu.nudt.hycloudserver.util.DispatchHandler
//
//gcc -fPIC -I $JAVA_HOME/include -I $JAVA_HOME/include/linux -I /usr/include/ -L /usr/local/lib -lgfapi  -Xlinker --unresolved-symbols=ignore-in-shared-libs  -shared  -o libDispatchHandler.so DispatchHandler.c

//cp ./libDispatchHandler.so $JAVA_HOME/jre/lib/amd64/

//加入到/etc/ld.so.conf

/*
c语言编译的时候默认头文件查找路径是/usr/include 动态库的路径是/usr/lib    /lib   /usr/local/lib
java编译的时候默认头文件找找路径使$JAVA_HOME/include -I $JAVA_HOME/include/linux 动态库的路径使$JAVA_HOME/jre/lib/amd64/
gcc DispatchHandler.c -c -fpic -I $JAVA_HOME/include -I $JAVA_HOME/include/linux -I /usr/local/include/glusterfs/api/ -L /usr/local/lib -lgfapi  -Xlinker --unresolved-symbols=ignore-in-shared-libs

gcc -shared DispatchHandler.o  -o libDispatchHandler.so
 */


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class DispatchHandler {

    // 静态初始化代码块，保证虚拟机在第一次使用该类时就会装载库
    static {
        //String libraryDirs = System.getProperty("java.library.path");
       // System.out.println(libraryDirs);
       // System.loadLibrary( "DispatchHandler" );
    }

    // native 关键字表示本地方法，提醒编译器该方法将在外部定义
    public static native void greeting(boolean b, int a, double d);

    // 这两个方法需要c语言返回给java
    public static native int getInt();
    public static native String getString();



    //根据快的编号和challenge计算hash，进行完整性验证
    public static native String get_hash_with_blocknumber_and_challenge(int blocksize,int offset,String filepath,String challenge,int blocknumber,int real_size,int dest_offset);


    public static void main(String[] args) throws IOException {
//        String libraryDirs = System.getProperty("java.library.path");
//        System.out.println(libraryDirs);
//
//        BlockChecksum block = new BlockChecksum("/opt/stripe_vol2/f.txt","f.txt","ABC",128,3,"");
//
//        greeting(true,2,34.0,block);
//        //java调用c语言中的函数，并且返回参数，返回string类型
//        //和int类型
//        System.out.println(getInt());
        // System.out.println(getString());

        // get_hash_with_blocknumber_and_challenge(25,2,"/opt/stripe3/upload.txt","trusted.1.123abc123abc1232.40.2.20",1,10,2);

//        Path path = Paths.get("/opt/stripe_vol2/f.txt");
//        UserDefinedFileAttributeView view =
//                Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
//        String name = "1.123abc123abc1232.40.2.20";
//        ByteBuffer buf = ByteBuffer.allocate(view.size(name));
//        view.read(name, buf);
//        buf.flip();
//        String value = Charset.defaultCharset().decode(buf).toString();
//
//
//            value=value.substring(0,value.length()-1).trim();
//
//        int len = value.length();
//        System.out.println("value of len "+len);
//        System.out.println(value);

        //String hash_result =  get_hash_with_blocknumber_and_challenge("/opt/stripe_vol2/f.txt",20,2,"123456789ABC1234",1,15);
        //System.out.println(hash_result);
    }




    public static  String get_hash_with_blocknumber_and_challenge(String filepath_and_name,int blocksize, long offset,  String challenge, int blocknumber,int real_size,long dest_offset,String p,String y) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.delete( 0, sb.length() );
        sb.append(blocknumber).append(".").append(challenge).append(".").append(blocksize).append(".").append(offset).append(".").append(real_size).append(".").append(dest_offset).append(".").append(p).append(".").append(y);
        String key = sb.toString();
       // System.out.println(key);

        Path path = Paths.get(filepath_and_name);
        //System.out.println("the filepath_and_name"+filepath_and_name);
        UserDefinedFileAttributeView view =
                Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);

        ByteBuffer buf = ByteBuffer.allocate(view.size(key));

        view.read(key, buf);
        buf.flip();
        String value = Charset.defaultCharset().decode(buf).toString();


        value=value.substring(0,value.length()-1).trim();

        return value;

    }

}
