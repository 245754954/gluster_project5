package cn.edu.nudt.hycloudserver.vo;

import cn.edu.nudt.hycloudinterface.entity.SegmentList;

public class FileNameSegmentListVo {

    private String filename;

    private SegmentList segmentsToDelete;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public SegmentList getSegmentsToDelete() {
        return segmentsToDelete;
    }

    public void setSegmentsToDelete(SegmentList segmentsToDelete) {
        this.segmentsToDelete = segmentsToDelete;
    }

    @Override
    public String toString() {
        return "FileNameSegmentListVo{" +
                "filename='" + filename + '\'' +
                ", segmentsToDelete=" + segmentsToDelete +
                '}';
    }
}
