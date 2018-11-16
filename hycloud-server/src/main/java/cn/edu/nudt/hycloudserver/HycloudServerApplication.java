package cn.edu.nudt.hycloudserver;

import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
public class HycloudServerApplication {

	public static void main(String[] args)throws Exception {
		ServerConfig.getConfig();

		SpringApplication.run(HycloudServerApplication.class, args);

		//pom中没有加spring-boot-starter-web依赖，启动时没有tomcat容器，会自动退出，所以加了一个sleep防止自动退出
	}
}
