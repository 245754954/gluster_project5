package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.BlockList;

import java.io.IOException;
import java.math.BigInteger;

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

    public static BlockList locateDamaged(String filename) throws IOException {
        return Transfer.locateDamaged(filename);
    }

    public static void addBlock(String filename, int blockIdx, int copyNum, byte[] hashBytes) throws IOException {
        BigInteger hash = new BigInteger(hashBytes);
        Transfer.addBlock(filename, blockIdx, copyNum, hash);
    }

//    public static void updateBlockCopyOne(String filename, int blockIdx, byte[] hashBytes) throws IOException {
//        BigInteger hash = new BigInteger(hashBytes);
//        Transfer.updateBlockCopyOne(filename, blockIdx, hash);
//    }
//
//    public static void updateBlockCopyTwo(String filename, int blockIdx, byte[] hashBytes) throws IOException {
//        BigInteger hash = new BigInteger(hashBytes);
//        Transfer.updateBlockCopyTwo(filename, blockIdx, hash);
//    }

    public static boolean recoverableBlock(String filename, int blockIdx) throws IOException {
        return Transfer.recoverableBlock(filename, blockIdx);
    }

    public static int restoreBlock(String filename, int blockIdx) throws IOException {
        return Transfer.restoreBlock(filename, blockIdx);
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
