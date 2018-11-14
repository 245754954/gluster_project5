package cn.edu.nudt.hadoop.Entry.entry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import cn.edu.nudt.hadoop.Entry.Verify.VerifyHandler;
public class Starter {
    private String strAction = null;


    @Parameter(names= {"--help","-h"}, description = "help")
    private boolean help;


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
        // TO DO
        VerifyHandler.startVerify(VerifyHandler.INPUT_PATH);
    }
}
