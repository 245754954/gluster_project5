package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.Challenge;
import cn.edu.nudt.hycloudinterface.utils.helper;
import cn.edu.nudt.hycloudserver.Dao.FileTableDao;
import cn.edu.nudt.hycloudserver.entity.FileTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/hdfs")
public class HDFSController {

    @Autowired
    private FileTableDao fileTableDao;

    @RequestMapping(value = "/fetchChallenge", method = {RequestMethod.POST})
    public Challenge fetchChallenge(String fetchChallenge){
        Challenge challenge = null;

        long fileNum = fileTableDao.count();
        if (fileNum != 0) {


            long fid = ThreadLocalRandom.current().nextLong(fileNum);

            List<FileTable> fileList = fileTableDao.findAll();
            helper.print("fileNum: " + fileNum + ", fid: " + fid + ", fileList.size() = " + fileList.size());

            FileTable fileTable = fileList.get((int) fid);
            challenge = new Challenge(fileTable.getFilename(), fileTable.getBlockNum());
            helper.print("Challenge: filename = " + challenge.getFilename() + ", blockNum = " + challenge.getBlockNum());
        }
        return  challenge;
    }

}