package cn.edu.nudt.hycloudserver.controller;


import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.Node;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudserver.entity.ModulationTreeServer;
import cn.edu.nudt.hycloudserver.entity.NodeServer;
import cn.edu.nudt.hycloudserver.entity.TreeTable;
import cn.edu.nudt.hycloudserver.service.TreeService;
import cn.edu.nudt.hycloudserver.service.serviceimpl.ModularServiceImpl;
import cn.edu.nudt.hycloudserver.vo.FileNameParameter;
import cn.edu.nudt.hycloudserver.vo.TreeTableVo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/tree")
public class TreeTableController {
    @Autowired
    private TreeService treeService;

    //根据文件唯一标识找到ModulationTree对象
    @RequestMapping(value = "/obtainRemoteTree",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTree(@RequestBody TreeTableVo tro){
        if(StringUtils.isEmpty(tro.getFilename()))
        {

            TreeTable treeTable = treeService.findTreeTableByFilename(tro.getFilename());
            ModulationTree tree = JSON.parseObject(treeTable.getModulationTree(), ModulationTree.class);
            return tree;

        }
        return null;
    }

    @RequestMapping(value = "/obtainRemoteTreeWithDel",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTreeWithDel(@RequestParam("filename") String filename, @RequestBody SegmentList segmentsToDelete) {


        if(StringUtils.isEmpty(filename))
        {

            TreeTable treeTable = treeService.findTreeTableByFilename(filename);
            ModulationTree tree = JSON.parseObject(treeTable.getModulationTree(), ModulationTree.class);
            tree.obtainSubTree(segmentsToDelete);

            return tree;

        }
        return null;

    }
    //上传文件保存
    @RequestMapping(value = "/uploadModulationTree",method = {RequestMethod.GET,RequestMethod.POST})
    public Boolean uploadModulationTree(@RequestBody TreeTableVo tro) {

        TreeTable tree = new TreeTable();
        tree.setFilename(tro.getFilename());
        tree.setModulationTree(tro.getModulationTree());
        treeService.save(tree);
        return true;
    }


}
