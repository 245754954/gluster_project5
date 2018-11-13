package cn.edu.nudt.hycloudinterface.entity;

import java.math.BigInteger;

public class BlockInfo {
    private String mFilename;
    private int mBlockIdx;
    private BigInteger mHash;

    public BlockInfo(){
        this.mBlockIdx = -1;
        this.mFilename = null;
        this.mHash = null;
    }

    public BlockInfo(String filename, int blockIdx, BigInteger hash){
        this.mFilename = filename;
        this.mBlockIdx = blockIdx;
        this.mHash = hash;
    }

    public BlockInfo(String filename, int blockIdx, byte[] hashBytes){
        this.mFilename = filename;
        this.mBlockIdx = blockIdx;
        this.mHash = new BigInteger(hashBytes);
    }

    public String getmFilename() {
        return mFilename;
    }

    public int getmBlockIdx() {
        return mBlockIdx;
    }

    public BigInteger getmHash() {
        return mHash;
    }

    public void setmFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public void setmBlockIdx(int mBlockIdx) {
        this.mBlockIdx = mBlockIdx;
    }

    public void setmHash(BigInteger mHash) {
        this.mHash = mHash;
    }
}
