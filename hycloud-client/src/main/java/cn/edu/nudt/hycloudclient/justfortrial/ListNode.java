package cn.edu.nudt.hycloudclient.justfortrial;

import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.Node;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.alibaba.fastjson.JSON;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ListNode {
    List<Node> mNodes;

    public ListNode(){
        mNodes = new ArrayList<Node>();
    }

    public ListNode(int num){
        mNodes = new ArrayList<Node>();
        SecureRandom sr = new SecureRandom();
        for (int i = 0; i < num; i++) {
            BigInteger bg = new BigInteger(160, sr);
            this.mNodes.add(new Node(bg, 1));
        }
    }

    public List<Node> getmNodes() {
        return mNodes;
    }

    public void setmNodes(List<Node> mNodes) {
        this.mNodes = mNodes;
    }

    public void dump(){
        for(Node nd: this.mNodes){
            helper.print("getmModulator:" + nd.getmModulator());
            helper.print("getmStatus:" + nd.getmStatus());
        }
    }

    public  static  void main(String ... argv){
//        ListNode ln = new ListNode(5);
//        ln.dump();
//
////        JSONObject jsonObject = JSONObject.fromObject(ln);
////        helper.print(jsonObject.toString());
////
////        ListNode restoreLN = (ListNode) JSONObject.toBean(jsonObject, ListNode.class);
////        restoreLN.dump();
//
//        String lnstring = JSON.toJSONString(ln);
//        helper.print("lnstring: " + lnstring);
//
//        ListNode restoreLN = JSON.parseObject(lnstring, ListNode.class);
//        restoreLN.dump();

        ModulationTree tree = new ModulationTree(4);
        tree.dump();

        String treestr = JSON.toJSONString(tree);
        helper.print(treestr);

        ModulationTree restoretree = JSON.parseObject(treestr, ModulationTree.class);
        restoretree.dump();


    }
}
