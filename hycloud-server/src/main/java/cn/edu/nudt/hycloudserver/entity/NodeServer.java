package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;


@Entity
public class NodeServer implements Serializable {

    @Id
    private BigInteger mModulator;
    public  int mStatus;
    public int traversed;

    public NodeServer() {

    }

    public NodeServer(BigInteger mModulator, int mStatus, int traversed) {
        this.mModulator = mModulator;
        this.mStatus = mStatus;
        this.traversed = traversed;
    }

    public BigInteger getmModulator() {
        return mModulator;
    }

    public void setmModulator(BigInteger mModulator) {
        this.mModulator = mModulator;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public int getTraversed() {
        return traversed;
    }

    public void setTraversed(int traversed) {
        this.traversed = traversed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeServer)) return false;
        NodeServer nodeServer = (NodeServer) o;
        return getmStatus() == nodeServer.getmStatus() &&
                getTraversed() == nodeServer.getTraversed() &&
                Objects.equals(getmModulator(), nodeServer.getmModulator());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getmModulator(), getmStatus(), getTraversed());
    }

    @Override
    public String toString() {
        return "NodeServer{" +
                "mModulator=" + mModulator +
                ", mStatus=" + mStatus +
                ", traversed=" + traversed +
                '}';
    }
}
