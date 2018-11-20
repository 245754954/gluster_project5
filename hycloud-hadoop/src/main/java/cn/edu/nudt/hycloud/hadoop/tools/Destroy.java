package cn.edu.nudt.hycloud.hadoop.tools;
import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.net.URI;

import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;



public class Destroy {

    public static final String blockPathMid = "_block_";

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://192.168.6.129:9000");
        String order = args[0];
        if (order.equals("damageBlock")) {
            String fileNameToDamage = args[1];
            String fileBlocknumToDamage = args[2];
            String damegeBlockPath = ProgConfig.getConfig().getBlockPathPrefix() + fileNameToDamage + blockPathMid + fileBlocknumToDamage;
            FileSystem fs = null;
            String path = "hdfs://master:9000/";
            long beginTime = System.currentTimeMillis();
            fs = FileSystem.get(URI.create(path), conf);

            Path file1 = new Path(ProgConfig.getConfig().getDamageFilePath());
//                      Path file2 =new Path("hdfs://master:9000/cpfile/2.txt_trash");
            Path file2 = new Path(damegeBlockPath);
            long endTime = System.currentTimeMillis();
            System.out.println("spend time:" + ((endTime - beginTime) ) + " ms");

            beginTime = System.currentTimeMillis();

            if(fs.exists(file2)){
                fs.delete(file2,true);
            }
            System.out.println(FileContext.getFileContext().util().copy(file1,file2));

            endTime = System.currentTimeMillis();
            System.out.println("spend time:" + ((endTime - beginTime) ) + " ms");

            System.out.println("copy finished!");
        } else {
            System.out.println("error order!");
        }
    }
}
