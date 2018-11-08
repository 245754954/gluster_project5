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


import cn.edu.nudt.hycloudserver.entity.Student;
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

/**
 * @author: xphi
 * @version: 1.0 2018/11/8
 */
public class TestClient {

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://127.0.0.1:8080/add");
        JSONObject jsonObject = doGet(url);


        Student student = new Student();
        student.setId(100);
        student.setName("haha");
        student = add(student);

        System.out.print(student.toString());
    }


    public static Student add(Student student) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/add");

        Map<String, JSONObject> param = new HashMap<>();
        param.put("student", JSONObject.fromObject(student));

        student.setName("8173298071829s");
        param.put("student1", JSONObject.fromObject(student));
        JSONObject jsonObject = doPost(url, param);

        Student student1 = (Student) JSONObject.toBean(jsonObject, Student.class);
        return student1;
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
