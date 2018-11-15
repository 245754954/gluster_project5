package cn.edu.nudt.hycloudinterface.entity;

public class CopyID {
    public static final int Origin = 0;
    public static final int CopyONE = 1;
    public static final int CopyTWO = 2;

    public static int getCopyID(int index){
        int rv = 0;
        switch (index){
            case 0:
                rv = Origin;
                break;
            case 1:
                rv = CopyONE;
                break;
            case 2:
                rv = CopyTWO;
                break;

                default:
                    rv = -1;
                    break;
        }
        return rv;
    }
}
