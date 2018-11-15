package cn.edu.nudt.hycloudclient.Entry;

import cn.edu.nudt.hycloudclient.Storage.StorageHandler;
import cn.edu.nudt.hycloudclient.Storage.StorageTransfer;
import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.deletion.DeletionHandler;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.JCommander.Builder;
import com.beust.jcommander.Parameter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Client {
    @Parameter(names= {"--config","-c"}, description = "path to config file")
    private String configPath = null;

    @Parameter(names= {"--help","-h"}, help=false, description = "action to perform")
    private boolean help = false;

//    @Parameter(names= {"--action","-a"}, required = true, description = "action to perform")
    @Parameter(required = true, description = "action to perform")
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

    @Parameter(names= {"--block", "-b"}, variableArity = true, description = "list of blocks to verify, starts with 0")
    private List<String> blocks = null;
//
//    @Parameter(names= {"--verify", "-y"}, variableArity = true, description = "list of files to verify")
//    private List<String> veifyFiles = null;
    //////////////////////////////////////////////


    public static void main(String[] argv) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException {
        System.out.println(System.getProperty("user.dir"));

        Client client = new Client();
        Builder builder = JCommander.newBuilder();
        builder.addObject(client);

        JCommander jcmd = builder.build();
        jcmd.parse(argv);
        jcmd.setProgramName("hycloud-client");

        Config conf = Config.getConfig(client.configPath);
        conf.dump();

        client.run(jcmd);
    }

    public void run(JCommander jcmd) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException {
        if(help) {
            jcmd.usage();
            System.exit(0);
        }
//		dumpArgs();

        long tstart, tend;
        // check args inside your action
        switch(Action.get(strAction)) {
            case Action.PUT:
                tstart = System.currentTimeMillis();

                StorageHandler.put(sourcefile);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart, tend);
                break;
            case Action.GET:
                tstart = System.currentTimeMillis();

                StorageHandler.get(sourcefile, localpath);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
                break;
            case Action.VERIFY:
                tstart = System.currentTimeMillis();

                StorageHandler.verifyBlock(sourcefile, blocks);
//                StorageHandler.verifyFile(veifyFiles);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
                break;
            case Action.RECOVER:
                tstart = System.currentTimeMillis();

                StorageHandler.recoverBlock(sourcefile, blocks);
//                StorageHandler.verifyFile(veifyFiles);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
                break;
            case Action.SPUT:
                tstart = System.currentTimeMillis();

                DeletionHandler.sput(sourcefile, granularity);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
                break;
            case Action.SGET:
                tstart = System.currentTimeMillis();

                DeletionHandler.sget(sourcefile, localpath);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
                break;
            case Action.SDEL:
                tstart = System.currentTimeMillis();

                DeletionHandler.sdel(sourcefile, deletes);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
                break;
            case Action.SDUMP:
                tstart = System.currentTimeMillis();

                DeletionHandler.sdump(sourcefile);

                tend = System.currentTimeMillis();
                helper.timing(strAction, tstart,tend);
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