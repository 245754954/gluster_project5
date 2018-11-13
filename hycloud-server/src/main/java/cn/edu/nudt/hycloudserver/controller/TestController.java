package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.alibaba.fastjson.JSON;
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
//    public JSONObject saveModulation(String student1, String student2) {
    public void saveModulation(String modulationTree, String filename) {

        System.out.println("recv segmentList: " + modulationTree);
        ModulationTree restoreTree = JSON.parseObject(modulationTree, ModulationTree.class);
        restoreTree.dump();

        System.out.println("recv filename: " + filename);
        String restoreFilename = JSON.parseObject(filename, String.class);
        helper.print("restoreFilename: " + restoreFilename);

//        JSONObject jsonObject = JSONObject.fromObject(segmentList);
//        SegmentList recvList = (SegmentList) JSONObject.toBean(jsonObject, SegmentList.class);
//        for (int i = 0; i < recvList.size(); i++){
//            helper.print("i: " + recvList.get(i));
//        }

//            System.out.println("recv modulationTree: " + modulationTree);
//        JSONObject jsonObject = JSONObject.fromObject(modulationTree);
//        ModulationTree recvTree = (ModulationTree) JSONObject.toBean(jsonObject, ModulationTree.class);
//        recvTree.dump();



//        ModulationTreeServer mo = new ModulationTreeServer();
//        List<NodeServer> list = new ArrayList<>();
//        NodeServer node1 = new NodeServer();
//        node1.setmModulator(BigInteger.ONE);
//        node1.setmStatus(1);
//        node1.setTraversed(2);
//
//        NodeServer node2 = new NodeServer();
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

//        System.out.println("student1" + student1);
//        System.out.println("student2" + student2);
//        JSONObject jsonObject1 = JSONObject.fromObject(student1);
//        JSONObject jsonObject2 = JSONObject.fromObject(student2);
//        StudentServer studentx1 = (StudentServer) JSONObject.toBean(jsonObject1, StudentServer.class);
//        StudentServer studentx2 = (StudentServer) JSONObject.toBean(jsonObject2, StudentServer.class);
//
//
//        System.out.println("xxxxx"+studentx1.toString());
//        System.out.println("xxxxx"+studentx2.toString());
//        //System.out.println("hhhhh"+student.getName());
//
//
//        studentx1.setName("haskjdhflkjahs");
//        return JSONObject.fromObject(studentx1);

//        System.out.println("recv filename: " + filename);
//        JSONObject jsonObject = JSONObject.fromObject(filename);
//        String recvfilename = (String) JSONObject.toBean(jsonObject, String.class);
//
//        System.out.println("reconstruct filename: " + filename);
    }
}