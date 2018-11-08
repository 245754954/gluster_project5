package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudserver.entity.Student;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xphi
 * @version: 1.0 2018/11/8
 */

@RestController
public class TestController {

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject saveModulation(String student, String student1) {
//        ModulationTree mo = new ModulationTree();
//        List<Node> list = new ArrayList<>();
//        Node node1 = new Node();
//        node1.setmModulator(BigInteger.ONE);
//        node1.setmStatus(1);
//        node1.setTraversed(2);
//
//        Node node2 = new Node();
//        node2.setmModulator(BigInteger.TEN);
//        node2.setmStatus(1);
//        node2.setTraversed(2);
//
//        list.add(node1);
//        list.add(node2);
//
//        mo.setmSegmentsNum(1);
//        mo.setmTree(list);
//        mo.setPath("/usr/local/hadoop");

//        Boolean flag = modularService.saveData(mo);
//        if (flag)
//        {
//            System.out.println("add success");
//            return "success";
//        }
//        else {
//
//            System.out.println("failed hahaha");
//            return "failed";
//        }

        JSONObject jsonObject = JSONObject.fromObject(student);
        JSONObject jsonObject1 = JSONObject.fromObject(student1);
        Student studentx1 = (Student) JSONObject.toBean(jsonObject, Student.class);
        Student studentx2 = (Student) JSONObject.toBean(jsonObject1, Student.class);


        System.out.println("xxxxx"+studentx1.toString());
        System.out.println("xxxxx"+studentx2.toString());
        //System.out.println("hhhhh"+student.getName());
        studentx1.setName("haskjdhflkjahs");
        return JSONObject.fromObject(studentx1);


    }
}