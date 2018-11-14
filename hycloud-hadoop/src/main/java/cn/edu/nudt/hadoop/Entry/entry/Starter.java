package cn.edu.nudt.hadoop.Entry.entry;

import cn.edu.nudt.hadoop.Entry.config.ProgConfig;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import cn.edu.nudt.hadoop.Entry.Verify.VerifyHandler;
public class Starter {
    private String strAction = null;


    @Parameter(names= {"--help","-h"}, description = "help")
    private boolean help;

    @Parameter(names= {"--config","-c"}, description = "config file")
    private String configfile = null;


    public static void main(String ... argv) throws Exception{

        Starter starter = new Starter();
        JCommander.Builder builder = JCommander.newBuilder();
        builder.addObject(starter);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("Hadoop-side");

        starter.run();
    }

    public void run() throws Exception{
//        ProgConfig.getConfig(configfile);
        // TO DO
//        while (true) {
//            VerifyHandler.startVerify(VerifyHandler.INPUT_PATH);
//        }
        VerifyHandler verifyHandler = new VerifyHandler();

        long beginTime = System.currentTimeMillis();
        verifyHandler.startVerify(VerifyHandler.INPUT_PATH);
        long endTime = System.currentTimeMillis();
        System.out.println("spend time:" + ((endTime - beginTime) ) + "s");
    }
}
