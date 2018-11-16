package cn.edu.nudt.hycloudserver.Configure;

import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.conf.Configuration;

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


    private Configuration hdfsConf;

    private String hdfsYhbdHome;
    private String hdfsVerifyHome;

    private int copyNum;
    private String hdfsVerifyCopyOneHome;
    private String hdfsVerifyCopyTwoHome;

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

        // block size in MB

        this.hdfsConf = new Configuration();
        hdfsConf.set("fs.default.name",
                props.getProperty("fs.default.name", "hdfs://192.168.6.173:9000"));

        this.hdfsYhbdHome= props.getProperty("hdfsYhbdHome", "hdfs://192.168.6.173:9000/yhbd/");
        this.hdfsVerifyHome= props.getProperty("hdfsVerifyHome", "hdfs://192.168.6.173:9000/yhbd/verify/");

        this.copyNum = Integer.parseInt(props.getProperty("copyNum", "3"));
        this.hdfsVerifyCopyOneHome= props.getProperty("hdfsVerifyCopyOneHome", "hdfs://192.168.6.173:9000/yhbd/copyone/");
        this.hdfsVerifyCopyTwoHome = props.getProperty("hdfsVerifyCopyTwoHome", "hdfs://192.168.6.173:9000/yhbd/copytwo/");
        freader.close();
    }

    public static void initConfig() throws IOException {
        Properties props = new Properties();

        // block size in MB
        props.setProperty("fs.default.name", "hdfs://192.168.6.173:9000");

        props.setProperty("hdfsYhbdHome", "hdfs://192.168.6.173:9000/yhbd/");
        props.setProperty("hdfsVerifyHome", "hdfs://192.168.6.173:9000/yhbd/verify/");

        props.setProperty("copyNum", "3");
        props.setProperty("hdfsVerifyCopyOneHome", "hdfs://192.168.6.173:9000/yhbd/copyone/");
        props.setProperty("hdfsVerifyCopyTwoHome", "hdfs://192.168.6.173:9000/yhbd/copytwo/");


        FileWriter fwriter = new FileWriter(defaultConfigPath);
        props.store(fwriter, "Configure file for YHBD Manager Server");
        fwriter.close();
    }

    public String getHdfsYhbdHome() {
        return hdfsYhbdHome;
    }

    public Configuration getHdfsConf() {
        return hdfsConf;
    }

    public String getHdfsVerifyHome() {
        return hdfsVerifyHome;
    }

    public int getCopyNum() {
        return copyNum;
    }

    public String getHdfsVerifyCopyOneHome() {
        return hdfsVerifyCopyOneHome;
    }

    public String getHdfsVerifyCopyTwoHome() {
        return hdfsVerifyCopyTwoHome;
    }

    public void dump() {
        helper.print("hdfsVerifyCopyOneHome: " + this.hdfsVerifyCopyOneHome);
        helper.print("hdfsVerifyCopyTwoHome: " + this.hdfsVerifyCopyTwoHome);
        helper.print("hdfsVerifyHome: " + this.hdfsVerifyHome);
        helper.print("hdfsYhbdHome: " + this.hdfsYhbdHome);

        helper.print("copyNum: " + this.copyNum);
        helper.print("hdfsConf: " + this.hdfsConf);
    }

    public static void main(String ... argv) throws IOException {
        ServerConfig.initConfig();

//        ServerConfig cfg = ServerConfig.getConfig();
//        cfg.dump();

    }
}
