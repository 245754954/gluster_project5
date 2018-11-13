package cn.edu.nudt.hycloudserver.entity;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Entity
public class VerifyTable {
    @Id
    private String mFilename;
    @OneToMany(cascade={CascadeType.ALL},fetch= FetchType.EAGER)
    private List<BlockInfoServer> mBlockList;

    public VerifyTable() {
    }

    public VerifyTable(String mFilename, List<BlockInfoServer> mBlockList) {
        this.mFilename = mFilename;
        this.mBlockList = mBlockList;
    }

    public String getmFilename() {
        return mFilename;
    }

    public void setmFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public List<BlockInfoServer> getmBlockList() {
        return mBlockList;
    }

    public void setmBlockList(List<BlockInfoServer> mBlockList) {
        this.mBlockList = mBlockList;
    }

    @Override
    public String toString() {
        return "VerifyTable{" +
                "mFilename='" + mFilename + '\'' +
                ", mBlockList=" + mBlockList +
                '}';
    }
}
