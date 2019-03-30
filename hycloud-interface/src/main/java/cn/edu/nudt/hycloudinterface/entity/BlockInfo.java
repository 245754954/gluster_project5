package cn.edu.nudt.hycloudinterface.entity;

import java.math.BigInteger;

public class BlockInfo {

    private int mBlockIdx;
    private BigInteger mHash;
    private Boolean mStatus;

    public BlockInfo(){
        this.mBlockIdx = -1;
        this.mHash = null;
        this.mStatus = true;
    }

    public BlockInfo(int blockIdx, BigInteger hash){
        this.mBlockIdx = blockIdx;
        this.mHash = hash;
        this.mStatus = true;

    }

    public BlockInfo(int blockIdx, byte[] hashBytes){
        this.mBlockIdx = blockIdx;
        this.mHash = new BigInteger(hashBytes);
        this.mStatus = true;
    }


    public int getmBlockIdx() {
        return mBlockIdx;
    }

    public BigInteger getmHash() {
        return mHash;
    }


    public void setmBlockIdx(int mBlockIdx) {
        this.mBlockIdx = mBlockIdx;
    }

    public void setmHash(BigInteger mHash) {
        this.mHash = mHash;
    }

    public Boolean getmStatus() {
        return mStatus;
    }

    public void setmStatus(Boolean mStatus) {
        this.mStatus = mStatus;
    }



}
