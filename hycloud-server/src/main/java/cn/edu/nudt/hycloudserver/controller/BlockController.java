package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.BlockStatus;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResult;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
import cn.edu.nudt.hycloudserver.Dao.BlockTableDao;
import cn.edu.nudt.hycloudserver.entity.BlockTable;
import cn.edu.nudt.hycloudserver.entity.FileTable;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private BlockTableDao blockTableDao;

    @RequestMapping(value = "/addBlock", method = {RequestMethod.POST})
    public void addBlock(String filenameKey, String blockIdxKey, String hashKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);
//        BigInteger hash = JSON.parseObject(hashKey, BigInteger.class);

        blockTableDao.save(new BlockTable(filename, blockIdx, hashKey, BlockStatus.INTACT));
    }

    @RequestMapping(value = "/verifyBlock", method = {RequestMethod.POST})
    public int verifyBlock(String filenameKey, String blockIdxKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);

        BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockIdx);
        int rv = BlockStatus.NOFOUND;
        if(blockTable != null){
                rv = blockTable.getStatus();
        }
        return rv;
    }

    @RequestMapping(value = "/deleteFileBlocks", method = {RequestMethod.POST})
    public int deleteFileBlocks(String filenameKey){
        String filename = JSON.parseObject(filenameKey, String.class);

        int cnt = blockTableDao.deleteByFilename(filename);
        return cnt;
    }

    // receive response from hadoop and update database
    @RequestMapping(value = "/submitBlockVerifyResult", method = {RequestMethod.POST})
    public void submitBlockVerifyResult(String filenameKey, String blockVerifyResultListKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        BlockVerifyResultList blockVerifyResultList = JSON.parseObject(blockVerifyResultListKey, BlockVerifyResultList.class);

//        blockTableDao.findByFilename(filename);
        int blockNum = blockVerifyResultList.size();
        for (int index = 0; index < blockNum; index++){
            BlockVerifyResult blockVerifyResult = blockVerifyResultList.getBlockVerifyResult(index);
            System.out.println(blockVerifyResult.getBlockIdx() + ",  " + blockVerifyResult.getStatus());

            blockTableDao.updateBlockInfo( blockVerifyResult.getStatus(),filename,blockVerifyResult.getBlockIdx());
        }
    }
}
