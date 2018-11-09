package cn.edu.nudt.hycloudclient.network;

import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Transfer {

    public static ModulationTree obtainRemoteTree(String filename) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/obtainRemoteTree");

        Map<String, JSONObject> param = new HashMap<>();
        param.put("filename", JSONObject.fromObject(filename));

        JSONObject jsonObject = doPost(url, param);
        ModulationTree tree = (ModulationTree) JSONObject.toBean(jsonObject, ModulationTree.class);
        return tree;
    }

    public static ModulationTree obtainRemoteTree(String filename, SegmentList segmentsToDelete) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/obtainRemoteTreeWithDel");

        Map<String, JSONObject> param = new HashMap<>();
        param.put("filename", JSONObject.fromObject(filename));
        param.put("segmentsToDelete", JSONObject.fromObject(segmentsToDelete));

        JSONObject jsonObject = doPost(url, param);
        ModulationTree tree = (ModulationTree) JSONObject.toBean(jsonObject, ModulationTree.class);
        return tree;
    }

    public static boolean updateModulationTree(String filename, ModulationTree tree) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/uploadModulationTree");

        Map<String, JSONObject> param = new HashMap<>();
        param.put("filename", JSONObject.fromObject(filename));
        param.put("ModulationTree", JSONObject.fromObject(tree));

        JSONObject jsonObject = doPost(url, param);
        return (Boolean)JSONObject.toBean(jsonObject, Boolean.class);
    }

    private static JSONObject doPost(URL url, Map<String, JSONObject> params) {
        HttpURLConnection httpsConn = null;
        try {
            httpsConn = (HttpURLConnection) url.openConnection();
            httpsConn.setConnectTimeout(10000);
            httpsConn.setReadTimeout(10000);
            String temp = new String();
            String response = new String();
            httpsConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpsConn.setDoOutput(true);
            httpsConn.setRequestMethod("POST");
            httpsConn.setIfModifiedSince(999999999);

            Set<String> paramKeySet = params.keySet();
            String paramData = "";
            for (String key : paramKeySet) {
                paramData = paramData + key + "=" + params.get(key) + "&";
            }

            httpsConn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");

            OutputStream outStream = httpsConn.getOutputStream();
            outStream.write(paramData.getBytes());
            outStream.flush();
            outStream.close();
            InputStream in = httpsConn.getInputStream();
            BufferedReader bd = new BufferedReader(new InputStreamReader(in));
            while ((temp = bd.readLine()) != null) {
                response += temp;
            }
            if (response.equals(""))
                return null;
            else
                return JSONObject.fromObject(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpsConn != null) {
                httpsConn.disconnect();
            }
        }
    }
}
