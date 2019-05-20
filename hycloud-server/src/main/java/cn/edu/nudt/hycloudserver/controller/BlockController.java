package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.Constants.*;
import cn.edu.nudt.hycloudinterface.entity.BlockList;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
import cn.edu.nudt.hycloudinterface.entity.QueryInfo;
import cn.edu.nudt.hycloudinterface.entity.UploadInfo;
import cn.edu.nudt.hycloudinterface.utils.helper;
import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import cn.edu.nudt.hycloudserver.Dao.BlockCopyOneDao;
import cn.edu.nudt.hycloudserver.Dao.BlockCopyTwoDao;
import cn.edu.nudt.hycloudserver.Dao.BlockTableDao;
import cn.edu.nudt.hycloudserver.Dao.FileTableDao;
import cn.edu.nudt.hycloudserver.entity.BlockCopyOne;
import cn.edu.nudt.hycloudserver.entity.BlockCopyTwo;
import cn.edu.nudt.hycloudserver.entity.BlockTable;
import cn.edu.nudt.hycloudserver.entity.FileTable;
import cn.edu.nudt.hycloudserver.util.DispatchHandler;
import cn.edu.nudt.hycloudserver.util.DispatchTask;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


@RestController
@RequestMapping("/block")
public class BlockController {

    @RequestMapping(value = "/verify1", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String verify1(String ups) throws InterruptedException {

        QueryInfo info = JSON.parseObject(ups, QueryInfo.class);
        List<UploadInfo> up1 = info.getUps();
        List<String> hashList = new ArrayList<>();
        List<DispatchTask> disList = new ArrayList<>();
        //等待所有子线程执行完毕才可以
        Vector<Thread> thread_vector = new Vector<Thread>();
        for (int i = 0; i < up1.size(); i++) {

            UploadInfo up = null;
            up = up1.get(i);

            DispatchTask dis = new DispatchTask();
            disList.add(dis);
            dis.setUp(up);
            dis.setI(i);
            Thread t = new Thread(dis);
            thread_vector.add(t);
            t.start();

        }

        for(Thread t :thread_vector){
            t.join();
        }

        //主线程执行

        disList.forEach(d -> {
            hashList.add(d.getHash());
        });

       return JSON.toJSONString(hashList);

    }


    @RequestMapping(value = "/verifyBlock", method = {RequestMethod.POST})
    public String verifyBlock(String filename, String filename_and_path, String challenge, String blocknumber, String blocksize, String real_size,String p,String y) throws IOException {

        Integer blocknumber1 = Integer.parseInt(blocknumber);
        Integer blocksize1 = Integer.parseInt(blocksize);
        Integer real_size1 = Integer.parseInt(real_size);
        Integer offset = blocknumber1 * blocksize1;

        //根据条待卷的大小和配置信息计算offset
        ServerConfig config = ServerConfig.getConfig();
        long stripe_size = config.getStripe_size();
        long stripe_count = config.getStripe_count();

        long line_size = 0;
        long stripe_num = 0;
        long dest_offset = 0;

        line_size = stripe_size * stripe_count;
        stripe_num = offset / line_size;
        dest_offset = (stripe_num * stripe_size) + (offset % stripe_size);

        String hash_reslut = DispatchHandler.get_hash_with_blocknumber_and_challenge(filename_and_path, blocksize1, offset, challenge, blocknumber1, real_size1, dest_offset,p,y);
        System.out.println("the hash_result is " + hash_reslut);
        return hash_reslut;
    }


}
