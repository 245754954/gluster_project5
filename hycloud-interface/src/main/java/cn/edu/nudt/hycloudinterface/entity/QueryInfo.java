package cn.edu.nudt.hycloudinterface.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryInfo implements Serializable {

    private List<UploadInfo> ups;

    public QueryInfo() {
        this.ups = new ArrayList<UploadInfo>();
    }

    public List<UploadInfo> getUps() {
        return ups;
    }

    public void setUps(List<UploadInfo> ups) {
        this.ups = ups;
    }
}
