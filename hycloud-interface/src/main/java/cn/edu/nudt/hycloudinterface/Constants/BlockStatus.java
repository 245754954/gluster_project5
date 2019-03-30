package cn.edu.nudt.hycloudinterface.Constants;

public class BlockStatus {
    public static final int NOFOUND= 0;
    public static final int DAMAGED= 1;
    public static final int INTACT = 2;

    private int status;

    public BlockStatus(int status) {
        this.status = status;
    }

    public BlockStatus() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString(){
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
