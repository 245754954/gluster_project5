package cn.edu.nudt.hycloudserver.Configure;

import com.beust.jcommander.Parameter;

public class CmdParams {
    @Parameter(names= {"--config","-c"}, description = "path to config file")
    private String configPath = null;


    public String getConfigPath() {
        return configPath;
    }
}
