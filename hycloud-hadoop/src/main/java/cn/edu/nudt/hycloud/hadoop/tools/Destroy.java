package cn.edu.nudt.hycloud.hadoop.tools;
import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;


public class Destroy {

    public static final String blockPathMid = "_block_";

    public static void main(String[] args) throws Exception {
        ProgConfig.getConfig();
//        BasicConfigurator.configure();
        String order = args[0];
        int copyID = -1;
        String pathPrefix = null;
        if (order.equals("damageBlock")) {
            String str_copyID = args[1];
            switch (str_copyID.toUpperCase()) {
                case "ORIGIN":
                    copyID = 0;
                    pathPrefix = ProgConfig.getConfig().getBlockPathPrefix();
                    System.out.println("0");
                    break;
                case "COPYONE":
                    copyID = 1;
                    pathPrefix = ProgConfig.getConfig().getCopyOnePrefix();
                    System.out.println("1");
                    break;
                case "COPYTWO":
                    copyID = 2;
                    pathPrefix = ProgConfig.getConfig().getCopyTwoPrefix();
                    System.out.println("2");
                    break;
                default:
                    System.out.println("Wrong copyID!");
                    break;
            }
            String fileNameToDamage = args[2];
            String fileBlocknumToDamage = args[3];
            String damegeBlockPath = pathPrefix + fileNameToDamage + blockPathMid + fileBlocknumToDamage;

            String path = ProgConfig.getConfig().getSystemPath();
            long beginTime = System.currentTimeMillis();

            helper.print("URI.create(path): " + URI.create(path));
            helper.print("ProgConfig.getConfig().getHdfsConf(): " + ProgConfig.getConfig().getHdfsConf().toString());

            Configuration configuration = new Configuration();
            configuration.set("fs.default.name", "hdfs://192.168.6.181:9000");
//            configuration.set("fs.hdfs.impl",
//                    org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
//            );
//            configuration.set("fs.file.impl",
//                    org.apache.hadoop.fs.LocalFileSystem.class.getName()
//            );
            FileSystem fs = FileSystem.get(URI.create(path), configuration);

            Path file1 = new Path(ProgConfig.getConfig().getDamageFilePath());
//                      Path file2 =new Path("hdfs://master:9000/cpfile/2.txt_trash");
            Path file2 = new Path(damegeBlockPath);

            if(fs.exists(file2)){
                fs.delete(file2,true);
                System.out.println("Damage result: " + FileContext.getFileContext().util().copy(file1,file2));

                long endTime = System.currentTimeMillis();
                System.out.println("spend time:" + ((endTime - beginTime) ) + " ms");

                System.out.println("copy finished!");
            }
            else {
                System.out.println("no such file or block!");
            }

        } else {
            System.out.println("error order!");
        }
    }
}
