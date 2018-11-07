package cn.edu.nudt.hycloudclient.Controller;


import cn.edu.nudt.hycloudserver.entity.ModulationTree;
import cn.edu.nudt.hycloudserver.entity.Node;
import cn.edu.nudt.hycloudserver.service.ModularTreeService;
import cn.edu.nudt.hycloudserver.service.serviceimpl.ModularServiceImpl;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/module")
public class ModularController {

    @Reference
    private ModularTreeService modularService;

    @RequestMapping(value = "/add",method = {RequestMethod.GET,RequestMethod.POST})
    public  String saveModulation(){
        ModulationTree mo = new ModulationTree();
        List<Node> list = new ArrayList<>();
        Node node1 = new Node();
        node1.setmModulator(BigInteger.ONE);
        node1.setmStatus(1);
        node1.setTraversed(2);

        Node node2 = new Node();
        node2.setmModulator(BigInteger.TEN);
        node2.setmStatus(1);
        node2.setTraversed(2);

        list.add(node1);
        list.add(node2);

        mo.setmSegmentsNum(1);
        mo.setmTree(list);
        mo.setPath("/usr/local/hadoop");

        Boolean flag = modularService.saveData(mo);
        if (flag)
        {
            System.out.println("add success");
            return "success";
        }
        else {

            System.out.println("failed hahaha");
            return "failed";
        }


    }

    @RequestMapping(value = "/find",method = {RequestMethod.GET,RequestMethod.POST})
    public  String findModularByPath(@RequestParam("path")String path){
        System.out.println("received paht value is "+path);
        String path1 = "/usr/local/hadoop";

        ModulationTree modularTreeByPath = modularService.findModularTreeByPath(path1);

        System.out.println(modularTreeByPath);
        return  "success";
    }
}
