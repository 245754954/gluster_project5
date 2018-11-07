package cn.edu.nudt.hycloudserver.Task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//定时任务，这里用于定时周期性的执行某个任务
@Component
@EnableScheduling
public class TaskRunner {
/*
    @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    public void work() throws Exception {
        System.out.println("执行调度任务："+new Date());


    }

    //完整性验证
    @Scheduled(fixedRate = 5000)//每秒执行一次
    public void play() throws Exception {


        System.out.println("执行Quartz定时器任务："+new Date());
    }



    //数据恢复
    @Scheduled(cron = "0/2 * * * * ?") //每2秒执行一次
    public void doSomething() throws Exception {

        System.out.println("每2秒执行一个的定时任务："+new Date());
    }




    @Scheduled(cron = "0 0 0/1 * * ? ") // 每一小时执行一次
    public void goWork() throws Exception {
        System.out.println("每一小时执行一次的定时任务："+new Date());
    }

*/


}