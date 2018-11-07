package cn.edu.nudt.hycloudserver.entity;





import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class ModulationTree implements Serializable {

    //全局唯一的主键,返回的是文件的路径
    @Id
    private String path;

    private int mSegmentsNum;
    //多个文件
    @OneToMany(cascade={CascadeType.ALL},fetch= FetchType.EAGER)
    private List<Node> mTree;

    public ModulationTree() {

    }

    public ModulationTree(String path, int mSegmentsNum, List<Node> mTree) {
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

    public List<Node> getmTree() {
        return mTree;
    }

    public void setmTree(List<Node> mTree) {
        this.mTree = mTree;
    }

    @Override
    public String toString() {
        return "ModulationTree{" +
                "path='" + path + '\'' +
                ", mSegmentsNum=" + mSegmentsNum +
                ", mTree=" + mTree +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModulationTree)) return false;
        ModulationTree that = (ModulationTree) o;
        return getmSegmentsNum() == that.getmSegmentsNum() &&
                Objects.equals(getPath(), that.getPath()) &&
                Objects.equals(getmTree(), that.getmTree());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPath(), getmSegmentsNum(), getmTree());
    }




}
