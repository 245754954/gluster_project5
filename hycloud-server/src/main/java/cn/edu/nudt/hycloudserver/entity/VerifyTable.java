package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Objects;

@Entity
public class VerifyTable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String pkey;
    private  String filename;
    private Integer blockid;
    private BigInteger hash;
    private Boolean status;

    public VerifyTable() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public VerifyTable(String filename, Integer blockid, BigInteger hash, Boolean status) {
        this.filename = filename;
        this.blockid = blockid;
        this.hash = hash;
        this.status = status;
    }

    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public Integer getBlockid() {
        return blockid;
    }

    public void setBlockid(Integer blockid) {
        this.blockid = blockid;
    }

    public BigInteger getHash() {
        return hash;
    }

    public void setHash(BigInteger hash) {
        this.hash = hash;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VerifyTable{" +
                "pkey='" + pkey + '\'' +
                ", filename='" + filename + '\'' +
                ", blockid=" + blockid +
                ", hash=" + hash +
                ", status=" + status +
                '}';
    }
}
