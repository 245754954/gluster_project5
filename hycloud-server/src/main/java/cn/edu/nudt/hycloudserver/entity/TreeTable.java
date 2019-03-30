package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name = "TreeTable")
public class TreeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String filename;

    @Column(columnDefinition = "longtext",nullable = true)
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
