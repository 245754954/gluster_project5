package cn.edu.nudt.hycloudserver.Configure;

import cn.edu.nudt.hycloudserver.listener.ApplicationStartup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ListenerConfig {

    @Bean
    public ApplicationStartup applicationStartup() {
        return new ApplicationStartup();
    }
}
