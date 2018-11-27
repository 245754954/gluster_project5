package cn.edu.nudt.hycloudinterface.entity;

import cn.edu.nudt.hycloudinterface.utils.Assist;
import cn.edu.nudt.hycloudinterface.utils.helper;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModulationTree implements Serializable{
    private static final long serialVersionUID = 294622255506745864L;
    public static final int ModulatorBits = 160;


    private int mSegmentsNum;
    private int mNodesNum;
    private int mLeavesStart;
    private List<Node> mTree;


    public ModulationTree() {
        this.mSegmentsNum = 0;
        this.mNodesNum = 0;
        this.mLeavesStart = 0;
        this.mTree = null;
    }

    public ModulationTree(int segmentsNum, List<Node> mTree) {
        this.mSegmentsNum = segmentsNum;
        int leavesNum = calLeavesNum(this.mSegmentsNum);
        this.mNodesNum = calNodesNum(leavesNum);
        this.mLeavesStart = this.mNodesNum - leavesNum;
        this.mTree = mTree;
    }



    /**
     *
     * @param segmentsNum
     * is the actual number of segments in the file, which may not be the same with the number of leaves node in the tree.
     */
    public ModulationTree(int segmentsNum) {
        this.mSegmentsNum = segmentsNum;
        int leavesNum = calLeavesNum(this.mSegmentsNum);
        this.mNodesNum = calNodesNum(leavesNum);
        this.mLeavesStart = this.mNodesNum - leavesNum;

        this.mTree = new ArrayList<Node>();
        SecureRandom srand = new SecureRandom(); // or new SecureRandom.getInstance("Specified PRNG")
        BigInteger modulator;
        for(int i = 0; i < this.mNodesNum; i++) {
            modulator = new BigInteger(ModulatorBits, srand);
            this.mTree.add(new Node(modulator, Node.Remain));
        }
    }

    /**
     * Derive keys for all segments, including the deleted ones. The keys for the deleted ones are set to null.
     * @param masterKey
     * @return
     * - a list of keys for all segments. The keys for the deleted ones are set to null.
     */
    public List<BigInteger> deriveKeys(BigInteger masterKey){
        List<BigInteger> hashChains = new ArrayList<BigInteger>();
        List<BigInteger> keys = new ArrayList<BigInteger>();

//        int depth = 32 - Integer.numberOfLeadingZeros(mTree.size());
//        int startIndex = Assist.pow(2, depth - 1) - 1; // the index of the first node at the bottom level.

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");

            BigInteger xorTem = mTree.get(0).mModulator.xor(new BigInteger(digest.digest(masterKey.toByteArray())));
            hashChains.add(new BigInteger(digest.digest(xorTem.toByteArray())));
            if (this.mSegmentsNum == 1){
                keys.add(hashChains.get(0));
            }else{
                //			for(int i = 1; i < mTree.size(); i++) {
                for(int i = 1; i < this.mLeavesStart + mSegmentsNum; i++) {
                    int fidx = father(i);
                    xorTem = mTree.get(i).mModulator.xor(hashChains.get(fidx));
                    hashChains.add(new BigInteger(digest.digest(xorTem.toByteArray())));

                    if(i >= this.mLeavesStart) {
                        if(mTree.get(i).mStatus == Node.Deleted) {
                            keys.add(null);
                        }else {
                            keys.add(hashChains.get(i));
                        }
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return keys;
    }

    public List<Boolean> getLeafNodesStatus(){
        List<Boolean> statuses= new ArrayList<Boolean>();

//          int depth = 32 - Integer.numberOfLeadingZeros(mTree.size());
//        int startIndex = Assist.pow(2, depth - 1) - 1; // the index of the first node at the bottom level.

        for(int i = 0; i < mSegmentsNum; i++) {
            if(mTree.get(i + this.mLeavesStart).mStatus == Node.Deleted) {
                statuses.add(Node.Unrecoverable);
            }else {
                statuses.add(Node.Recoverable);
            }
        }
        return statuses;
    }

    /**
     * obtain the sub-tree that needs to be adjusted to delete the given segments.
     * @param segmentsToDelete
     * - indexes of segments to be deleted. The index starts with one
     */
    public void obtainSubTree(SegmentList segmentsToDelete) {
//        int depth = 32 - Integer.numberOfLeadingZeros(mTree.size());
//        int startIndex = Assist.pow(2, depth - 1) - 1; // the index of the first node at the bottom level

        for(int i = 0; i < segmentsToDelete.size(); i++) {
            int segIdx = segmentsToDelete.get(i);
            // index of segment starts with one, thus the range is [1, mSegmentsNum]
            if(segIdx < 1 || segIdx > this.mSegmentsNum) {
                helper.err("Error: index of segment out of range [1, " + mSegmentsNum + "]");
//				System.exit(-1);
                continue;
            }
            int treeIdx = segIdx + this.mLeavesStart - 1; // index of segment starts with one

            if(mTree.get(treeIdx).mStatus == Node.Deleted) {
                helper.print("Segment(" + segIdx + ") has been deleted before.");
                continue;
            }
            mTree.get(treeIdx).mStatus = Node.Deleted;
            while(treeIdx > 0) {
                int fidx = father(treeIdx);
                if(mTree.get(fidx).mStatus == Node.OnPath) {
                    if(mTree.get(treeIdx).mStatus == Node.Deleted
                            && mTree.get(sibling(treeIdx)).mStatus == Node.Deleted) {
                        mTree.get(fidx).mStatus = Node.Deleted;
                    }else {
                        break;
                    }
                }else if(mTree.get(fidx).mStatus == Node.Remain ||
                        mTree.get(fidx).mStatus == Node.ToChange) {
                    if(mTree.get(treeIdx).mStatus == Node.Deleted &&
                            mTree.get(sibling(treeIdx)).mStatus == Node.Deleted) {
                        mTree.get(fidx).mStatus = Node.Deleted;
                    }else {
                        mTree.get(fidx).mStatus = Node.OnPath;
                        if(mTree.get(sibling(treeIdx)).mStatus != Node.Deleted) {
                            mTree.get(sibling(treeIdx)).mStatus = Node.ToChange;
                        }
                    }
                }else {
                    System.out.println("Error Occur!");
                }
                treeIdx = fidx;
            }
        }
    }

    /**
     * Adjust the modulators indicated by the sub-tree (obtained in the previous step obtainRemoteSubTree) to keep the keys unchanged for the remain segments.
     * </br>Note that the method can only be called after the obtainSubTree or obtainRemoteSubTree method.
     * @param newMasterKey
     * @param oldMasterKey
     */
    public void adjustModulators(BigInteger newMasterKey, BigInteger oldMasterKey) {
        if(mTree.get(0).mStatus == Node.Deleted) {
            // all leaves have been deleted. Nothing to do.
            return;
        }

        HashMap<Integer, BigInteger> newOmegas = new HashMap<Integer, BigInteger>();
        HashMap<Integer, BigInteger> oldOmegas = new HashMap<Integer, BigInteger>();
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");

            int currIdx = 0;
            BigInteger temXor = mTree.get(currIdx).mModulator.xor(new BigInteger(mDigest.digest(newMasterKey.toByteArray())));
            newOmegas.put(currIdx, new BigInteger(mDigest.digest(temXor.toByteArray())));

            temXor = mTree.get(currIdx).mModulator.xor(new BigInteger(mDigest.digest(oldMasterKey.toByteArray())));
            oldOmegas.put(currIdx, new BigInteger(mDigest.digest(temXor.toByteArray())));

            if(mTree.get(0).mStatus == Node.Remain) {
                // no segments to delete, just change a new master key
                temXor = new BigInteger(mDigest.digest(newMasterKey.toByteArray()));
                temXor = temXor.xor(new BigInteger(mDigest.digest(oldMasterKey.toByteArray())));
                mTree.get(0).mModulator = mTree.get(0).mModulator.xor(temXor);
//				Log.print("No segments to delete, just change a new master key");
                return;
            }

            mTree.get(currIdx).traversed = 1;
            currIdx = 2 * currIdx + 1;
            while(currIdx >= 0) {
                if(mTree.get(currIdx).mStatus == Node.ToChange) {
                    temXor = newOmegas.get(father(currIdx)).xor(oldOmegas.get(father(currIdx)));
                    mTree.get(currIdx).mModulator = mTree.get(currIdx).mModulator.xor(temXor);

                    // restore status of node to remain when moving backwards to its father
                    mTree.get(currIdx).mStatus = Node.Remain;
                    mTree.get(currIdx).traversed = Node.TraversedNoneChild;
                    currIdx = father(currIdx);
                }else if(mTree.get(currIdx).mStatus == Node.Deleted) {
                    // Do not restore the status of deleted node
                    currIdx = father(currIdx);
                }else if(mTree.get(currIdx).mStatus == Node.OnPath) {
                    if(mTree.get(currIdx).traversed == Node.TraversedNoneChild) {
                        temXor = mTree.get(currIdx).mModulator.xor(newOmegas.get(father(currIdx)));
                        newOmegas.put(currIdx, new BigInteger(mDigest.digest(temXor.toByteArray())));

                        temXor = mTree.get(currIdx).mModulator.xor(oldOmegas.get(father(currIdx)));
                        oldOmegas.put(currIdx, new BigInteger(mDigest.digest(temXor.toByteArray())));

                        mTree.get(currIdx).traversed = Node.TraversedLeftChild;
                        currIdx = 2 * currIdx + 1;
                    }else if(mTree.get(currIdx).traversed == Node.TraversedLeftChild) {
                        mTree.get(currIdx).traversed = Node.TraversedBothChild;
                        currIdx = 2 * currIdx + 2;
                    }else if(mTree.get(currIdx).traversed == Node.TraversedBothChild) {
                        // restore status of node to remain when moving backwards to its father
                        mTree.get(currIdx).mStatus = Node.Remain;
                        mTree.get(currIdx).traversed = Node.TraversedNoneChild;
                        currIdx = father(currIdx);
                    }else {
                        System.err.println("Error: Wrong -traversed- info");
                    }
                }else {
                    System.err.println("Error: Wrong Subtree.");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//
//	public void deleteSegments(List<Integer> segmentsToDelete, BigInteger newMasterKey, BigInteger oldMasterKey) {
//		obtainSubTree(segmentsToDelete);
//		adjustModulators(newMasterKey, oldMasterKey);
//	}

    /**
     * Save the key modulation tree to a file by serialization.
     * @param modulationTreePath
     */
    public void save(String modulationTreePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(modulationTreePath);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                    oos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * load a key modulation tree object from a file by reverse serialization.
     * @param modulationTreePath
     * - the path to the file stored the serialized key modulation tree object.
     * @return
     * - a key modulation tree object.
     */
    public static ModulationTree load(String modulationTreePath) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ModulationTree mTree = null;

        try {
            fis = new FileInputStream(modulationTreePath);
            ois = new ObjectInputStream(fis);

            mTree = (ModulationTree) ois.readObject();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mTree;
    }

    /**
     * Note that the index starts with zero.
     * @param index
     * @return
     * - index of the father.
     */
    private int father(int index) {
        return index > 0 ? ((index - 1) / 2): -1;
    }

    /**
     * Note that the index starts with zero.
     * @param index
     * @return
     * - the index of the sibling.
     */
    private int sibling(int index) {
        int fidx = father(index);
        return (2 * fidx + 1) == index ? (index + 1): (index - 1);
    }

//    /**
//     * Get the nodes number of the minimum complete binary tree that has no less than segmentsNum leaf nodes.
//     * @param segmentsNum
//     * - the number of segments of the corresponding file. The number of leaf nodes of the tree must be equal or larger than segmentsNum.
//     * @return
//     * - the nodes number of the minimum complete binary tree.
//     */
//    private int getNodesNum(int segmentsNum) {
//        int nBits = 31 - Integer.numberOfLeadingZeros(segmentsNum);
//        int leavesNum = Assist.pow(2, nBits);
//        if(leavesNum < segmentsNum) leavesNum = Assist.pow(2, nBits + 1);
//
//        int nodesNum = 0;
//        while(leavesNum > 0) {
//            nodesNum += leavesNum;
//            leavesNum /= 2;
//        }
//        return nodesNum;
//    }

    private int calLeavesNum(int segmentsNum){
        int nBits = 31 - Integer.numberOfLeadingZeros(segmentsNum);
        int leavesNum = Assist.pow(2, nBits);
        if(leavesNum < segmentsNum) leavesNum = Assist.pow(2, nBits + 1);
        return leavesNum;
    }
    private int calNodesNum(int leavesNum){
        int nodesNum = 0;
        while(leavesNum > 0) {
            nodesNum += leavesNum;
            leavesNum /= 2;
        }
        return nodesNum;
    }

    private int pow(int base, int power){
        int res = 1;
        for (int i = 0; i < power; i++) {
            res *= base;
        }
        return  res;
    }

    /**
     * Dump the details of the key modulation tree.
     */
    public void dump() {
//        int depth = 32 - Integer.numberOfLeadingZeros(mTree.size());
//        int leavesNum = mTree.size() - Assist.pow(2, depth - 1) + 1;
        int depth = 32 - Integer.numberOfLeadingZeros(this.mNodesNum);

        helper.print("----------------------------------------------------------------"
                + "\nKey Modulation Tree information:");
        helper.print("depth: " + depth);
        helper.print("mSegmentsNum: " + this.mSegmentsNum);
//        helper.print("leavesNum: " + leavesNum);
//        helper.print("nodesNum: " + mTree.size());
        helper.print("mLeavesStart: " + this.mLeavesStart);
        helper.print("leavesNum: " + (this.mNodesNum - this.mLeavesStart));
        helper.print("mNodesNum: " + this.mNodesNum);

        int idx = 0;
        for(Node kn: mTree) {
            helper.print("node(" + idx + "): (" + kn.mStatus + ", " + kn.traversed + ", " + kn.mModulator.toString(16) + ")");
            idx++;
        }
        helper.print("----------------------------------------------------------------");
    }

    public int getmSegmentsNum() {
        return mSegmentsNum;
    }

    public int getmNodesNum() {
        return mNodesNum;
    }

    public int getmLeavesStart() {
        return mLeavesStart;
    }

    public List<Node> getmTree() {
        return mTree;
    }

    public void setmSegmentsNum(int mSegmentsNum) {
        this.mSegmentsNum = mSegmentsNum;
    }

    public void setmNodesNum(int mNodesNum) {
        this.mNodesNum = mNodesNum;
    }

    public void setmLeavesStart(int mLeavesStart) {
        this.mLeavesStart = mLeavesStart;
    }

    public void setmTree(List<Node> mTree) {
        this.mTree = mTree;
    }

    public static void main(String[] args) throws IOException {
        ModulationTree mTree = new ModulationTree(5);
        SegmentList delSegs = new SegmentList();
        delSegs.add(2);
        delSegs.add(1);
        delSegs.add(3);
        delSegs.add(4);
        delSegs.add(5);
//		delSegs.add(6);
//		delSegs.add(7);
//		delSegs.add(8);

        mTree.obtainSubTree(delSegs);
        helper.print("\n\n************************ Dump - 1 **********************************");
        mTree.dump();

        BigInteger newMasterKey = new BigInteger("1234");
        BigInteger oldMasterKey = new BigInteger("5678");
        mTree.adjustModulators(newMasterKey, oldMasterKey);
        helper.print("\n\n************************ Dump - 2 **********************************");
        mTree.dump();

        mTree.obtainSubTree(delSegs);
        helper.print("\n\n************************ Dump - 3 **********************************");
        mTree.dump();

        mTree.adjustModulators(newMasterKey, oldMasterKey);
        helper.print("\n\n************************ Dump - 4 ************************************");
        mTree.dump();
    }
}
