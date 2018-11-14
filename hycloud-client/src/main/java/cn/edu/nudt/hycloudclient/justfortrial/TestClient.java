/*
 *
 * THIS FILE IS PART OF HUHANG BACK-END PROJECT
 * Copyright (c) 2018 湖南护航科技有限公司 版权所有
 * Hunan Convoy Technology co., ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of Hunan
 * Convoy Technology co., ltd.. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * contract agreement you entered into with Hunan Convoy Technology co., ltd..
 *
 */
package cn.edu.nudt.hycloudclient.justfortrial;


import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResult;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xphi
 * @version: 1.0 2018/11/8
 */
public class TestClient {

    //New test case .Added by zfc
    @Test
    public void test1()throws  Exception{
        URL url = new URL("http://127.0.0.1:8080/block//submitBlockVerifyResult");
        String filename = "jdk.tar.gz";
        Integer blockIdx =8;
        Map<String, String> param = new HashMap<>();
        param.put("filenameKey", JSON.toJSONString(filename));
        BlockVerifyResultList lis = new BlockVerifyResultList();
        BlockVerifyResult b = new BlockVerifyResult();
        b.setBlockIdx(8);
        b.setStatus(1);
        List<BlockVerifyResult> b1 = new ArrayList<BlockVerifyResult>();
        b1.add(b);
        lis.setBlockVerifyResultList(b1);
        param.put("blockVerifyResultListKey", JSON.toJSONString(lis));

        BasicTransfer.doPost(url, param);
    }

    public static void main(String[] args) throws Exception {
        addBlock("file-1", 0, "1231");
        addBlock("file-1", 1, "1273231");
        addBlock("file-1", 2, "1823231");
        addBlock("file-2", 0, "1234331");

        verifyBlock("file-1",0);
        verifyBlock("file-2",0);
        verifyBlock("file-1",2);
        verifyBlock("file-2",2);


//        addTree();
//        addNode();
//        addSegmentList();


//        URL url = new URL("http://127.0.0.1:8080/add");
//        JSONObject jsonObject = doGet(url);


//        Student student1 = new Student();
//        student1.setId(100);
//        student1.setName("student1");
//
//        Student student2 = new Student();
//        student2.setId(100);
//        student2.setName("student1");
//        Student rvstudent = add(student1, student2);
//
//        System.out.print(rvstudent.toString());

    }

    public static void addBlock(String filename, int blockIdx, String hashStr) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/block/add");

        BigInteger hash = new BigInteger(hashStr);

        Map<String, String> param = new HashMap<>();
        param.put("filenameKey", JSON.toJSONString(filename));
        param.put("blockIdxKey", JSON.toJSONString(blockIdx));
        param.put("hashKey", JSON.toJSONString(hash));

        BasicTransfer.doPost(url, param);
    }

    public static int verifyBlock(String filename, int blockIdx) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/block/verify");

        Map<String, String> param = new HashMap<>();
        param.put("filenameKey", JSON.toJSONString(filename));
        param.put("blockIdxKey", JSON.toJSONString(blockIdx));

        String recvStr = BasicTransfer.doPost(url, param);
        Integer status = JSON.parseObject(recvStr, Integer.class);
        helper.print(filename + ", " + blockIdx + ", status = " + status);
        return status;
    }




