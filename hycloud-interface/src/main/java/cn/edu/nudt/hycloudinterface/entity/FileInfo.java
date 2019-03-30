package cn.edu.nudt.hycloudinterface.entity;

import java.util.ArrayList;
import java.util.List;

public class FileInfo {
    private String mFilename;
    private List<BlockInfo> mBlockList;

    public FileInfo(){
        this.mFilename = null;
        this.mBlockList = null;
    }

    public FileInfo(String mFilename){
        this.mFilename = mFilename;
        this.mBlockList = new ArrayList<BlockInfo>();
    }
    public FileInfo(String mFilename, List<BlockInfo> mBlockList){
        this.mBlockList = mBlockList;
        this.mFilename = mFilename;
    }

    public void addBlock(int blockIdx, byte[] hashBytes){
        this.mBlockList.add(new BlockInfo(blockIdx, hashBytes));
    }

    public String getmFilename() {
        return mFilename;
    }

    public void setmFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public List<BlockInfo> getmBlockList() {
        return mBlockList;
    }

    public void setmBlockList(List<BlockInfo> mBlockList) {
        this.mBlockList = mBlockList;
    }
}
