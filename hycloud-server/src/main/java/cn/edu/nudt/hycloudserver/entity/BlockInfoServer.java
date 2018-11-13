package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Objects;

@Entity
public class BlockInfoServer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int mBlockIdx;
    private BigInteger mHash;
    private Boolean mStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmBlockIdx() {
        return mBlockIdx;
    }

    public void setmBlockIdx(int mBlockIdx) {
        this.mBlockIdx = mBlockIdx;
    }

    public BigInteger getmHash() {
        return mHash;
    }

    public void setmHash(BigInteger mHash) {
        this.mHash = mHash;
    }

    public Boolean getmStatus() {
        return mStatus;
    }

    public void setmStatus(Boolean mStatus) {
        this.mStatus = mStatus;
    }

    public BlockInfoServer() {
    }

    public BlockInfoServer(int mBlockIdx, BigInteger mHash, Boolean mStatus) {
        this.mBlockIdx = mBlockIdx;
        this.mHash = mHash;
        this.mStatus = mStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockInfoServer that = (BlockInfoServer) o;
        return id == that.id &&
                mBlockIdx == that.mBlockIdx &&
                Objects.equals(mHash, that.mHash) &&
                Objects.equals(mStatus, that.mStatus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mBlockIdx, mHash, mStatus);
    }
}
