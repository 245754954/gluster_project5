package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.Constants.*;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
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
import com.alibaba.fastjson.JSON;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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

    @RequestMapping(value = "/recoverableBlock", method = {RequestMethod.POST})
    public boolean recoverableBlock(String filenameKey, String blockIdxKey) throws IOException {
        boolean rv = false;

        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);

        ///////////////////////////////////////////////////////////////////////////////////////
        // Scheme 2: query database
        BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockIdx);
        if (blockTable != null && blockTable.getCopyNum() > 0){
            rv = true;
        }

        ///////////////////////////////////////////////////////////////////////////////////////
        // Scheme 1: query the platform directly, which may not response in time.
        return rv;
    }

    @RequestMapping(value = "/restoreBlock", method = {RequestMethod.POST})
    public int restoreBlock(String filenameKey, String blockIdxKey) throws IOException {
        int rv;

        String filename = JSON.parseObject(filenameKey, String.class);
        Integer blockIdx = JSON.parseObject(blockIdxKey, Integer.class);

        BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockIdx);
        if (blockTable == null){
            rv = RestoreResult.NOTFOUND;
        }else if(blockTable.getStatus() == BlockStatus.INTACT){
            rv = RestoreResult.SUCCESS;
        }else{
            // to recover in hdfs
            int copyID = -1;
            BlockCopyOne blockCopyOne = blockCopyOneDao.findByFilenameAndBlockIdx(filename, blockIdx);
            if (blockCopyOne != null && blockCopyOne.getStatus() == BlockStatus.INTACT){
                copyID = CopyID.CopyONE;
            }else{
                BlockCopyTwo blockCopyTwo = blockCopyTwoDao.findByFilenameAndBlockIdx(filename, blockIdx);
                if (blockCopyTwo != null && blockCopyTwo.getStatus() == BlockStatus.INTACT){
                    copyID = CopyID.CopyTWO;
                }
            }

            if (copyID == CopyID.CopyONE || copyID == CopyID.CopyTWO){
                String srcBlock = getBlockPath(filename, blockIdx, copyID);
                String dstBlock = getBlockPath(filename, blockIdx);
                boolean res = copyBlock(srcBlock, dstBlock);

                if(res){
                    blockTable.setStatus(BlockStatus.INTACT);
                    blockTableDao.save(blockTable);

                    // set file to intact if its blocks are all intact
                    List<BlockTable> blockTableList = blockTableDao.findByFilenameAndStatus(filename, BlockStatus.DAMAGED);
                    if (blockTableList == null){
                        FileTable fileTable = fileTableDao.findByFilename(filename);
                        fileTable.setStatus(FileStatus.INTACT);
                        fileTableDao.save(fileTable);
                    }
                    rv = RestoreResult.SUCCESS;
                }else{
                    rv = RestoreResult.FAILED;
                }
            }else{
                rv = RestoreResult.FAILED;
            }

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
            BlockTable blockTable = blockTableDao.findByFilenameAndBlockIdx(filename, blockVerifyResult.getBlockIdx());
            switch (copyID){
                case CopyID.Origin:
                    blockTable.setStatus(blockVerifyResult.getStatus());
                    blockTableDao.save(blockTable);
                    if (blockVerifyResult.getStatus() != BlockStatus.INTACT){
                        FileTable fileTable = fileTableDao.findByFilename(filename);
                        fileTable.setStatus(FileStatus.DAMAGED);
                        fileTableDao.save(fileTable);
                    }
                    break;
                case CopyID.CopyONE:
                    BlockCopyOne blockCopyOne = blockCopyOneDao.findByFilenameAndBlockIdx(filename, blockVerifyResult.getBlockIdx());
                    blockCopyOne.setStatus(blockVerifyResult.getStatus());
                    blockCopyOneDao.save(blockCopyOne);
                    if (blockVerifyResult.getStatus() != BlockStatus.INTACT){
                        blockTable.setCopyNum( (blockTable.getCopyNum() > 0) ? (blockTable.getCopyNum() - 1) : 0);
                        blockTableDao.save(blockTable);
                    }
                    break;
                case CopyID.CopyTWO:
                    BlockCopyTwo blockCopyTwo = blockCopyTwoDao.findByFilenameAndBlockIdx(filename, blockVerifyResult.getBlockIdx());
                    blockCopyTwo.setStatus(blockVerifyResult.getStatus());
                    blockCopyTwoDao.save(blockCopyTwo);
                    if(blockVerifyResult.getStatus() != BlockStatus.INTACT){
                        blockTable.setCopyNum( (blockTable.getCopyNum() > 0) ? (blockTable.getCopyNum() - 1) : 0);
                        blockTableDao.save(blockTable);
                    }
                    break;
            }
        }
    }

    public String getBlockPath(String filename, int blockIdx) throws IOException {
        return ServerConfig.getConfig().getHdfsVerifyHome() + filename + "_block_" + blockIdx;
    }

    public String getBlockPath(String filename, int blockIdx, int copyID) throws IOException {
        if(!CopyID.isValid(copyID)){
            throw new IOException("getBlockPath: copyID Error");
        }
        ServerConfig serverConfig = ServerConfig.getConfig();
        String pathPrefix = serverConfig.getHdfsVerifyHome();
        if(copyID == CopyID.CopyONE){
            pathPrefix = serverConfig.getHdfsVerifyCopyOneHome();
        }else if (copyID == CopyID.CopyTWO){
            pathPrefix = serverConfig.getHdfsVerifyCopyTwoHome();
        }

        return pathPrefix + filename + "_block_" + blockIdx;
    }

    /**
     *
     * @param srcBlock
     * - complete path of the block in HDFS, e.g., "hdfs://master:9000/cptest/srcBlock"
     * @param dstBlock
     * - complete path of the block in HDFS, e.g., "hdfs://master:9000/cptest/dstBlock"
     * @return
     * @throws IOException
     */
    public boolean copyBlock(String srcBlock, String dstBlock) throws IOException {
//        FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.6.129:9000/cpfile/"), ServerConfig.getConfig().getHdfsConf());

        FileSystem fs = FileSystem.get(URI.create(ServerConfig.getConfig().getHdfsYhbdHome()), ServerConfig.getConfig().getHdfsConf());
//        helper.print("getHdfsYhbdHome: " + ServerConfig.getConfig().getHdfsYhbdHome());
//        helper.print("srcBlock: " + srcBlock);
//        helper.print("dstBlock: " + dstBlock);

        Path srcBlockPath = new Path(srcBlock);
        Path dstBlockPath = new Path(dstBlock);

        if(fs.exists(dstBlockPath)){
            fs.delete(dstBlockPath,true);
        }
        return FileContext.getFileContext().util().copy(srcBlockPath, dstBlockPath);
    }
}
