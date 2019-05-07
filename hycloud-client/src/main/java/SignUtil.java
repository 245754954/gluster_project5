



/*
    javac -encoding UTF-8 SignUtil
* javah -classpath . -jni cn.edu.nudt.hycloudclient.util.SignUtil
*
* */
class SignUtil {


    //String libraryDirs = System.getProperty("java.library.path");
//    // System.out.println(libraryDirs);
    static {
        com.github.fommil.jni.JniLoader.load("libhycloudnative.so");

    }
//
//    static {
//        try {
//            NativeUtils.loadLibraryFromJar("libhycloudnative.so");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //输入消息摘要，得到公钥Y，签名W，一个圆素P，私钥X，需要保存到本地数据库
    public  static native  String[] Sign(String message,int len);
    public static native void greeting();

    public static void main(String[] args){

        String []str =  SignUtil.Sign("zfc",3);
        if(null==str){

            System.out.print("null");
        }else{

            System.out.print("not null");
        }

//        System.out.print(str[0]);
//        SignUtil.greeting();



    }

}
