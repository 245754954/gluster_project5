package cn.edu.nudt.hycloud.hadoop.tools;

import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.Constants.BlockStatus;
import cn.edu.nudt.hycloudinterface.Constants.CopyID;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DestroyWithUpdate {
    public final String blockPathMid = "_block_";

    @Parameter(names= {"--config","-c"}, description = "path to config file")
    private String configPath=null;

    @Parameter(names= {"--help","-h"}, help=false, description = "help information")
    private boolean help = false;

    //    @Parameter(names= {"--action","-a"}, required = true, description = "action to perform")
    @Parameter(required = true,
            description = "which copy to destroy, " +
                    "including origin, copyone, copytwo")
    private String copy = null;

    @Parameter(names= {"--file","-f"}, description = "filename")
    private String filename = null;

    @Parameter(names= {"--block","-b"}, variableArity = true, description = "list of blocks to destroy")
    private List<String> strBlockIdxList = null;
//    @Parameter(names= {"--block","-b"}, description = "block to destroy")
//    private String strBlockIdx;


    public static void main(String[] argv) throws Exception {
        DestroyWithUpdate destroyWithUpdate = new DestroyWithUpdate();
        JCommander.Builder builder = JCommander.newBuilder();
        builder.addObject(destroyWithUpdate);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("hycloud-client");

        ProgConfig progConfig = ProgConfig.getConfig(destroyWithUpdate.configPath);
//        progConfig.dump();

        destroyWithUpdate.run(jcmd);
    }

    private void run(JCommander jcmd) throws IOException {
        if (help){
            jcmd.usage();
            System.exit(0);
        }


//        BasicConfigurator.configure();   damageBlock origin/copyone/copytwo filename blocknum
        int copyID = -1;
        String pathPrefix = null;

            switch (this.copy.toUpperCase()) {
                case "ORIGIN":
                    copyID = 0;
                    pathPrefix = ProgConfig.getConfig().getBlockPathPrefix();
                    break;
                case "COPYONE":
                    copyID = 1;
                    pathPrefix = ProgConfig.getConfig().getCopyOnePrefix();
                    break;
                case "COPYTWO":
                    copyID = 2;
                    pathPrefix = ProgConfig.getConfig().getCopyTwoPrefix();
                    break;
                default:
                    System.err.println("Wrong copyID!");
                    System.exit(-1);
                    break;
            }

            long beginTime = System.currentTimeMillis();
            FileSystem fs = FileSystem.get(URI.create(ProgConfig.getConfig().getSystemPath()),
                    ProgConfig.getConfig().getHdfsConf());
            Path srcBlock = new Path(ProgConfig.getConfig().getDamageFilePath());
            String dstBlockPrefix = pathPrefix + this.filename + blockPathMid;
            for (String blockIdx: strBlockIdxList){
                Path dstBlock = new Path(dstBlockPrefix + blockIdx);

                if(fs.exists(dstBlock)){
                    fs.delete(dstBlock,true);
                    boolean result = FileContext.getFileContext().util().copy(srcBlock,dstBlock);
//                    System.out.println("Damage result: " + result);
                    if (result) {
                        DestoryTransfer.updateBlockInfo(new CopyID(copyID),
                                this.filename,
                                Integer.parseInt(blockIdx),
                                new BlockStatus(BlockStatus.DAMAGED));
                    }
                    long endTime = System.currentTimeMillis();
                    System.out.println("Destroyed block " + blockIdx + " of " + filename + " (" + (endTime - beginTime) + " ms)");
//                    System.out.println("spend time:" + ((endTime - beginTime) ) + " ms");
                }
                else {
                    System.out.println("no such file or block!");
                }
            }
    }
}
