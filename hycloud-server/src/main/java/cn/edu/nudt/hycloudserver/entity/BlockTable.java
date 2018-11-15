package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "BlockTable")
public class BlockTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String filename;

    @NotNull
    private Integer blockIdx;

    @NotNull
    private Integer copyNum;

    @NotNull
    private String hash;
    // hash = JSON.toJSONString(new BigInteger(hashBytes))

    @NotNull
    private Integer status = 0;

    public BlockTable() {
    }

    public BlockTable(@NotNull String filename, @NotNull Integer blockIdx, @NotNull Integer copyNum, @NotNull String hash, @NotNull Integer status) {
        this.filename = filename;
        this.blockIdx = blockIdx;
        this.copyNum = copyNum;
        this.hash = hash;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getBlockIdx() {
        return blockIdx;
    }

    public void setBlockIdx(Integer blockIdx) {
        this.blockIdx = blockIdx;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCopyNum() {
        return copyNum;
    }

    public void setCopyNum(Integer copyNum) {
        this.copyNum = copyNum;
    }
}
