package cn.edu.nudt.hycloudinterface.entity;

import java.util.List;

public class BlockVerifyResultList {
    List<BlockVerifyResult> blockVerifyResultList;

    public BlockVerifyResultList() {
    }

    public BlockVerifyResultList(List<BlockVerifyResult> blockVerifyResultList) {
        this.blockVerifyResultList = blockVerifyResultList;
    }

    public List<BlockVerifyResult> getBlockVerifyResultList() {
        return blockVerifyResultList;
    }

    public void setBlockVerifyResultList(List<BlockVerifyResult> blockVerifyResultList) {
        this.blockVerifyResultList = blockVerifyResultList;
    }

    public void addBlockVerifyResult(BlockVerifyResult blockVerifyResult){
        this.blockVerifyResultList.add(blockVerifyResult);
    }

    public BlockVerifyResult getBlockVerifyResult(int index){
        return  this.blockVerifyResultList.get(index);
    }

    public int size(){
        return  this.blockVerifyResultList.size();
    }

    public void add(BlockVerifyResult blockVerifyResult){
        this.blockVerifyResultList.add(blockVerifyResult) ;
    }
}
