package cn.edu.nudt.hycloudhdfs;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.fs.FsShell;

@SpringBootApplication
public class HycloudHdfsApplication {


	public static void main(String[] args) {
		SpringApplication.run(HycloudHdfsApplication.class, args);
	}
}
