package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.UploadInfo;

import java.io.IOException;
import java.util.List;

public class StorageTransfer {



    public static List<String> verifyBlock(List<UploadInfo> ups) throws IOException {

        return Transfer.verifyBlock(ups);
    }



}
