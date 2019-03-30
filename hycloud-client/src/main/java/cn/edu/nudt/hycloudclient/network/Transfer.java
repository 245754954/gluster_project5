package cn.edu.nudt.hycloudclient.network;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudinterface.entity.QueryInfo;
import cn.edu.nudt.hycloudinterface.entity.UploadInfo;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Transfer {


    public static String verifyBlock(List<UploadInfo> ups) throws IOException {
//        URL url = new URL(Config.getConfig().getServer_url() + "/block/verifyBlock");
        URL url = new URL(Config.getConfig().getServer_url() + "/block/verify1");

//        String filename  =up.getFilename();
//        String filename_and_path = up.getFilename_and_path();
//        Long blocknumber = up.getBlocknumber();
//        Long blocksize = up.getBlocksize();
//        Long real_size = up.getReal_size();
//        String challenge = up.getChallenge();
//        String hash_result = up.getHash_result();

//        String paramData = "filename=" + filename + "&filename_and_path=" + filename_and_path + "&challenge="+ challenge +"&blocknumber="+ blocknumber +"&blocksize="+blocksize+"&real_size="+real_size;
        BasicTransfer basic = new BasicTransfer();
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setUps(ups);

        basic.update("ups", JSON.toJSONString(queryInfo));
        String hash = basic.doPost(url);
//        String hash = BasicTransfer.doPost(url, json.toString());
//        System.out.println(json.toString());
        return hash;
    }



}
