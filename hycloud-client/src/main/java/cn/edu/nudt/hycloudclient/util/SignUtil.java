package cn.edu.nudt.hycloudclient.util;

/*
    javac -encoding UTF-8 SignUtil
* javah -classpath . -jni cn.edu.nudt.hycloudclient.util.SignUtil
*
* */
public class SignUtil {


    //String libraryDirs = System.getProperty("java.library.path");
//    // System.out.println(libraryDirs);
    static {
        com.github.fommil.jni.JniLoader.load("libhycloudnative.so");

    }
//
//    static {
//        try {
//            NativeUtils.loadLibraryFromJar("../../../../../libhycloudnative.so");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //输入消息摘要，得到公钥Y，签名W，一个圆素P，私钥X，需要保存到本地数据库
    public  static native  String[] Sign(String message,int len,String x,String p);
    //产生用户私侥X,和系统参数P
    public static native  String[] generate_x_and_p();

    public  static native int Verify(String y_str,String p_str,String w_str,String m_str);

    public static native void greeting();

    public static void main(String[] args){
//
//        String []str =  SignUtil.Sign("zfc",3);
//        if(null==str){
//
//            System.out.print("null");
//        }else{
//
//            System.out.print("not null");
//        }
//        System.out.println("the value of w_str :  "+str[0]);
//        System.out.println("the value of y_str:   "+str[1]);
//        System.out.println("the value of p_str:   "+str[2]);
//



    }

}
