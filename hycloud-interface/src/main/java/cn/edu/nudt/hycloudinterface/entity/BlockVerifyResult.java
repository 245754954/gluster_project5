package cn.edu.nudt.hycloudinterface.entity;

public class BlockVerifyResult {
    private int blockIdx;
    private int status;

    public BlockVerifyResult() {
    }

    public BlockVerifyResult(int blockIdx, int status) {
        this.blockIdx = blockIdx;
        this.status = status;
    }

    public int getBlockIdx() {
        return blockIdx;
    }

    public void setBlockIdx(int blockIdx) {
        this.blockIdx = blockIdx;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
