package cn.edu.nudt.hycloud.hadoop.Verify;

import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
import cn.edu.nudt.hycloudinterface.entity.Challenge;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VerifyTransfer {


    public static Challenge fetchChallenge() throws IOException {
        URL url = new URL(ProgConfig.getConfig().getManagerServerUrl() + "hdfs/fetchChallenge");

        Map<String, String> param = new HashMap<String, String>();
        param.put("fetchChallenge", JSON.toJSONString("fetchChallenge"));

        String recvStr = BasicTransfer.doPost(url, param);
        Challenge challenge = JSON.parseObject(recvStr, Challenge.class);
        // store challenge
//        storeChallenge(challenge);
        return challenge;
    }

//    /**
//     *
//     * @param filename
//     * @param result
//     * - status of file, using FileStatus class
//     * @throws MalformedURLException
//     */
//    public static void submitResult(String filename, Integer result) throws MalformedURLException {
//        URL url = new URL("http://127.0.0.1:8080/hdfs/submitResult");
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("filenameKey", JSON.toJSONString(filename));
//        param.put("resultKey", JSON.toJSONString(result));
//
//        BasicTransfer.doPost(url, param);
//    }

    public static void submitResult(int copyID, String filename, BlockVerifyResultList blockVerifyResultList) throws IOException {
        URL url = new URL(ProgConfig.getConfig().getManagerServerUrl() + "block/submitBlockVerifyResult");

        Map<String, String> param = new HashMap<String, String>();
        param.put("copyIDKey", JSON.toJSONString(copyID));
        param.put("filenameKey", JSON.toJSONString(filename));
        param.put("blockVerifyResultListKey", JSON.toJSONString(blockVerifyResultList));

        BasicTransfer.doPost(url, param);
    }



}
