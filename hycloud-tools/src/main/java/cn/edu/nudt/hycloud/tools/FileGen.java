package cn.edu.nudt.hycloud.tools;

import cn.edu.nudt.hycloudinterface.utils.helper;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGen {

    @Parameter(names= {"--filename","-f"}, required = true, description = "filename")
    private String filename;

    @Parameter(names= {"--size","-s"}, required = true, description = "size of file, e.g., 2K, 3M, 4G")
    private String size;

    public static void main(String ... argv) throws IOException {

        FileGen fileGen = new FileGen();
        JCommander.Builder builder = JCommander.newBuilder();
        builder.addObject(fileGen);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("FileGen");

        long tstart = System.currentTimeMillis();

        fileGen.run();

        long tend = System.currentTimeMillis();
        helper.timing("FileGen", tstart, tend);
    }

    public void run() throws IOException {
        CreateFile(this.filename, this.size);
    }

    //生成文件
    public void CreateFile(String filename, String size) throws IOException {
        Random rand = new Random();
        int p,n;
        FileWriter fileWriter = new FileWriter(new File(filename));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        if (size.contains("K") || size.contains("k")){
            n = Integer.parseInt(size.substring(0,size.length()-2));
            p = 1;
        }
        else if (size.contains("M") || size.contains("m")){
            n = Integer.parseInt(size.substring(0,size.length()-2));
            p = 2;
        }
        else if (size.contains("G") || size.contains("g")){
            n = Integer.parseInt(size.substring(0,size.length()-2));
            p = 3;
        }
        else{
            n = Integer.parseInt(size.substring(0,size.length()-1));
            p = 0;
        }
        int nexth = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < Math.pow(1024,p); j++) {
                if (nexth == 63) {
                    bufferedWriter.write('\n');
                    nexth = 0;
                }
                else {
                    bufferedWriter.write(charlist[rand.nextInt(36)]);
                }
                nexth++;
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private  static char charlist[] = {'a','b','c','d','e','f','g','h','i',
            'j','k','l','m','n','o','p','q','r',
            's','t','u','v','w','x','y','z','0',
            '1','2','3','4','5','6','7','8','9'};
}
