package cn.edu.nudt.hycloudserver.hadoop;

import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class HadoopFileSystem {
    private static HadoopFileSystem hadoopFileSystem;

    private FileSystem fileSystem;


    public static HadoopFileSystem getHadoopFileSystem() throws IOException {
        if(hadoopFileSystem == null) {
            hadoopFileSystem = new HadoopFileSystem();
        }
        return hadoopFileSystem;
    }

    private HadoopFileSystem() throws IOException {
        fileSystem = FileSystem.get(URI.create(ServerConfig.getConfig().getHdfsYhbdHome()), ServerConfig.getConfig().getHdfsConf());
        fileSystem.exists(new Path(ServerConfig.getConfig().getHdfsYhbdHome()));
    }


    public FileSystem getFileSystem() {
        return fileSystem;
    }


//    public static void main(String ... argv) throws IOException {
//        long tstart, tend;
//        ServerConfig serverConfig = ServerConfig.getConfig();
//        serverConfig.dump();
//
//        helper.print("System Type: " + System.getProperty("os.name").toLowerCase());
//        if(System.getProperty("os.name").toLowerCase().startsWith("win")){
//            System.setProperty("hadoop.home.dir", serverConfig.getHadoopHomeDir());
//        }
//
//        tstart = System.currentTimeMillis();
//        Configuration configuration = new Configuration();
//        configuration.set("fs.defaultFS", "hdfs://192.168.6.129:9000");
//        FileSystem fs = FileSystem.get(configuration);
//        tend = System.currentTimeMillis();
//        helper.timing("FileSystem init", tstart, tend);
//
//        //////////////////////////////////////////////////////////////////////////////////////
//        int blockIdx = 0;
//        String srcBlock = ServerConfig.getConfig().getHdfsVerifyCopyOneHome() + "test128.txt_block_" + blockIdx;
//        String dstBlock = ServerConfig.getConfig().getHdfsVerifyHome() + "test128.txt_block_" + blockIdx;
//        Path srcBlockPath = new Path(srcBlock);
//        Path dstBlockPath = new Path(dstBlock);
//
//        tstart = System.currentTimeMillis();
//        if(fs.exists(dstBlockPath)){
//            fs.delete(dstBlockPath,true);
//        }
////        boolean rv = FileContext.getFileContext().util().copy(srcBlockPath, dstBlockPath);
//        boolean rv = FileUtil.copy(fs, srcBlockPath, fs, dstBlockPath, false, configuration);
//        tend = System.currentTimeMillis();
//        helper.timing("copy block_" + blockIdx, tstart, tend);
//
//        //////////////////////////////////////////////////////////////////////////////////////
//        blockIdx = 1;
//        srcBlock = ServerConfig.getConfig().getHdfsVerifyCopyOneHome() + "test128.txt_block_" + blockIdx;
//        dstBlock = ServerConfig.getConfig().getHdfsVerifyHome() + "test128.txt_block_" + blockIdx;
//        srcBlockPath = new Path(srcBlock);
//        dstBlockPath = new Path(dstBlock);
//
//        tstart = System.currentTimeMillis();
//        if(fs.exists(dstBlockPath)){
//            fs.delete(dstBlockPath,true);
//        }
////        boolean rv = FileContext.getFileContext().util().copy(srcBlockPath, dstBlockPath);
//        rv = FileUtil.copy(fs, srcBlockPath, fs, dstBlockPath, false, configuration);
//        tend = System.currentTimeMillis();
//        helper.timing("copy block_" + blockIdx, tstart, tend);
//
//        //////////////////////////////////////////////////////////////////////////////////////
//        blockIdx = 2;
//        srcBlock = ServerConfig.getConfig().getHdfsVerifyCopyOneHome() + "test128.txt_block_" + blockIdx;
//        dstBlock = ServerConfig.getConfig().getHdfsVerifyHome() + "test128.txt_block_" + blockIdx;
//        srcBlockPath = new Path(srcBlock);
//        dstBlockPath = new Path(dstBlock);
//
//        tstart = System.currentTimeMillis();
//        if(fs.exists(dstBlockPath)){
//            fs.delete(dstBlockPath,true);
//        }
////        boolean rv = FileContext.getFileContext().util().copy(srcBlockPath, dstBlockPath);
//        rv = FileUtil.copy(fs, srcBlockPath, fs, dstBlockPath, false, configuration);
//        tend = System.currentTimeMillis();
//        helper.timing("copy block_" + blockIdx, tstart, tend);
//    }
}
