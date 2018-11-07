package cn.edu.nudt.hycloudclient.Controller;


import cn.edu.nudt.hycloudserver.entity.ModulationTree;
import cn.edu.nudt.hycloudserver.service.StudentService;
import cn.edu.nudt.hycloudserver.service.serviceimpl.ModularServiceImpl;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class StudentController {

    @Reference
    private StudentService studentService;


    @RequestMapping(value = "/sayHello",method = {RequestMethod.GET,RequestMethod.POST})
    public String sayHello(){

        studentService.sayHello();



        return "sss";
    }

}
