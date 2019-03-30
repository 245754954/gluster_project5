package cn.edu.nudt.hycloudinterface.Constants;

public class RestoreResult {
    public static final int NOTFOUND = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    public static final int UNKNOWN = 3;

    public static String getString(int rv){
        switch (rv){
            case NOTFOUND:
                return "NOTFOUND";
            case SUCCESS:
                return "SUCCESS";
            case FAILED:
                return "FAILED";
        }
        return "UNKNOWN";
    }
}
