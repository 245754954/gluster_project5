package cn.edu.nudt.hycloudinterface.entity;

import cn.edu.nudt.hycloudinterface.utils.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SegmentList implements Serializable {
    private static final long serialVersionUID = 6500270138385227048L;

    private List<Integer> mSegmentList;
//	= new ArrayList<Integer>();

    public SegmentList() {
        this.mSegmentList = new ArrayList<Integer>();
    }

    /**
     * Generate a SegmentList containing the index from 1 to segmentsNum
     * @param segmentsNum
     */
    public SegmentList(int segmentsNum) {
        this.mSegmentList = new ArrayList<Integer>();
        for(int i = 1; i <= segmentsNum; i++) {
            this.mSegmentList.add(i);
        }
    }

//	public SegmentList(List<Integer> segmentList) {
//		this.mSegmentList = segmentList;
//	}

    /**
     * input segment id starts with zero, while the output id starts with one to be consistent with the modulation tree
     * the method lifts the ids by one
     * @param segmentList
     */
    public SegmentList(List<String> segmentList) {
        this.mSegmentList = null;
        if (segmentList != null){
            this.mSegmentList = new ArrayList<Integer>();
            for(String strIdx: segmentList) {
                this.mSegmentList.add(Integer.parseInt(strIdx) + 1);
            }
        }
    }

    public void dump(){
        String strDel = "";
        if (this.mSegmentList != null){
            for (int i = 0; i < this.mSegmentList.size(); i++) {
                strDel += (mSegmentList.get(i) -1) + ", ";
            }
        }
        helper.print("segments: " + strDel);
    }

    public boolean add(int segIdx) {
        return this.mSegmentList.add(segIdx);
    }

    public int get(int index) {
        return this.mSegmentList.get(index);
    }

    public int size() {
        int rv = 0;
        if (this.mSegmentList != null){
            rv = this.mSegmentList.size();
        }
        return rv;
    }

    public List<Integer> getmSegmentList() {
        return mSegmentList;
    }

    public void setmSegmentList(List<Integer> mSegmentList) {
        this.mSegmentList = mSegmentList;
    }
}