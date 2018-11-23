package cn.edu.nudt.hycloudinterface.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicTransfer {

    private StringBuilder paramData;

    public BasicTransfer() {
        paramData = new StringBuilder(50);
    }

    public void update(String key, String value){
//        paramData += key + "=" + value + "&";
        paramData.append(key).append("=").append(value).append("&");
    }

    public void reset(){
        paramData = new StringBuilder(50);
    }

    public String doPost(URL url) {
        HttpURLConnection httpsConn = null;
        try {
            httpsConn = (HttpURLConnection) url.openConnection();
            httpsConn.setConnectTimeout(30000);
            httpsConn.setReadTimeout(30000);
            httpsConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpsConn.setDoOutput(true);
            httpsConn.setRequestMethod("POST");
            httpsConn.setIfModifiedSince(999999999);
            httpsConn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
            //            Set<String> paramKeySet = params.keySet();
//            String paramData = "";
//                        String paramData = "\"filenameKey=testfile\"&blockIdxKey=1";
//            for (String key : paramKeySet) {
//                paramData = paramData + key + "=" + params.get(key) + "&";
//            }

            OutputStream outStream = httpsConn.getOutputStream();
//            outStream.write(paramData.getBytes());
            outStream.write(paramData.toString().getBytes());
            outStream.flush();
            outStream.close();

            String temp = new String();
            String response = new String();
            InputStream in = httpsConn.getInputStream();
            BufferedReader bd = new BufferedReader(new InputStreamReader(in));
            while ((temp = bd.readLine()) != null) {
                response += temp;
            }
            return response;
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
