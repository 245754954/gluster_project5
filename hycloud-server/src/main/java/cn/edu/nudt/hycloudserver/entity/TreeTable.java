package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class TreeTable implements Serializable {
    @Id
    @Column(length = 2048)
    private String filename;
    @Column(columnDefinition = "text")
    private String modulationTree;

    public TreeTable() {
    }

    public TreeTable(String filename, String modulationTree) {
        this.filename = filename;
        this.modulationTree = modulationTree;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getModulationTree() {
        return modulationTree;
    }

    public void setModulationTree(String modulationTree) {
        this.modulationTree = modulationTree;
    }

    @Override
    public String toString() {
        return "TreeTable{" +
                "filename='" + filename + '\'' +
                ", modulationTree='" + modulationTree + '\'' +
                '}';
    }
}
