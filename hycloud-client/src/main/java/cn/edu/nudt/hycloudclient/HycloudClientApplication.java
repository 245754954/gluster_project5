package cn.edu.nudt.hycloudclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:consumer.xml") //加载xml配
public class HycloudClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HycloudClientApplication.class, args);
	}
}
