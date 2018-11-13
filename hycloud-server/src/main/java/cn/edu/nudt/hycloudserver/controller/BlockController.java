package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.BlockStatus;
import cn.edu.nudt.hycloudserver.Dao.BlockTableDao;
import cn.edu.nudt.hycloudserver.entity.BlockTable;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private BlockTableDao blockTableDao;

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public void addBlock(String filenameKey, String blockIdxKey, String hashKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);
//        BigInteger hash = JSON.parseObject(hashKey, BigInteger.class);

        blockTableDao.save(new BlockTable(filename, blockIdx, hashKey, true));
    }

    @RequestMapping(value = "/verify", method = {RequestMethod.POST})
    public int verifyBlock(String filenameKey, String blockIdxKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);

        BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockIdx);
        int rv = BlockStatus.NOFOUND;
        if(blockTable != null){
            if(blockTable.getStatus() == false){
                rv = BlockStatus.DAMAGED;
            }else{
                rv = BlockStatus.INTACT;
            }
        }
        return rv;
    }
}
