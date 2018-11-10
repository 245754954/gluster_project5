package cn.edu.nudt.hycloudclient.Entry;

import cn.edu.nudt.hycloudclient.Storage.StorageHandler;
import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.deletion.DeletionHandler;
import cn.edu.nudt.hycloudinterface.entity.utils.helper;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.JCommander.Builder;
import com.beust.jcommander.Parameter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.List;

public class Client {
    @Parameter(names= {"--help","-h"}, help=false, description = "action to perform")
    private boolean help = false;

    @Parameter(names= {"--action","-a"}, required = true, description = "action to perform")
    private String strAction = null;

    //////////////////////////////////////////////
    // parameters for Assured Deletion
    @Parameter(names= {"--file","-f"}, description = "path or name of the file")
    private String sourcefile = null;
    @Parameter(names= {"--localpath","-l"}, description = "path to to store the file")
    private String localpath = null;
    @Parameter(names= {"--granularity","-g"}, description = "the size in KB of every segment")
    private int granularity = 64;
    @Parameter(names= {"--delete", "-d"}, variableArity = true, description = "list of segments to be deleted")
    private List<String> deletes = null;
    //////////////////////////////////////////////


    public static void main(String ... argv) throws IOException, IllegalBlockSizeException, BadPaddingException {
//        System.out.println(System.getProperty("user.dir"));

        Client client = new Client();
        Builder builder = JCommander.newBuilder();
        builder.addObject(client);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("hycloud-client");

        Config conf = Config.getConfig();
        conf.dump();

        client.run(jcmd);
    }

    public void run(JCommander jcmd) throws IOException, IllegalBlockSizeException, BadPaddingException {
        if(help) {
            jcmd.usage();
            System.exit(0);
        }
//		dumpArgs();

        // check args inside your action
        switch(Action.get(strAction)) {
            case Action.PUT:
                // check args inside your action
                StorageHandler.put(sourcefile);
                break;
            case Action.GET:
                // check args inside your action
                StorageHandler.get(sourcefile, localpath);
                break;
            case Action.VERIFY:
                // check args inside your action
                break;
            case Action.SPUT:
                DeletionHandler.sput(sourcefile, granularity);
                break;
            case Action.SGET:
                DeletionHandler.sget(sourcefile, localpath);
                break;
            case Action.SDEL:
                DeletionHandler.sdel(sourcefile, deletes);
                break;
            case Action.SDUMP:
                DeletionHandler.sdump(sourcefile);
                break;
            default:
                helper.err("Error: wrong action");
                break;
        }
    }

    public void dumpArgs() {
        helper.print("help: " + help);
        helper.print("strAction: " + strAction);
        helper.print("sourcefile: " + sourcefile);
        helper.print("localpath: " + localpath);
        helper.print("segmentSize: " + granularity);
        String strTem = "segToDel: [";
        if(deletes != null) {
            for(String segIdx: deletes) {
                strTem += segIdx + ",";
            }
        }
        helper.print(strTem + "]");
    }
}