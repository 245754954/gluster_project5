package cn.edu.nudt.hycloudclient.network;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;

public class Transfer {

    public static void updateFileInfo(String filename, long blockNum) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "file/addFile");

//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("blockNumKey", JSON.toJSONString(blockNum));
//        BasicTransfer.doPost(url, param);
        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        basicTransfer.update("blockNumKey", JSON.toJSONString(blockNum));
        basicTransfer.doPost(url);
    }

    public static int verifyFile(String filename) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "file/verifyFile");
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        String recvStr = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        String recvStr = basicTransfer.doPost(url);
        Integer status = JSON.parseObject(recvStr, Integer.class);
//        helper.print(filename + " status: " + FileStatus.getStatusString(status));
        return status;
    }

    public static void addBlock(String filename, int blockIdx, int copyNum, BigInteger hash) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "block/addBlock");

//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("blockIdxKey", JSON.toJSONString(blockIdx));
//        param.put("copyNumKey", JSON.toJSONString(copyNum));
//        param.put("hashKey", JSON.toJSONString(hash));
//        BasicTransfer.doPost(url, param);
        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        basicTransfer.update("blockIdxKey", JSON.toJSONString(blockIdx));
        basicTransfer.update("copyNumKey", JSON.toJSONString(copyNum));
        basicTransfer.update("hashKey", JSON.toJSONString(hash));
        basicTransfer.doPost(url);
    }
//
//    public static void updateBlockCopyOne(String filename, int blockIdx, BigInteger hash) throws IOException {
//        Config config = Config.getConfig();
//        URL url = new URL(config.getManagerServerUrl() + "block/updateBlockCopyOne");
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("blockIdxKey", JSON.toJSONString(blockIdx));
//        param.put("hashKey", JSON.toJSONString(hash));
//
//        BasicTransfer.doPost(url, param);
//    }
//
//    public static void updateBlockCopyTwo(String filename, int blockIdx, BigInteger hash) throws IOException {
//        Config config = Config.getConfig();
//        URL url = new URL(config.getManagerServerUrl() + "block/updateBlockCopyTwo");
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("blockIdxKey", JSON.toJSONString(blockIdx));
//        param.put("hashKey", JSON.toJSONString(hash));
//
//        BasicTransfer.doPost(url, param);
//    }

    public static boolean recoverableBlock(String filename, int blockIdx) throws IOException {
        URL url = new URL(Config.getConfig().getManagerServerUrl() + "block/recoverableBlock");

//        BasicTransfer basicTransfer = new BasicTransfer();
//        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
//        basicTransfer.update("blockIdxKey", JSON.toJSONString(blockIdx));
        String paramData = "filenameKey=" + filename + "&blockIdxKey=" + blockIdx;
        String recvStr = BasicTransfer.doPost(url, paramData);
        helper.print("recoverableBlock: " + recvStr);
        return Boolean.parseBoolean(recvStr);

//        boolean rv = JSON.parseObject(recvStr, Boolean.class);
////        helper.print(filename + ", " + blockIdx + ", recovered = " + rv);
//        return  rv;
    }

    public static int restoreBlock(String filename, int blockIdx) throws IOException {
        URL url = new URL(Config.getConfig().getManagerServerUrl() + "block/restoreBlock");

//        Map<String, String> param = new HashMap<>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("blockIdxKey", JSON.toJSONString(blockIdx));
//        String recvStr = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        basicTransfer.update("blockIdxKey", JSON.toJSONString(blockIdx));
        String recvStr = basicTransfer.doPost(url);

        Integer rv = JSON.parseObject(recvStr, Integer.class);
//        helper.print(filename + ", " + blockIdx + ", recovered = " + rv);
        return  rv;
    }

    public static int verifyBlock(String filename, int blockIdx) throws IOException {
        URL url = new URL(Config.getConfig().getManagerServerUrl() + "block/verifyBlock");

        String paramData = "filenameKey=" + filename + "&blockIdxKey=" + blockIdx + "&";
        String recvStr = BasicTransfer.doPost(url, paramData);
        return Integer.parseInt(recvStr);
    }

    public static int deleteFileBlocks(String filename) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "block/deleteFileBlocks");
//        Map<String, String> param = new HashMap<>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        String recvStr = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        String recvStr = basicTransfer.doPost(url);

        Integer cnt = JSON.parseObject(recvStr, Integer.class);
//        helper.print(cnt + "number of blocks deleted");
        return cnt;
    }

    public static int deleteFile(String filename) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "file/deleteFile");
//        Map<String, String> param = new HashMap<>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        String recvStr = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        String recvStr = basicTransfer.doPost(url);

        Integer cnt = JSON.parseObject(recvStr, Integer.class);
//        helper.print(cnt + "number of files deleted");
        return cnt;
    }




//    public static boolean verifyBlock(String filename, int blockIdx) throws MalformedURLException {
//        URL url = new URL("http://127.0.0.1:8080/verify/verifyBlock");
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filename", JSON.toJSONString(filename));
//        param.put("blockIdx", JSON.toJSONString(blockIdx));
//
//        String recvStr = BasicTransfer.doPost(url, param);
//        return  JSON.parseObject(recvStr, Boolean.class);
//
//    }
//    public static void updateFileInfo(FileInfo fileInfo) throws MalformedURLException {
//        URL url = new URL("http://127.0.0.1:8080/verify/updateFileInfo");
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("fileInfo", JSON.toJSONString(fileInfo));
//
//        BasicTransfer.doPost(url, param);
//    }

    public static ModulationTree obtainRemoteTree(String filename) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "tree/obtainRemoteTree");
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filename", JSON.toJSONString(filename));
//        String recvString = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filename", JSON.toJSONString(filename));
        String recvString = basicTransfer.doPost(url);

        ModulationTree tree =JSON.parseObject(recvString, ModulationTree.class);
        return tree;
    }

    public static ModulationTree obtainRemoteTree(String filename, SegmentList segmentsToDelete) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "tree/obtainRemoteTreeWithDel");

//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filename", JSON.toJSONString(filename));
//        param.put("segmentsToDelete", JSON.toJSONString(segmentsToDelete));
//        String recvString = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filename", JSON.toJSONString(filename));
        basicTransfer.update("segmentsToDelete", JSON.toJSONString(segmentsToDelete));
        String recvString = basicTransfer.doPost(url);

        ModulationTree tree =JSON.parseObject(recvString, ModulationTree.class);
        return tree;
    }

    public static boolean updateModulationTree(String filename, ModulationTree tree) throws IOException {
        Config config = Config.getConfig();
        URL url = new URL(config.getManagerServerUrl() + "tree/updateModulationTree");

//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("modulationTreeKey", JSON.toJSONString(tree));
//        String recvString = BasicTransfer.doPost(url, param);

        BasicTransfer basicTransfer = new BasicTransfer();
        basicTransfer.update("filenameKey", JSON.toJSONString(filename));
        basicTransfer.update("modulationTreeKey", JSON.toJSONString(tree));
        String recvString = basicTransfer.doPost(url);

        return JSON.parseObject(recvString, Boolean.class);
    }

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

//    public static String doPost(URL url, Map<String, String> params) {
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
