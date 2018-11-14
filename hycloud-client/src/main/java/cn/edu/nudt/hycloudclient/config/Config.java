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
//    \hycloud-client\src\main\resources

    private String mConfigPath = null;


    /**
     * block size for integrity verification
     */
    private int mBlockSize;

    /**
     * domain name or IP of the manager server
     */
    private String mManagerServerName;
    /**
     * port of the manager server
     */
    private int mManagerServerPort;
    /**
     * path to store the client database
     */
    private String mClientDatabasePath;
//    /**
//     * bits to represent a modulator
//     */
//    private int mModulatorBits;

    private Configuration mHdfsConf;

    private String mHdfsDeleteHome;
    private String mHdfsVerifyHome;

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

        // block size in MB
        this.mBlockSize = Integer.parseInt(props.getProperty("BlockSize", "128"));

        mManagerServerName = props.getProperty("ManagerServerName", "localhost");
        mManagerServerPort = Integer.parseInt(props.getProperty("ManagerServerPort", "8080"));
        mClientDatabasePath = props.getProperty("ClientDatabasePath", "./yhbdclient.db");
//        mModulatorBits = Integer.parseInt(props.getProperty("ModulatorBits", "160"));

        this.mHdfsConf = new Configuration();
        mHdfsConf.set("fs.default.name",
                props.getProperty("fs.default.name", "hdfs://192.168.6.173:9000"));
        mHdfsDeleteHome = props.getProperty("HdfsDeleteHome", "hdfs://192.168.6.173:9000/yhbd/delete/");
        this.mHdfsVerifyHome= props.getProperty("HdfsVerifyHome", "hdfs://192.168.6.173:9000/yhbd/verify/");
        freader.close();
    }

    public static void initConfig() throws IOException {
        Properties props = new Properties();

        // block size in MB
        props.setProperty("BlockSize", "128");

        props.setProperty("ManagerServerName", "localhost");
        props.setProperty("ManagerServerPort", "8080");

        props.setProperty("ClientDatabasePath", "./yhbdclient.db");

        props.setProperty("fs.default.name", "hdfs://192.168.6.129:9000");
        props.setProperty("HdfsDeleteHome", "hdfs://192.168.6.129:9000/yhbd/delete/");
        props.setProperty("HdfsVerifyHome", "hdfs://192.168.6.129:9000/yhbd/verify/");

        FileWriter fwriter = new FileWriter(PropertiesFilePath);
        props.store(fwriter, "Configure file for YHBD client");
        fwriter.close();
    }


    public String getManagerServerName() {
        return mManagerServerName;
    }

    public int getManagerServerPort() {
        return mManagerServerPort;
    }

    public String getClientDatabasePath() {
        return mClientDatabasePath;
    }

//    public int getModulatorBits() {
//        return mModulatorBits;
//    }

    public Configuration getHdfsConf() {
        return this.mHdfsConf;
    }

    public String getHdfsDeleteHome() {
        return this.mHdfsDeleteHome;
    }

    public String getHdfsVerifyHome(){
        return  this.mHdfsVerifyHome;
    }

    /**
     *
     * @return
     * - block size in B
     */
    public int getBlockSize() {
        return mBlockSize * 1024 * 1024;
    }

    public String getManagerServerUrl(){
        return "http://" +this.mManagerServerName + ":" + this.mManagerServerPort + "/";
    }

    public void dump() {
        helper.print("mClientDatabasePath: " + this.mClientDatabasePath);
        helper.print("mManagerServerName: " + this.mManagerServerName);
        helper.print("mManagerServerPort: " + this.mManagerServerPort);
//        helper.print("mModulatorBits: " + this.mModulatorBits);
        helper.print("mHdfsConf: " + this.mHdfsConf);
        helper.print("mHdfsDeleteHome: " + this.mHdfsDeleteHome);
        helper.print("mHdfsVerifyHome: " + this.mHdfsVerifyHome);
        helper.print("mBlockSize: " + this.mBlockSize);
    }

    public static void main(String ... argv) throws IOException {
//        Config.initConfig();

        Config cfg = Config.getConfig();
        cfg.dump();
        helper.print(cfg.getManagerServerUrl());
    }
}
