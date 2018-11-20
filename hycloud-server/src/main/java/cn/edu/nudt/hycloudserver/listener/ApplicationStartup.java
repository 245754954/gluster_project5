package cn.edu.nudt.hycloudserver.listener;

import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import cn.edu.nudt.hycloudserver.hadoop.HadoopFileSystem;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

//    @Autowired
//    BasePlatformInitializer initializer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            ServerConfig serverConfig = ServerConfig.getConfig();

            if(System.getProperty("os.name").toLowerCase().startsWith("win")){
                System.setProperty("hadoop.home.dir", serverConfig.getHadoopHomeDir());
            }
            HadoopFileSystem.getHadoopFileSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}