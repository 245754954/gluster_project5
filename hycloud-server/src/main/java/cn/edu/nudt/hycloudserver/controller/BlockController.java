package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.Constants.*;
import cn.edu.nudt.hycloudinterface.entity.BlockList;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
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
import com.alibaba.fastjson.JSON;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/block")
public class BlockController {

    @RequestMapping(value="/verify1",method = {RequestMethod.GET,RequestMethod.POST})
    public void verify1(@Param("ups") List<UploadInfo> ups){
        for(UploadInfo up:ups){

            System.out.println(up.toString());
        }

    }





    @RequestMapping(value = "/verifyBlock", method = {RequestMethod.POST})
    public String verifyBlock(String filename, String filename_and_path,String challenge,String blocknumber,String blocksize,String real_size) throws IOException {

        Integer blocknumber1 = Integer.parseInt(blocknumber);
        Integer blocksize1 = Integer.parseInt(blocksize);
        Integer real_size1 = Integer.parseInt(real_size);
        Integer offset = blocknumber1*blocksize1;

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

        String hash_reslut = DispatchHandler.get_hash_with_blocknumber_and_challenge(filename_and_path,blocksize1,offset,challenge,blocknumber1,real_size1,dest_offset);
        System.out.println("the hash_result is "+hash_reslut);
        return hash_reslut;
    }


}
