package cn.edu.nudt.hycloudserver.entity;

import cn.edu.nudt.hycloudinterface.entity.FileStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FileTable")
public class FileTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String filename;

    @NotNull
    private Long blockNum;

    @NotNull
    private Integer status = FileStatus.INTACT;


    public FileTable() {
    }

    /**
     * status is set to true in default
     * @param filename
     * @param blockNum
     */
    public FileTable(@NotNull String filename, @NotNull Long blockNum) {
        this.filename = filename;
        this.blockNum = blockNum;
        this.status = FileStatus.INTACT;
    }

    public FileTable(@NotNull String filename, @NotNull Long blockNum, @NotNull Integer status) {
        this.filename = filename;
        this.blockNum = blockNum;
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
