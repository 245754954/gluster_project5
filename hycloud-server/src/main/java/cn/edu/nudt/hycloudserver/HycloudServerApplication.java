package cn.edu.nudt.hycloudserver;

import cn.edu.nudt.hycloudserver.Configure.CmdParams;
import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import com.beust.jcommander.JCommander;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HycloudServerApplication {

	public static void main(String[] args)throws Exception {
//		ServerConfig.getConfig();
		CmdParams cmdParams = new CmdParams();
		JCommander.Builder builder = JCommander.newBuilder();
		builder.addObject(cmdParams);

		JCommander jcmd = builder.build();
		jcmd.parse(args);
		jcmd.setProgramName("HycloudServerApplication");

		ServerConfig.getConfig(cmdParams.getConfigPath());

		SpringApplication.run(HycloudServerApplication.class, args);
	}
}
