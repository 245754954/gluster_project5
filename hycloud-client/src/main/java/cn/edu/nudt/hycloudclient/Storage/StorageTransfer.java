package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.BlockInfo;

import java.math.BigInteger;
import java.net.MalformedURLException;

public class StorageTransfer {

    public static void addBlockInfoToManagerServer(String filename, int blockIdx, byte[] hashBytes) throws MalformedURLException {
        BigInteger hashInteger = new BigInteger(hashBytes);
        BlockInfo blockInfo = new BlockInfo(filename, blockIdx, hashInteger);

        Transfer.addBlock(blockInfo);
    }

    public static boolean verifyBlock(String filename, int blockIdx) throws MalformedURLException {
        return Transfer.verifyBlock(filename, blockIdx);
    }

}
