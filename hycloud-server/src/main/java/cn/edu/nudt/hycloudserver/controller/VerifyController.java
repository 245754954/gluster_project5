package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;
import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping("/versify")
public class VerifyController {

    @RequestMapping(value = "/verifyBlock")
    public Boolean verifyBlock(String filename, String blockIdx){
        Boolean rv = false;
        // query the status of blockIdx in filename

        return rv;
    }


    public void addBlock(String blockInfo){
        BlockInfo restoredBlockInfo = JSON.parseObject(blockInfo, BlockInfo.class);
        // store the following variables into database, the status column is set to true in default
        restoredBlockInfo.getmFilename();
        restoredBlockInfo.getmBlockIdx();
        restoredBlockInfo.getmHash();
        //status
    }
}
