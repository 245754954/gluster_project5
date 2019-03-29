package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.entity.UploadInfo;
import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.BlockList;

import java.io.IOException;
import java.math.BigInteger;

public class StorageTransfer {



    public static String verifyBlock(UploadInfo up) throws IOException {

        return Transfer.verifyBlock(up);
    }



}
