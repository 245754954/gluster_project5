package cn.edu.nudt.hycloudserver.controller;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xphi
 * @version: 1.0 2018/11/8
 */

@RestController
public class TestController {

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject saveModulation() {
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("abc", "bca");
        return jsonObject;


    }
}