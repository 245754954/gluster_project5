package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;
import cn.edu.nudt.hycloudserver.entity.VerifyTable;
import cn.edu.nudt.hycloudserver.service.serviceimpl.VerifyServiceImpl;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private VerifyServiceImpl verifyService;

    @RequestMapping(value = "/verifyBlock",method = {RequestMethod.GET,RequestMethod.POST})
    public Boolean verifyBlock(String filename, String blockIdx){
        Boolean rv = false;
        // query the status of blockIdx in filename
        Integer blockid = Integer.parseInt(blockIdx);
        VerifyTable verifyTable = verifyService.findVerifyTableByFilenameEqualsAndBlockidEquals(filename,blockid);
        System.out.println(verifyTable);
        if(null!=verifyTable){
            return true;
        }
        return false;
    }


    @RequestMapping(value = "/addBlock",method = {RequestMethod.GET,RequestMethod.POST})
    public void addBlock(String blockInfo){
        BlockInfo restoredBlockInfo = JSON.parseObject(blockInfo, BlockInfo.class);
        // store the following variables into database, the status column is set to true in default
        VerifyTable v = new VerifyTable();
        v.setFilename(restoredBlockInfo.getmFilename());
        v.setBlockid(restoredBlockInfo.getmBlockIdx());
        v.setHash(v.getHash());
        v.setStatus(true);

        System.out.println(restoredBlockInfo.getmFilename());
        System.out.println(restoredBlockInfo.getmBlockIdx());
        System.out.println(restoredBlockInfo.getmHash());

        //status
    }
}
