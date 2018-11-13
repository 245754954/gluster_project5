package cn.edu.nudt.hycloudserver.controller;


import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudserver.entity.TreeTable;
import cn.edu.nudt.hycloudserver.service.TreeService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tree")
public class TreeTableController {
    @Autowired
    private TreeService treeService;

    //根据文件唯一标识找到ModulationTree对象
    @RequestMapping(value = "/obtainRemoteTree",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTree(String filename){
        if(!StringUtils.isEmpty(filename))
        {
            TreeTable treeTable = treeService.findTreeTableByFilename(filename);
            ModulationTree tree = JSON.parseObject(treeTable.getModulationTree(), ModulationTree.class);
            return tree;
        }
        return null;
    }

    @RequestMapping(value = "/obtainRemoteTreeWithDel",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTreeWithDel(String filename, String segmentsToDelete) {
        if(!StringUtils.isEmpty(filename))
        {
            SegmentList recvSegmentList = JSON.parseObject(segmentsToDelete, SegmentList.class);
            TreeTable treeTable = treeService.findTreeTableByFilename(filename);
            ModulationTree tree = JSON.parseObject(treeTable.getModulationTree(), ModulationTree.class);
            tree.obtainSubTree(recvSegmentList);

            return tree;
        }
        return null;
    }
    //上传文件保存
    @RequestMapping(value = "/updateModulationTree",method = {RequestMethod.GET,RequestMethod.POST})
    public Boolean updateModulationTree(String filename, String modulationTree) {
//        String recvFilename = JSON.parseObject(filename, String.class);
//        ModulationTree recvModulationTree = JSON.parseObject(modulationTree, ModulationTree.class);

        TreeTable tree = new TreeTable();
        tree.setFilename(filename);
        tree.setModulationTree(modulationTree);
        treeService.save(tree);
        return true;
    }
//    public Boolean uploadModulationTree(@RequestBody TreeTableVo tro) {
//
//        TreeTable tree = new TreeTable();
//        tree.setFilename(tro.getFilename());
//        tree.setModulationTree(tro.getModulationTree());
//        treeService.save(tree);
//        return true;
//    }


}
