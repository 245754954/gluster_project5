package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.entity.UploadInfo;
import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.BlockList;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class StorageTransfer {



    public static String verifyBlock(List<UploadInfo> ups) throws IOException {

        return Transfer.verifyBlock(ups);
    }



}
