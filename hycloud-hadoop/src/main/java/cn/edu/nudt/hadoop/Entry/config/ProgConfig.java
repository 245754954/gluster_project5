package cn.edu.nudt.hadoop.Entry.config;

import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ProgConfig {
    private static ProgConfig mProgConfig;

    private final String propertiesFilename = "namenode.properties";
    private final String PropertiesFilePath = "./hycloud-client/src/main/resources/" + propertiesFilename;
//    \hycloud-client\src\main\resources
    private String mConfigPath = null;

    // your configuration
    private String inputPath ="hdfs://192.168.6.129:9000/chal/chal.txt";
    private String localChalName = "/home/dky/test/chal.txt";
    private String chalHdfsPath = "hdfs://192.168.6.129:9000/chal/";
    private String outputPath ="hdfs://192.168.6.129:9000/output";
    private int postFileStatus = -1;
    private String blockPathPrefix = "hdfs://192.168.6.129:9000/yhbd/verify/";

    public static ProgConfig getConfig() throws IOException {
        if(mProgConfig == null) {
            mProgConfig = new ProgConfig();
        }
        return mProgConfig;
    }
    public static ProgConfig getConfig(String configPath) throws IOException {
        if(mProgConfig == null) {
            mProgConfig = new ProgConfig(configPath);
        }
        return mProgConfig;
    }

    private ProgConfig() throws IOException {
//        this.mConfigPath = this.PropertiesFilePath;
        loadConfig();
    }

    private ProgConfig(String configPath) throws IOException {
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
            File temfile = new File("./" + propertiesFilename);
            if (temfile.exists() && temfile.isFile()){
                temConfigPath = "./" + propertiesFilename;
            }
        }
        FileReader freader = new FileReader(temConfigPath);
        props.load(freader);
        this.inputPath = props.getProperty("inputPath", "hdfs://192.168.6.129:9000/chal/chal.txt");
        this.blockPathPrefix = props.getProperty("blockPathPrefix", "hdfs://192.168.6.129:9000/yhbd/verify/");
        this.chalHdfsPath = props.getProperty("chalHdfsPath", "hdfs://192.168.6.129:9000/chal/");
        this.localChalName = props.getProperty("localChalName", "/home/dky/test/chal.txt");
        this.outputPath = props.getProperty("outputPath", "hdfs://192.168.6.129:9000/output");
        this.postFileStatus = Integer.parseInt(props.getProperty("outputPath", "-1"));
        freader.close();
    }

    private void initConfig() throws IOException {
        Properties props = new Properties();

        props.setProperty("ManagerServerName", "localhost");
        props.setProperty("ManagerServerPort", "8080");

        FileWriter fwriter = new FileWriter(PropertiesFilePath);
        props.store(fwriter, "Configure file for YHBD client");
        fwriter.close();
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getLocalChalName() {
        return localChalName;
    }

    public void setLocalChalName(String localChalName) {
        this.localChalName = localChalName;
    }

    public String getChalHdfsPath() {
        return chalHdfsPath;
    }

    public void setChalHdfsPath(String chalHdfsPath) {
        this.chalHdfsPath = chalHdfsPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public int getPostFileStatus() {
        return postFileStatus;
    }

    public void setPostFileStatus(int postFileStatus) {
        this.postFileStatus = postFileStatus;
    }

    public String getBlockPathPrefix() {
        return blockPathPrefix;
    }

    public void setBlockPathPrefix(String blockPathPrefix) {
        this.blockPathPrefix = blockPathPrefix;
    }

    public void dump() {
//        helper.print("mClientDatabasePath: " + this.mClientDatabasePath);

    }

    public static void main(String ... argv) throws IOException {
        ProgConfig cfg = ProgConfig.getConfig();
        cfg.initConfig();
        cfg.loadConfig();
        cfg.dump();
    }
}
