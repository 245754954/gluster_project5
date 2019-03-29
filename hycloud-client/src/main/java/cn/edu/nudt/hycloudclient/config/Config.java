package cn.edu.nudt.hycloudclient.config;

import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config mConfig;

    private static final String clientConfigFileName = "client.properties";
    private static final String PropertiesFilePath = "./hycloud-client/" + clientConfigFileName;


    private String mConfigPath=null;
    /**
     * block size for integrity verification
     */
    private int mBlockSize;

    private int num_of_challenge;


    /**
     * path to store the client database
     */
    private String mClientDatabasePath;


    private String server_url;

    private String store_directory;

    public static Config getConfig() throws IOException {
        if(mConfig == null) {
            mConfig = new Config();
        }
        return mConfig;
    }
    public static Config getConfig(String configPath) throws IOException {
        if(mConfig == null) {
            mConfig = new Config(configPath);
        }
        return mConfig;
    }

    private Config() throws IOException {
//        this.mConfigPath = this.PropertiesFilePath;
        loadConfig();
    }

    private Config(String configPath) throws IOException {
//        this.mConfigPath = this.PropertiesFilePath;
        this.mConfigPath = configPath;
        loadConfig();
    }

    private void loadConfig() throws IOException {
        Properties props = new Properties();

        String temConfigPath = this.PropertiesFilePath;
        if(this.mConfigPath != null){
            temConfigPath = this.mConfigPath;
        }else{
            File temfile = new File("./" + clientConfigFileName);
            if (temfile.exists() && temfile.isFile()){
                temConfigPath = "./" + clientConfigFileName;
            }
        }
        FileReader freader = new FileReader(temConfigPath);

        props.load(freader);

        //upload and download url
        this.server_url = props.getProperty("server_url","http://127.0.0.1:8080");
        this.store_directory = props.getProperty("store_directory","/home/ftp");
        //the blocksize the unit is Byte,defalut value is 128KB
        this.mBlockSize = Integer.parseInt(props.getProperty("BlockSize", "128"));
        //the number of challenge
        this.num_of_challenge = Integer.parseInt(props.getProperty("num_of_challenge","3"));

        mClientDatabasePath = props.getProperty("ClientDatabasePath", "./yhbdclient.db");
        freader.close();
    }

    public static void initConfig() throws IOException {
        /*
        Properties props = new Properties();

        // block size in MB
        props.setProperty("BlockSize", "128");



        props.setProperty("ClientDatabasePath", "./yhbdclient.db");


        FileWriter fwriter = new FileWriter(PropertiesFilePath);
        props.store(fwriter, "Configure file for YHBD client");
        fwriter.close();*/

    }


    public String getClientDatabasePath() {
        return mClientDatabasePath;
    }

//    public int getModulatorBits() {
//        return mModulatorBits;
//    }



    public String getServer_url() {
        return server_url;
    }

    public String getStore_directory() {
        return store_directory;
    }

    public int getNum_of_challenge() {
        return num_of_challenge;
    }

    /**
     *
     * @return
     * - block size in B
     */
    public int getBlockSize() {
        return mBlockSize * 1024 * 1024;
    }







    public void dump() {
        helper.print("mClientDatabasePath: " + this.mClientDatabasePath);

        helper.print("mBlockSize: " + this.mBlockSize);

    }

    public static void main(String ... argv) throws IOException {
        //Config.initConfig();

        Config cfg = Config.getConfig();
        cfg.dump();

    }
}