package cn.edu.nudt.hycloud.hadoop.entry;

import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import cn.edu.nudt.hycloud.hadoop.Verify.VerifyHandler;
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
        ProgConfig.getConfig(configfile);
        // TO DO
//        while (true) {
//            VerifyHandler.startVerify(VerifyHandler.INPUT_PATH);
//        }
        while (true){
            VerifyHandler verifyHandler = new VerifyHandler();

            long beginTime = System.currentTimeMillis();
            verifyHandler.startVerify();
            long endTime = System.currentTimeMillis();
            System.out.println("spend time:" + ((endTime - beginTime) ) + "s");

            Thread.sleep(ProgConfig.getConfig().getSleepTime());
        }

    }
}
