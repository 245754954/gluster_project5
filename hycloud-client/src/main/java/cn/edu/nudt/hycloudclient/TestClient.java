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
package cn.edu.nudt.hycloudclient;


import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: xphi
 * @version: 1.0 2018/11/8
 */
public class TestClient {

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://127.0.0.1:8080/add");
        JSONObject jsonObject = doGet(url);


        System.out.print(jsonObject.get("abc"));
    }


    private static JSONObject doGet(URL url) {
        HttpURLConnection httpURLConn = null;
        try {
            String temp = new String();
            String response = new String();
            httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.setConnectTimeout(10000);
            httpURLConn.setReadTimeout(10000);
            httpURLConn.setDoOutput(true);
            httpURLConn.setRequestMethod("GET");
            httpURLConn.setIfModifiedSince(999999999);
            httpURLConn.connect();
            InputStream in = httpURLConn.getInputStream();
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
            if (httpURLConn != null) {
                httpURLConn.disconnect();
            }
        }
    }
}
