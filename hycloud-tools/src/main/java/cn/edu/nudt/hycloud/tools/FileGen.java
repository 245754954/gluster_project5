package cn.edu.nudt.hycloud.tools;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class FileGen {

    @Parameter(names= {"--filename","-f"}, required = true, description = "filename")
    private String filename;

    @Parameter(names= {"--size","-s"}, required = true, description = "size of file")
    private int size;

    public static void main(String ... argv){

        FileGen fileGen = new FileGen();
        JCommander.Builder builder = JCommander.newBuilder();
        builder.addObject(fileGen);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("FileGen");

        fileGen.run();
    }

    public void run(){

    }
}
