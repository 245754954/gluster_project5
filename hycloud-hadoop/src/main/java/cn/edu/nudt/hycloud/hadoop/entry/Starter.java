package cn.edu.nudt.hycloud.hadoop.entry;

import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import cn.edu.nudt.hycloud.hadoop.Verify.VerifyHandler;
public class Starter {
    private String strAction = null;

    @Parameter(names= {"--help","-h"}, description = "help")
    private boolean help = false;

    @Parameter(names= {"--verbose","-v"}, description = "show more information")
    private boolean verbose = false;

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
        ProgConfig.getConfig(configfile, verbose);

        while(true){
            VerifyHandler verifyHandler = new VerifyHandler();

            long tstart = System.currentTimeMillis();

            verifyHandler.startVerify();

            long tend = System.currentTimeMillis();
            helper.timing(ProgConfig.getConfig().isVerbose(), "spend time:", tstart, tend);


            Thread.sleep(ProgConfig.getConfig().getSleepTime());
        }

    }
}
