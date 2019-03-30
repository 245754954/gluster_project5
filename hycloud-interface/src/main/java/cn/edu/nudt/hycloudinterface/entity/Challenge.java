package cn.edu.nudt.hycloudinterface.entity;

public class Challenge {
    private String filename;

    private Long blockNum;

    public Challenge() {
    }

    public Challenge(String filename, Long blockNum) {
        this.filename = filename;
        this.blockNum = blockNum;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }
}
