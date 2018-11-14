package cn.edu.nudt.hycloudinterface.entity;

public class FileStatus {
    public static final int NOFOUND= 0;
    public static final int DAMAGED= 1;
    public static final int INTACT = 2;


    public static String getStatusString(int status){
        String rv = "UNKNOWN";
        switch (status){
            case FileStatus.NOFOUND:
                rv = "NOFOUND";
                break;
            case FileStatus.DAMAGED:
                rv = "DAMAGED";
                break;
            case FileStatus.INTACT:
                rv = "INTACT";
                break;
        }
        return rv;
    }

}