//    public static Student add(Student student1, Student student2) throws MalformedURLException {
//        URL url = new URL("http://127.0.0.1:8080/add");
//
//        Map<String, JSONObject> param = new HashMap<>();
//        param.put("student1", JSONObject.fromObject(student1));
//        param.put("student2", JSONObject.fromObject(student2));
//
//        JSONObject jsonObject = doPost(url, param);
//
//        return (Student) JSONObject.toBean(jsonObject, Student.class);
//    }
//
//    public static void addSegmentList() throws MalformedURLException {
//        URL url = new URL("http://127.0.0.1:8080/add");
//
//        SegmentList segmentList = new SegmentList(5);
//        for (int i = 0; i < segmentList.size(); i++){
//            helper.print("i: " + segmentList.get(i));
//        }
//        Map<String, JSONObject> param = new HashMap<>();
//        param.put("segmentList", JSONObject.fromObject(segmentList));
//
//        doPost(url, param);
//    }
//
//    public static void addNode() throws MalformedURLException {
//        URL url = new URL("http://127.0.0.1:8080/add");
//
//        BigInteger modulator = new BigInteger(160, new SecureRandom());
//        Node node = new Node(modulator, 1);
//        helper.print("restoreNode: " + node.getmModulator().toString());
//        helper.print("restoreNode: " + node.getmStatus());
//
//        Map<String, JSONObject> param = new HashMap<>();
//
//        JSONObject jsonObject = JSONObject.fromObject(node);
//
//        Node restoreNode = (Node) JSONObject.toBean(jsonObject, Node.class);
//        helper.print("restoreNode: " + restoreNode.getmModulator().toString());
//        helper.print("restoreNode: " + restoreNode.getmStatus());
//
////        param.put("modulationTree", jsonObject);
//
////        doPost(url, param);
//    }

    public static void addTree() throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/add");

        ModulationTree tree = new ModulationTree(4);
        tree.dump();

        Map<String, String> param = new HashMap<String, String>();
//        JSONObject jsonObject = JSONObject.fromObject(tree);
        String treeString = JSON.toJSONString(tree);
        helper.print(treeString);

        ModulationTree restoreTree = JSON.parseObject(treeString, ModulationTree.class);
        restoreTree.dump();

        param.put("modulationTree", treeString);

        String filename = "testfile.txt";
        helper.print("filename: " + filename);
        String filenameString = JSON.toJSONString(filename);
        helper.print("filenameString: " + filenameString);

        param.put("filename", filenameString);


        BasicTransfer.doPost(url, param);
//        doPost2(url, param);
    }


//    private static JSONObject doGet(URL url) {
//        HttpURLConnection httpURLConn = null;
//        try {
//            String temp = new String();
//            String response = new String();
//            httpURLConn = (HttpURLConnection) url.openConnection();
//            httpURLConn.setConnectTimeout(10000);
//            httpURLConn.setReadTimeout(10000);
//            httpURLConn.setDoOutput(true);
//            httpURLConn.setRequestMethod("GET");
//            httpURLConn.setIfModifiedSince(999999999);
//            httpURLConn.connect();
//            InputStream in = httpURLConn.getInputStream();
//            BufferedReader bd = new BufferedReader(new InputStreamReader(in));
//            while ((temp = bd.readLine()) != null) {
//                response += temp;
//            }
//            if (response.equals(""))
//                return null;
//            else
//                return JSONObject.fromObject(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (httpURLConn != null) {
//                httpURLConn.disconnect();
//            }
//        }
//    }


//    private static String doPost(URL url, Map<String, String> params) {
//        HttpURLConnection httpsConn = null;
//        try {
//            httpsConn = (HttpURLConnection) url.openConnection();
//            httpsConn.setConnectTimeout(10000);
//            httpsConn.setReadTimeout(10000);
//            String temp = new String();
//            String response = new String();
//            httpsConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//            httpsConn.setDoOutput(true);
//            httpsConn.setRequestMethod("POST");
//            httpsConn.setIfModifiedSince(999999999);
//
//            Set<String> paramKeySet = params.keySet();
//            String paramData = "";
//            for (String key : paramKeySet) {
//                paramData = paramData + key + "=" + params.get(key) + "&";
//            }
//
//            httpsConn.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
//
//            OutputStream outStream = httpsConn.getOutputStream();
//            outStream.write(paramData.getBytes());
//            outStream.flush();
//            outStream.close();
//            InputStream in = httpsConn.getInputStream();
//            BufferedReader bd = new BufferedReader(new InputStreamReader(in));
//            while ((temp = bd.readLine()) != null) {
//                response += temp;
//            }
////            if (response.equals(""))
////                return null;
////            else
////                return JSONObject.fromObject(response);
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (httpsConn != null) {
//                httpsConn.disconnect();
//            }
//        }
//    }

}
