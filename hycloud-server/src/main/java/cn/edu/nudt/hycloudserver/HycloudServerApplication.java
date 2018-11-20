package cn.edu.nudt.hycloudserver;

import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HycloudServerApplication {

	public static void main(String[] args)throws Exception {
		ServerConfig.getConfig();

		SpringApplication.run(HycloudServerApplication.class, args);
	}
}
