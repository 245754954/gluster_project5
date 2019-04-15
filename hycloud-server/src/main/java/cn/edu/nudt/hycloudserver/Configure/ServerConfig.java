package cn.edu.nudt.hycloudserver.Configure;

import cn.edu.nudt.hycloudinterface.utils.helper;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {
    private static ServerConfig serverConfig;

    private static final String serverConfigFileName = "server.properties";
    private static final String defaultConfigPath = "./hycloud-server/" + serverConfigFileName;

    private String serverConfigPath = null;

    //the size of stripe default value 1MB
    private long stripe_size;

    //the number of bricks
    private long stripe_count;

    //the directory that store the file uploaded by client
    private String store_directory;

    public static ServerConfig getConfig() throws IOException {
        if(serverConfig == null) {
            serverConfig = new ServerConfig();
        }
        return serverConfig;
    }
    public static ServerConfig getConfig(String configPath) throws IOException {
        if(serverConfig == null) {
            serverConfig = new ServerConfig(configPath);
        }
        return serverConfig;
    }

    private ServerConfig() throws IOException {
        loadConfig();
    }

    private ServerConfig(String configPath) throws IOException {
        this.serverConfigPath = configPath;
        loadConfig();
    }

    private void loadConfig() throws IOException {
        Properties props = new Properties();

        String temConfigPath = defaultConfigPath;
        if(this.serverConfigPath != null){
            temConfigPath = this.serverConfigPath;
        }else{
            File temfile = new File("./" + serverConfigFileName);
            if (temfile.exists() && temfile.isFile()){
                temConfigPath = "./" + serverConfigFileName;
            }
        }
        FileReader freader = new FileReader(temConfigPath);

        props.load(freader);

        //the directory that store the file uploaded by client
        this.store_directory = props.getProperty("store_directory","/home/ftp/");

        //total bricks
        this.stripe_count = Long.parseLong(props.getProperty("stripe_count"));
        //the default size of stripe volume
        this.stripe_size = Long.parseLong(props.getProperty("stripe_size"));
        // block size in MB
        // block size in MB


        freader.close();
    }



    public long getStripe_size() {
        return stripe_size;
    }

    public long getStripe_count() {
        return stripe_count;
    }

    public String getStore_directory() {
        return store_directory;
    }

    public void dump() {
        helper.print("stripe_count: " + this.stripe_count);
        helper.print("stripe_size: " + this.stripe_size);
    }

    public static void main(String ... argv) throws IOException {
//        ServerConfig.initConfig();

//        ServerConfig cfg = ServerConfig.getConfig();
//        cfg.dump();

    }
}
