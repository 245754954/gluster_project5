package cn.edu.nudt.hycloud.hadoop.tools;

import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.Constants.BlockStatus;
import cn.edu.nudt.hycloudinterface.Constants.CopyID;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DestoryTransfer {

    public static void updateBlockInfo(CopyID copyID, String filename, int blockIdx, BlockStatus blockStatus, BigInteger hash) throws IOException {
        ProgConfig progConfig = ProgConfig.getConfig();
        URL url = new URL(progConfig.getManagerServerUrl() + "block/updateBlock");

        Map<String, String> param = new HashMap<String, String>();
        param.put("copyIDKey", JSON.toJSONString(copyID));
        param.put("filenameKey", JSON.toJSONString(filename));
        param.put("blockIdxKey", JSON.toJSONString(blockIdx));
        param.put("blockStatusKey", JSON.toJSONString(blockStatus));

        BasicTransfer.doPost(url, param);
    }
}
