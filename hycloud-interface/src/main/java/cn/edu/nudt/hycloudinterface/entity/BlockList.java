package cn.edu.nudt.hycloudinterface.entity;

import cn.edu.nudt.hycloudinterface.utils.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlockList implements Serializable {
    private static final long serialVersionUID = 6500270138385227048L;

    private List<Integer> blockList;
//	= new ArrayList<Integer>();

    public BlockList() {
        this.blockList = new ArrayList<Integer>();
    }

    /**
     * Generate a SegmentList containing the index from 1 to segmentsNum
     * @param blocksNum
     */
    public BlockList(int blocksNum) {
        this.blockList = new ArrayList<Integer>();
        for(int i = 0; i < blocksNum; i++) {
            this.blockList.add(i);
        }
    }

//	public SegmentList(List<Integer> segmentList) {
//		this.mSegmentList = segmentList;
//	}

    /**
     * input segment id starts with zero, while the output id starts with one to be consistent with the modulation tree
     * the method lifts the ids by one
     * @param blockList
     */
    public BlockList(List<String> blockList) {
        this.blockList = null;
        if (blockList != null){
            this.blockList = new ArrayList<Integer>();
            for(String strIdx: blockList) {
                this.blockList.add(Integer.parseInt(strIdx) + 1);
            }
        }
    }


    public void dump(String label){
        String strDel = "";
        if (this.blockList != null){
            for (int i = 0; i < this.blockList.size(); i++) {
                strDel += blockList.get(i) + ", ";
            }
        }
        helper.print(label + strDel);
    }

    public boolean add(int segIdx) {
        return this.blockList.add(segIdx);
    }

    public int get(int index) {
        return this.blockList.get(index);
    }

    public int size() {
        int rv = 0;
        if (this.blockList != null){
            rv = this.blockList.size();
        }
        return rv;
    }

    public List<Integer> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Integer> blockList) {
        this.blockList = blockList;
    }

    public int add(BlockList blockList){
        if (blockList != null){
            for (int i = 0; i < blockList.size(); i++) {
                this.blockList.add(blockList.get(i));
            }
        }
        return blockList.size();
    }
}