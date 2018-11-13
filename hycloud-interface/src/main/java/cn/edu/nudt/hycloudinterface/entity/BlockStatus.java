package cn.edu.nudt.hycloudinterface.entity;

public class BlockStatus {
    public static final int NOFOUND= 0;
    public static final int DAMAGED= 1;
    public static final int INTACT = 2;


    public static String getStatusString(int status){
        String rv = "UNKNOWN";
        switch (status){
            case BlockStatus.NOFOUND:
                rv = "NOFOUND";
                break;
            case BlockStatus.DAMAGED:
                rv = "DAMAGED";
                break;
            case BlockStatus.INTACT:
                rv = "INTACT";
                break;
        }
        return rv;
    }
}
