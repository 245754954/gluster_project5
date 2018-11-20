package cn.edu.nudt.hycloud.hadoop.config;

import cn.edu.nudt.hycloudinterface.utils.helper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ProgConfig {
    private static ProgConfig mProgConfig;

    private static final String propertiesFilename = "namenode.properties";
    private static final String PropertiesFilePath = "./hycloud-hadoop/" + propertiesFilename;
//    \hycloud-client\src\main\resources
    private String mConfigPath = null;

    // your configuration
    private String managerServer ="127.0.0.1";
    private String managerServerPort = "8080";
    private String inputPath ="hdfs://192.168.6.129:9000/chal/chal.txt";
    private String localChalName = "/home/dky/test/chal.txt";
    private String chalHdfsPath = "hdfs://192.168.6.129:9000/chal/";
    private String outputPath ="hdfs://192.168.6.129:9000/output";
    private String blockPathPrefix = "hdfs://192.168.6.129:9000/yhbd/verify/";
    private String copyOnePrefix = "hdfs://192.168.6.129:9000/yhbd/copyone/";
    private String copyTwoPrefix = "hdfs://192.168.6.129:9000/yhbd/copytwo/";
    private String damageFilePath = "hdfs://master:9000/cpfile/damageFile";

    private int sleepTime = 30000;

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
        this.sleepTime = Integer.parseInt(props.getProperty("sleepTime", "30000"));
        this.managerServer = props.getProperty("managerServer", "127.0.0.1");
        this.managerServerPort = props.getProperty("managerServerPort", "8080");
        this.inputPath = props.getProperty("inputPath", "hdfs://192.168.6.129:9000/chal/chal.txt");
        this.blockPathPrefix = props.getProperty("blockPathPrefix", "hdfs://192.168.6.129:9000/yhbd/verify/");
        this.chalHdfsPath = props.getProperty("chalHdfsPath", "hdfs://192.168.6.129:9000/chal/");
        this.localChalName = props.getProperty("localChalName", "/home/dky/test/chal.txt");
        this.outputPath = props.getProperty("outputPath", "hdfs://192.168.6.129:9000/output");

        this.copyOnePrefix = props.getProperty("copyOnePrefix", "hdfs://192.168.6.129:9000/yhbd/copyone/");
        this.copyTwoPrefix = props.getProperty("copyTwoPrefix", "hdfs://192.168.6.129:9000/yhbd/copytwo/");

        this.damageFilePath = props.getProperty("damageFilePath","hdfs://master:9000/cpfile/damageFile");
        freader.close();
    }

    public static void initConfig() throws IOException {
        Properties props = new Properties();
        props.setProperty("sleepTime", "30000");
        props.setProperty("managerServer", "127.0.0.1");
        props.setProperty("managerServerPort", "8080");
        props.setProperty("inputPath", "hdfs://192.168.6.129:9000/chal/chal.txt");
        props.setProperty("blockPathPrefix", "hdfs://192.168.6.129:9000/yhbd/verify/");
        props.setProperty("chalHdfsPath", "hdfs://192.168.6.129:9000/chal/");
        props.setProperty("localChalName", "/home/dky/test/chal.txt");
        props.setProperty("outputPath", "hdfs://192.168.6.129:9000/output");
        props.setProperty("copyOnePrefix", "hdfs://192.168.6.129:9000/yhbd/copyone/");
        props.setProperty("copyTwoPrefix", "hdfs://192.168.6.129:9000/yhbd/copytwo/");
        props.setProperty("damageFilePath","hdfs://master:9000/cpfile/damageFile");

        FileWriter fwriter = new FileWriter(PropertiesFilePath);
        props.store(fwriter, "Configure file for YHBD client");
        fwriter.close();
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
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


    public String getBlockPathPrefix() {
        return blockPathPrefix;
    }

    public void setBlockPathPrefix(String blockPathPrefix) {
        this.blockPathPrefix = blockPathPrefix;
    }

    public String getManagerServer() {
        return managerServer;
    }

    public void setManagerServer(String managerServer) {
        this.managerServer = managerServer;
    }

    public String getManagerServerPort() {
        return managerServerPort;
    }

    public void setManagerServerPort(String managerServerPort) {
        this.managerServerPort = managerServerPort;
    }

    public String getManagerServerUrl(){
        return "http://" + this.managerServer + ":" + this.managerServerPort + "/";
    }

    public String getCopyOnePrefix() {
        return copyOnePrefix;
    }

    public String getCopyTwoPrefix() {
        return copyTwoPrefix;
    }

    public String getDamageFilePath() {return damageFilePath; }

    public void dump() {
        helper.print("outputPath: " + this.outputPath);
        helper.print("managerServer: " + this.managerServer);
        helper.print("managerServerPort: " + this.managerServerPort);
        helper.print("localChalName: " + this.localChalName);
        helper.print("chalHdfsPath: " + this.chalHdfsPath);
        helper.print("blockPathPrefix: " + this.blockPathPrefix);
        helper.print("inputPath: " + this.inputPath);
        helper.print("getManagerServerUrl: " + this.getManagerServerUrl());
    }

    public static void main(String ... argv) throws IOException {
//        ProgConfig.initConfig();

        ProgConfig cfg = ProgConfig.getConfig();
        cfg.dump();
    }
}
