package cn.edu.nudt.hycloudclient.util;


import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.Node;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class TestTransfer {

    @Test
    public void test1()throws Exception{
            String filename = "bigdata";
            FileVo f= new FileVo(filename);

            ModulationTree mo = new ModulationTree();
            mo.setmSegmentsNum(1);

            List<Node> nodes = new ArrayList<Node>();
            Node n1 = new Node();
            Node n2 = new Node();

            n1.setmModulator(BigInteger.ONE);
            n1.setmStatus(1);
            n1.setTraversed(1);

            n2.setmModulator(BigInteger.TEN);
            n2.setmStatus(2);
            n2.setTraversed(2);

            nodes.add(n1);
            nodes.add(n2);
            mo.setmTree(nodes);

            URL url = new URL("http://127.0.0.1:8080/uploadModulationTree");

            Map<String, JSONObject> param = new HashMap<>();
            param.put("filename", JSONObject.fromObject(f));
            param.put("modulationTree", JSONObject.fromObject(mo));

            JSONObject jsonObject = doPost(url, param);


            ModulationTree tree = (ModulationTree) JSONObject.toBean(jsonObject, ModulationTree.class);









    }

    public  JSONObject doPost(URL url, Map<String, JSONObject> params) {
        HttpURLConnection httpsConn = null;
        try {
            httpsConn = (HttpURLConnection) url.openConnection();
            httpsConn.setConnectTimeout(10000);
            httpsConn.setReadTimeout(10000);
            String temp = new String();
            StringBuilder response = new StringBuilder(new String());
            httpsConn.setRequestProperty("Content-type", "application/json");
//            httpsConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpsConn.setDoOutput(true);
            httpsConn.setRequestMethod("POST");
            httpsConn.setIfModifiedSince(999999999);

            Set<String> paramKeySet = params.keySet();
            StringBuilder paramData = new StringBuilder();
            int count = 0;
            for (String key : paramKeySet) {
                paramData.append(key).append("=").append(params.get(key));
                if (count < paramKeySet.size() - 1) {
                    paramData.append("&");
                }
                count += 1;
            }

            httpsConn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");

            OutputStream outStream = httpsConn.getOutputStream();
            outStream.write(paramData.toString().getBytes());
            outStream.flush();
            outStream.close();
            InputStream in = httpsConn.getInputStream();
            BufferedReader bd = new BufferedReader(new InputStreamReader(in));
            while ((temp = bd.readLine()) != null) {
                response.append(temp);
            }
            if (response.toString().equals(""))
                return null;
            else
                return JSONObject.fromObject(response.toString());
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
