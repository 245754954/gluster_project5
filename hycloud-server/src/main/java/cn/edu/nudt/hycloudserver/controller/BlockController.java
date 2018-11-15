package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.*;
import cn.edu.nudt.hycloudinterface.utils.helper;
import cn.edu.nudt.hycloudserver.Dao.BlockCopyOneDao;
import cn.edu.nudt.hycloudserver.Dao.BlockCopyTwoDao;
import cn.edu.nudt.hycloudserver.Dao.BlockTableDao;
import cn.edu.nudt.hycloudserver.Dao.FileTableDao;
import cn.edu.nudt.hycloudserver.entity.BlockCopyOne;
import cn.edu.nudt.hycloudserver.entity.BlockCopyTwo;
import cn.edu.nudt.hycloudserver.entity.BlockTable;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private FileTableDao fileTableDao;

    @Autowired
    private BlockTableDao blockTableDao;

    @Autowired
    private BlockCopyOneDao blockCopyOneDao;

    @Autowired
    private BlockCopyTwoDao blockCopyTwoDao;

    @RequestMapping(value = "/updateBlockInfo", method = {RequestMethod.POST})
    public void addBlock(String filenameKey, String blockIdxKey, String copyNumKey, String hashKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);
        Integer copyNum = JSON.parseObject(copyNumKey, Integer.class);
//        BigInteger hash = JSON.parseObject(hashKey, BigInteger.class);

        blockTableDao.save(new BlockTable(filename, blockIdx, copyNum, hashKey, BlockStatus.INTACT));
        blockCopyOneDao.save(new BlockCopyOne(filename, blockIdx, hashKey, BlockStatus.INTACT));
        blockCopyTwoDao.save(new BlockCopyTwo(filename, blockIdx, hashKey, BlockStatus.INTACT));
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
        int cnt1 = blockCopyOneDao.deleteByFilename(filename);
        int cnt2 = blockCopyTwoDao.deleteByFilename(filename);
        if (cnt != cnt1 || cnt != cnt2){
            helper.err("Block copies is inconsistent");
        }
        return cnt;
    }

    // receive response from hadoop and update database
    @RequestMapping(value = "/submitBlockVerifyResult", method = {RequestMethod.POST})
    public void submitBlockVerifyResult(String copyIDKey, String filenameKey, String blockVerifyResultListKey){
        Integer copyID = JSON.parseObject(copyIDKey, Integer.class);
        String filename = JSON.parseObject(filenameKey, String.class);
        BlockVerifyResultList blockVerifyResultList = JSON.parseObject(blockVerifyResultListKey, BlockVerifyResultList.class);

        int blockNum = blockVerifyResultList.size();
        for (int index = 0; index < blockNum; index++){
            BlockVerifyResult blockVerifyResult = blockVerifyResultList.getBlockVerifyResult(index);
//            System.out.println(blockVerifyResult.getBlockIdx() + ",  " + blockVerifyResult.getStatus());
            switch (copyID){
                case CopyID.Origin:
                    blockTableDao.updateBlockStatus( blockVerifyResult.getStatus(), filename, blockVerifyResult.getBlockIdx());
                    if (blockVerifyResult.getStatus() != BlockStatus.INTACT){
                        fileTableDao.updateFileStatus(FileStatus.DAMAGED, filename);
                    }
                    break;
                case CopyID.CopyONE:
                    blockCopyOneDao.updateBlockStatus( blockVerifyResult.getStatus(), filename, blockVerifyResult.getBlockIdx());
                    if (blockVerifyResult.getStatus() != BlockStatus.INTACT){
                        BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockVerifyResult.getBlockIdx());
                        int temCopyNum = (blockTable.getCopyNum() > 0) ? (blockTable.getCopyNum() - 1) : 0;
                        blockTableDao.updateBlockCopyNum(temCopyNum, filename, blockVerifyResult.getBlockIdx());
                    }
                    break;
                case CopyID.CopyTWO:
                    blockCopyTwoDao.updateBlockStatus( blockVerifyResult.getStatus(), filename, blockVerifyResult.getBlockIdx());
                    if(blockVerifyResult.getStatus() != BlockStatus.INTACT){
                        BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockVerifyResult.getBlockIdx());
                        int temCopyNum = (blockTable.getCopyNum() > 0) ? (blockTable.getCopyNum() - 1) : 0;
                        blockTableDao.updateBlockCopyNum(temCopyNum, filename, blockVerifyResult.getBlockIdx());
                    }
                    break;
            }

        }
    }
}
