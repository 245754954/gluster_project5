package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResult;
import cn.edu.nudt.hycloudinterface.entity.Challenge;
import cn.edu.nudt.hycloudinterface.entity.FileStatus;
import cn.edu.nudt.hycloudserver.Dao.FileTableDao;
import cn.edu.nudt.hycloudserver.entity.FileTable;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("/hdfs")
public class HDFSController {

    @Autowired
    private FileTableDao fileTableDao;

    @RequestMapping(value = "/fetchChallenge", method = {RequestMethod.POST})
    public Challenge fetchChallenge(String fetchChallenge){

        long fileNum = fileTableDao.count();
        SecureRandom srand = new SecureRandom();
        long fid = srand.nextLong() % fileNum;

        List<FileTable> fileList = fileTableDao.findAll();
        FileTable fileTable = fileList.get((int) fid);
        Challenge challenge = new Challenge(fileTable.getFilename(), fileTable.getBlockNum());
        return  challenge;
    }

}