package cn.edu.nudt.hadoop;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Starter {


    @Parameter(names= {"--filename","-f"}, required = true, description = "filename")
    private String filename;

    @Parameter(names= {"--size","-s"}, required = true, description = "size of file")
    private int size;

    public static void main(String ... argv){

        Starter starter = new Starter();
        JCommander.Builder builder = JCommander.newBuilder();
        builder.addObject(starter);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("Hadoop-side");

        starter.run();
    }

    public void run(){
        // TO DO

    }
}
