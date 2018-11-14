package cn.edu.nudt.hadoop.Entry.Verify;

import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import com.alibaba.fastjson.JSON;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VerifyTransfer2 {

    public static void submitResult(String filename, BlockVerifyResultList blockVerifyResultList) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/hdfs/submitBlockVerifyResult");

        Map<String, String> param = new HashMap<String, String>();
        param.put("filenameKey", JSON.toJSONString(filename));
        param.put("blockVerifyResultListKey", JSON.toJSONString(blockVerifyResultList));

        BasicTransfer.doPost(url, param);
    }
}
