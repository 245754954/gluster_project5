package cn.edu.nudt.hycloudhdfs.controller;


import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hdfs")
public class HdfsController {

    @Autowired
    FsShell fsShell;

    public HdfsController() {
        System.out.println("sdtfgyuh");
    }

    //列出hadoop下面的所有文件，也就是进行遍历操作
    @RequestMapping(value = "/find")
    public String getFiles(){

        for (FileStatus fileStatus : fsShell.lsr("/")) {
            System.out.println(">" + fileStatus.getPath());
        }




        return "success";

    }

}
