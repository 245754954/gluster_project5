package cn.edu.nudt.hycloudclient.util;


import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class HttpPostUploadUtil {



    /**
     * @param args
     */
    public static void main(String[] args) {
        List<File> files = new ArrayList<File>();
        File f = new File("C:\\Users\\18392\\Desktop\\httpd2\\medir\\kart2.avi");
        files.add(f);
        upload(files);

    }



    public static  void upload(List<File> files) {
        String result = "上传失败";
        try {
            //生成一个uuid来充当随机数
            String boundary = java.util.UUID.randomUUID().toString();
            String prefix = "--";
            String linend = "\r\n";
            String multipart = "multipart/form-data";
            String charset = "UTF-8";
            String rl ="http://127.0.0.1:8080/upload/upload";

            URL url = new URL( new String(rl.getBytes(),"utf-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
			con.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("charset", "UTF-8");
            con.setRequestProperty("Content-Type", multipart + ";boundary=" + boundary);
            con.connect();

            /*--这里是模拟上传真正的文件前的字符串，总共四行--*/

            //  -----------------------------7df258a3609da
            //*注：这里两个 -- 就可以了   7df258a3609da为随机数，这里用生成的UUID来充当
            String one = "--" + boundary + linend;

            //Content-Disposition: form-data; name="file"; filename="1.txt"
            //*注：
            //1、这里的"file"就是Action中的属性名，
            //2、name="file"; filename="1.txt" 必须是双引号，不能用单引号，所以需要借助 \" 在java字符串中表示双引号。
            String two = "Content-Disposition: form-data; name=\"file\"; filename=\"kart2.avi\"" + linend;

            //Content-Type: text/plain
            //*注    这里的text/plain就是上传的文件类型，
            //如果是jpg图片则为 image/jpg png图片为 image/png 这里因为上传的txt文件，所以为text/plain

            //String three = "Content-Type: text/plain" + linend;
            //String three = "Content-Type: text/jpg" + linend;
            String three = "Content-Type:video/x-msvideo" + linend;

            // *注：这是一个空行，必须存在
            String four = linend;

            //把以上的字符串链接起来
            StringBuilder sb = new StringBuilder();
            sb.append(one);
            sb.append(two);
            sb.append(three);
            sb.append(four);
            String start = sb.toString();

            //数据输出流，用于把文件或者字符串的二进制输出到网络上
            DataOutputStream dataOut = new DataOutputStream(
                    con.getOutputStream());


            /*---------------------------------------开始上传---------------------------------------*/
            for (File file : files) {
                // 输出文件头
                dataOut.write(start.getBytes());

                // 输出文件
                InputStream is = new FileInputStream(file);
                int buffLength = 1024;
                byte[] buff = new byte[buffLength];
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    dataOut.write(buff, 0, len);
                }

                is.close();
                dataOut.write(linend.getBytes());
            }



            // 结束的换行标志
            String overFlag = linend + "--" + boundary + "--" + linend;
            dataOut.write(overFlag.getBytes());
            dataOut.flush();
            dataOut.close();
            /*--------------------------------------上传结束--------------------------------------*/

            /*--一下是读取服务器返回的信息，与上传无关--*/
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                result = temp + result + "\n";
            }
            result = result + con.getResponseCode();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }






}

