package cn.edu.nudt.hycloudserver.controller;


import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.utils.helper;
import cn.edu.nudt.hycloudserver.Dao.TreeTableDao;
import cn.edu.nudt.hycloudserver.entity.TreeTable;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tree")
public class TreeTableController {
    @Autowired
    private TreeTableDao treeTableDao;

    //根据文件唯一标识找到ModulationTree对象
    @RequestMapping(value = "/obtainRemoteTree",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTree(String filename){
        if(!StringUtils.isEmpty(filename))
        {
            TreeTable treeTable = treeTableDao.findByFilename(filename);
            ModulationTree tree = JSON.parseObject(treeTable.getModulationTree(), ModulationTree.class);
            return tree;
        }
        return null;
    }

    @RequestMapping(value = "/obtainRemoteTreeWithDel",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTreeWithDel(String filename, String segmentsToDelete) {
        if(!StringUtils.isEmpty(filename))
        {
            TreeTable treeTable = treeTableDao.findByFilename(filename);
            SegmentList restoredSegmentsToDelete = JSON.parseObject(segmentsToDelete, SegmentList.class);

            ModulationTree tree = JSON.parseObject(treeTable.getModulationTree(), ModulationTree.class);
            tree.obtainSubTree(restoredSegmentsToDelete);
            return tree;
        }
        return null;

    }
    //上传文件保存
    @RequestMapping(value = "/updateModulationTree", method = {RequestMethod.POST})
    public Boolean updateModulationTree(String filenameKey, String modulationTreeKey) {
//        helper.print("treeLength: " + modulationTreeKey.length());

        TreeTable treeTable = treeTableDao.findByFilename(filenameKey);
        if (treeTable == null){
            treeTable = new TreeTable();
        }
        treeTable.setFilename(filenameKey);
        treeTable.setModulationTree(modulationTreeKey);
        treeTableDao.save(treeTable);
        return true;
    }


}
