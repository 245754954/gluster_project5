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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DestroyRatio {
    public final String blockPathMid = "_block_";

    @Parameter(names= {"--config","-c"}, description = "path to config file")
    private String configPath=null;

    @Parameter(names= {"--help","-h"}, help=false, description = "help information")
    private boolean help = false;

    @Parameter(names= {"--disable.update"}, description = "whether to disable update")
    private boolean disableUpdate = false;

    //    @Parameter(names= {"--action","-a"}, required = true, description = "action to perform")
    @Parameter(required = true,
            description = "which copy to destroy, " +
                    "including origin, copyone, copytwo")
    private String copy = null;

    @Parameter(names= {"--file","-f"}, description = "filename")
    private String filename = null;

    @Parameter(names= {"--ratio","-r"}, help=false, description = "number of blocks to destroy out of every 100 blocks")
    private int ratio = 0;

    @Parameter(names= {"--num","-n"}, help=false, description = "number of blocks of the file")
    private int num = 0;


    public static void main(String[] argv) throws Exception {
        DestroyRatio destroy = new DestroyRatio();
        JCommander.Builder builder = JCommander.newBuilder();
        builder.addObject(destroy);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("hycloud-client");

        ProgConfig progConfig = ProgConfig.getConfig(destroy.configPath);
//        progConfig.dump();

        destroy.run(jcmd);
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

        /** randomly select required blocks to damage */
        int subNum = num * ratio / 100;
        List<Integer> blockIdxList = new ArrayList<>();
        while (blockIdxList.size() < subNum){
            int idx = ThreadLocalRandom.current().nextInt(num);
            if (!blockIdxList.contains(idx)){
                blockIdxList.add(idx);
            }
        }
        /** end randomly select */


        for (Integer blockIdx: blockIdxList){
            Path dstBlock = new Path(dstBlockPrefix + blockIdx);

            if(fs.exists(dstBlock)){
                fs.delete(dstBlock,true);
                boolean result = FileContext.getFileContext().util().copy(srcBlock,dstBlock);
//                    System.out.println("Damage result: " + result);
                if (result && !disableUpdate) {
                    DestoryTransfer.updateBlockInfo(new CopyID(copyID),
                            this.filename,
                            blockIdx,
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
