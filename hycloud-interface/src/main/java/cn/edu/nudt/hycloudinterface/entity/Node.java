package cn.edu.nudt.hycloudinterface.entity;

import java.io.Serializable;
import java.math.BigInteger;

public class Node implements Serializable {
    private static final long serialVersionUID = -3076608727651118319L;

    // status of file segment
    public static final boolean Recoverable = true;
    public static final boolean Unrecoverable = false;

    // status of node
    public static final int Remain = 0;
    public static final int OnPath = 1;
    public static final int ToChange = 2;
    public static final int Deleted = 3;

    // for traversing
    public static final int TraversedNoneChild = 0;
    public static final int TraversedLeftChild = 1;
    public static final int TraversedBothChild = 2;

    public BigInteger mModulator;
    public int mStatus;
    public int traversed;

    public Node(BigInteger modulator, int status) {
        this.mModulator = modulator;
        this.mStatus = status;
    }
}
