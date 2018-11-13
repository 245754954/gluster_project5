package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;
import cn.edu.nudt.hycloudinterface.entity.FileInfo;
import cn.edu.nudt.hycloudserver.entity.BlockInfoServer;
import cn.edu.nudt.hycloudserver.entity.VerifyTable;
import cn.edu.nudt.hycloudserver.service.serviceimpl.VerifyServiceImpl;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private VerifyServiceImpl verifyService;

    @RequestMapping(value = "/verifyBlock",method = {RequestMethod.GET,RequestMethod.POST})
    public Boolean verifyBlock(String filename, String blockIdx){
        Boolean rv = false;
        // query the status of blockIdx in filename
        Integer blockid = JSON.parseObject(blockIdx,Integer.class);
        String fname = JSON.parseObject(filename,String.class);
        VerifyTable v = verifyService.findVerifyTableByMFilename(filename);
        List<BlockInfoServer> nodes = v.getmBlockList();
        Boolean flag = false;
        for(BlockInfoServer b:nodes)
        {
            if(b.getmBlockIdx()==blockid)
            {
                flag =  b.getmStatus();
                break;
            }
        }

       return flag;
    }


    @RequestMapping(value = "/updateFileInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public void updateFileInfo(String fileInfo){
        FileInfo fileInfoRestore = JSON.parseObject(fileInfo, FileInfo.class);
        // store the following variables into database, the status column is set to true in default
        VerifyTable v = new VerifyTable();
        v.setmFilename(fileInfoRestore.getmFilename());

        List<BlockInfoServer> blockServer = new ArrayList<BlockInfoServer>();
        List<BlockInfo> blocks = fileInfoRestore.getmBlockList();
        for(BlockInfo b:blocks)
        {
            BlockInfoServer b1 = new BlockInfoServer();
            b1.setmBlockIdx(b.getmBlockIdx());
            b1.setmHash(b.getmHash());
            b1.setmStatus(b.getmStatus());
            blockServer.add(b1);
        }
        v.setmBlockList(blockServer);

        verifyService.saveBlockInfo(v);

        //status
    }
}
