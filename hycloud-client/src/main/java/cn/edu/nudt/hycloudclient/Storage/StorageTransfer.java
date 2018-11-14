package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.FileInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;

public class StorageTransfer {

//    public static void updateFileInfo(FileInfo fileInfo) throws MalformedURLException {
//        Transfer.updateFileInfo(fileInfo);
//    }
//
//    public static boolean verifyBlock(String filename, int blockIdx) throws MalformedURLException {
//        return Transfer.verifyBlock(filename, blockIdx);
//    }

    public static void updateFileInfo(String filename, long blockNum) throws IOException {
        Transfer.updateFileInfo(filename, blockNum);
    }

    public static int verifyFile(String filename) throws IOException {
        return Transfer.verifyFile(filename);
    }

    public static void updateBlockInfo(String filename, int blockIdx, byte[] hashBytes) throws IOException {
        BigInteger hash = new BigInteger(hashBytes);
        Transfer.updateBlockInfo(filename, blockIdx, hash);
    }

    public static int verifyBlock(String filename, int blockIdx) throws IOException {
        return Transfer.verifyBlock(filename, blockIdx);
    }

    public static int deleteFileBlocks(String filename) throws IOException {
        return Transfer.deleteFileBlocks(filename);
    }

    public static int deleteFile(String filename) throws IOException {
        return Transfer.deleteFile(filename);
    }

}
