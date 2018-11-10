package cn.edu.nudt.hycloudserver.controller;




import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.Node;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudserver.entity.ModulationTreeServer;
import cn.edu.nudt.hycloudserver.entity.NodeServer;
import cn.edu.nudt.hycloudserver.service.serviceimpl.ModularServiceImpl;
import cn.edu.nudt.hycloudserver.vo.FileNameParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class ModulationTreeController {

    @Autowired
    private ModularServiceImpl modularService;

    //根据文件唯一标识找到ModulationTree对象
    @RequestMapping(value = "/obtainRemoteTree",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTree(@RequestBody FileNameParameter filename){
        if(StringUtils.isEmpty(filename.getFilename()))
        {
            ModulationTreeServer treeServer = modularService.findModularTreeByPath(filename.getFilename());
            // 将NodeServer转换为Node
            List<Node> nodes = treeServer.getmTree().stream()
                    .map(n -> new Node(n.getmModulator(), n.getmStatus(), n.getTraversed()))
                    .collect(toList());
            return new ModulationTree(treeServer.getmSegmentsNum(), nodes);

        }
        return null;
    }

    @RequestMapping(value = "/obtainRemoteTreeWithDel",method = {RequestMethod.GET,RequestMethod.POST})
    public ModulationTree obtainRemoteTreeWithDel(@RequestParam("filename") String filename, @RequestBody SegmentList segmentsToDelete) {
        if(StringUtils.isEmpty(filename))
        {
            ModulationTreeServer treeServer = modularService.findModularTreeByPath(filename);
            // 将NodeServer转换为Node
            List<Node> nodes = treeServer.getmTree().stream()
                    .map(n -> new Node(n.getmModulator(), n.getmStatus(), n.getTraversed()))
                    .collect(toList());



            ModulationTree mo = new ModulationTree(treeServer.getmSegmentsNum(), nodes);
            mo.obtainSubTree(segmentsToDelete);
            return mo;

        }

        return null;

    }
    //上传文件保存
    @RequestMapping(value = "/uploadModulationTree",method = {RequestMethod.GET,RequestMethod.POST})
    public Boolean uploadModulationTree(@RequestParam("filename") String filename, @RequestBody ModulationTree modulationTree) {
        ModulationTreeServer Mo = new ModulationTreeServer();
        Mo.setPath(filename);
        Mo.setmSegmentsNum(modulationTree.getmSegmentsNum());

        List<NodeServer> nodes = new ArrayList<NodeServer>();
        for (Node node :modulationTree.getmTree())
        {
            NodeServer n = new NodeServer();
            n.setmModulator(node.getmModulator());
            n.setTraversed(node.getTraversed());
            n.setmStatus(node.getmStatus());

            nodes.add(n);
        }
        Mo.setmTree(nodes);

        modularService.saveData(Mo);
        return true;
    }




}
