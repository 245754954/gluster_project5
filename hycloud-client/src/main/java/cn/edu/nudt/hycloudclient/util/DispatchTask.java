package cn.edu.nudt.hycloudclient.util;

import cn.edu.nudt.hycloudclient.Storage.StorageTransfer;
import cn.edu.nudt.hycloudclient.database.StorageBase;
import cn.edu.nudt.hycloudclient.entity.UploadInfo;
import cn.edu.nudt.hycloudinterface.utils.helper;

import java.io.IOException;

public class DispatchTask implements  Runnable {

    UploadInfo up;
    int i;

    public DispatchTask(){

        up=null;
    }



    public void setUp(UploadInfo up) {
        this.up = up;
        this.i=0;
    }

    public void setI(int i) {
        this.i = i;
    }

    long tstart, tend;


    @Override
    public void run() {

        tstart = System.currentTimeMillis();

        if(null==this.up.getHash_result()){
            System.out.println("The infomation of block "+(this.i)+" wrong please check the the input!");
            return ;
        }
        String hash = null;
        try {
            hash = StorageTransfer.verifyBlock(this.up);

        if(up.getHash_result().equals(hash))
        {
            System.out.println("the block "+(i)+" is intact");
        }
        else
        {

            System.out.println("the block "+(i)+" is not intact");
        }
        tend = System.currentTimeMillis();
        helper.print("spend time :"+(tend-tstart));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
