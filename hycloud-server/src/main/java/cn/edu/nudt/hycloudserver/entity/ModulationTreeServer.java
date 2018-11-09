package cn.edu.nudt.hycloudserver.entity;





import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class ModulationTreeServer implements Serializable {

    //全局唯一的主键,返回的是文件的路径
    @Id
    private String path;

    private int mSegmentsNum;
    //多个文件
    @OneToMany(cascade={CascadeType.ALL},fetch= FetchType.EAGER)
    private List<NodeServer> mTree;

    public ModulationTreeServer() {

    }

    public ModulationTreeServer(String path, int mSegmentsNum, List<NodeServer> mTree) {
        this.path = path;
        this.mSegmentsNum = mSegmentsNum;
        this.mTree = mTree;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getmSegmentsNum() {
        return mSegmentsNum;
    }

    public void setmSegmentsNum(int mSegmentsNum) {
        this.mSegmentsNum = mSegmentsNum;
    }

    public List<NodeServer> getmTree() {
        return mTree;
    }

    public void setmTree(List<NodeServer> mTree) {
        this.mTree = mTree;
    }

    @Override
    public String toString() {
        return "ModulationTreeServer{" +
                "path='" + path + '\'' +
                ", mSegmentsNum=" + mSegmentsNum +
                ", mTree=" + mTree +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModulationTreeServer)) return false;
        ModulationTreeServer that = (ModulationTreeServer) o;
        return getmSegmentsNum() == that.getmSegmentsNum() &&
                Objects.equals(getPath(), that.getPath()) &&
                Objects.equals(getmTree(), that.getmTree());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPath(), getmSegmentsNum(), getmTree());
    }




}
