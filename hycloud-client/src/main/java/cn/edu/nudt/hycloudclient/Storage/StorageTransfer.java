package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.FileInfo;

import java.net.MalformedURLException;

public class StorageTransfer {

    public static void updateFileInfo(FileInfo fileInfo) throws MalformedURLException {
        Transfer.updateFileInfo(fileInfo);
    }

    public static boolean verifyBlock(String filename, int blockIdx) throws MalformedURLException {
        return Transfer.verifyBlock(filename, blockIdx);
    }

}
