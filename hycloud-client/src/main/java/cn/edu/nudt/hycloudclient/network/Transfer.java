package cn.edu.nudt.hycloudclient.network;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.entity.UploadInfo;
import cn.edu.nudt.hycloudinterface.entity.BlockList;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.utils.BasicTransfer;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;

public class Transfer {


    public static String verifyBlock(UploadInfo up) throws IOException {
        URL url = new URL(Config.getConfig().getServer_url() + "/block/verifyBlock");

        String filename  =up.getFilename();
        String filename_and_path = up.getFilename_and_path();
        Long blocknumber = up.getBlocknumber();
        Long blocksize = up.getBlocksize();
        Long real_size = up.getReal_size();
        String challenge = up.getChallenge();
        String hash_result = up.getHash_result();

        String paramData = "filename=" + filename + "&filename_and_path=" + filename_and_path + "&challenge="+ challenge +"&blocknumber="+ blocknumber +"&blocksize="+blocksize+"&real_size="+real_size;
        String hash = BasicTransfer.doPost(url, paramData);
        return hash;
    }



}
