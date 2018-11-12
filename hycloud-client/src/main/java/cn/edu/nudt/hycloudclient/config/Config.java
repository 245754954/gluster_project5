package cn.edu.nudt.hycloudclient.config;

import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.conf.Configuration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config mConfig;

    private final String PropertiesFilePath = "./hycloud-client/src/main/resources/client.properties";
//    \hycloud-client\src\main\resources

    private String mConfigPath;

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

    private String mHdfsHome;

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
        this.mConfigPath = this.PropertiesFilePath;
        loadConfig();
    }

    private Config(String configPath) throws IOException {
        this.mConfigPath = this.PropertiesFilePath;
        if (configPath != null){
            this.mConfigPath = configPath;
        }
        loadConfig();
    }

    private void loadConfig() throws IOException {
        Properties props = new Properties();

        FileReader freader = new FileReader(this.mConfigPath);

        props.load(freader);

        mManagerServerName = props.getProperty("ManagerServerName", "localhost");
        mManagerServerPort = Integer.parseInt(props.getProperty("ManagerServerPort", "8080"));
        mClientDatabasePath = props.getProperty("ClientDatabasePath", "./yhbdclient.db");
//        mModulatorBits = Integer.parseInt(props.getProperty("ModulatorBits", "160"));

        this.mHdfsConf = new Configuration();
        mHdfsConf.set("fs.default.name",
                props.getProperty("fs.default.name", "hdfs://192.168.6.173:9000"));
        mHdfsHome = props.getProperty("HdfsHome", "hdfs://192.168.6.173:9000/yhbd/");
        freader.close();
    }

    private void initConfig() throws IOException {
        Properties props = new Properties();

        props.setProperty("ManagerServerName", "localhost");
        props.setProperty("ManagerServerPort", "8080");
        props.setProperty("ClientDatabasePath", "./yhbdclient.db");
//        props.setProperty("ModulatorBits", "160");


        props.setProperty("fs.default.name", "hdfs://192.168.6.173:9000");
//		props.setProperty("fs.defaultFS", "hdfs://nameservice1");
//		props.setProperty("dfs.Nameservices","nameservice1");
//		props.setProperty("dfs.ha.namenodes.nameservice1","nn1,nn2");
//		props.setProperty("dfs.namenode.rpc-address.nameservice1.nn1","8020");
//		props.setProperty("dfs.namenode.rpc-address.nameservice1.nn2","8020");
//		props.setProperty("dfs.client.failover.proxy.provider.nameservice1",
//        		"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        props.setProperty("HdfsHome", "hdfs://192.168.6.173:9000/yhbd");

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

    public String getHdfsHome() {
        return this.mHdfsHome;
    }

    public void dump() {
        helper.print("mClientDatabasePath: " + this.mClientDatabasePath);
        helper.print("mManagerServerName: " + this.mManagerServerName);
        helper.print("mManagerServerPort: " + this.mManagerServerPort);
//        helper.print("mModulatorBits: " + this.mModulatorBits);
        helper.print("mHdfsConf: " + this.mHdfsConf);
        helper.print("mHdfsHome: " + this.mHdfsHome);
    }

    public static void main(String ... argv) throws IOException {
        Config cfg = Config.getConfig();
        cfg.initConfig();
        cfg.loadConfig();
        cfg.dump();
    }
}
