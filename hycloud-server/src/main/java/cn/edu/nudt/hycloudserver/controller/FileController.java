package cn.edu.nudt.hycloudserver.controller;


import cn.edu.nudt.hycloudinterface.entity.FileStatus;
import cn.edu.nudt.hycloudserver.Dao.FileTableDao;
import cn.edu.nudt.hycloudserver.entity.FileTable;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileTableDao fileTableDao;

    @RequestMapping(value = "/addFile", method = {RequestMethod.POST})
    public void addFile(String filenameKey, String blockNumKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        Long blockNum = JSON.parseObject(blockNumKey, Long.class);

        fileTableDao.save(new FileTable(filename, blockNum, FileStatus.INTACT));
    }

    @RequestMapping(value = "/verifyFile", method = {RequestMethod.POST})
    public int verifyFile(String filenameKey){
        String filename = JSON.parseObject(filenameKey, String.class);

        FileTable fileTable = fileTableDao.findByFilename(filename);
        int rv = FileStatus.NOFOUND;
        if(fileTable != null){
            rv = fileTable.getStatus();
        }
        return rv;
    }

    @RequestMapping(value = "/deleteFile", method = {RequestMethod.POST})
    public int deleteFile(String filenameKey){
        String filename = JSON.parseObject(filenameKey, String.class);
        return fileTableDao.deleteByFilename(filename);
    }

}