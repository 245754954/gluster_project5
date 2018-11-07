package cn.edu.nudt.hycloudserver.service;

import cn.edu.nudt.hycloudserver.entity.ModulationTree;
import cn.edu.nudt.hycloudserver.entity.Node;

import java.util.List;

public interface ModularTreeService {
    //保存数据
    public Boolean saveData(ModulationTree modulationTree);


    //当对方发送path之后返回modularTree对象

    public ModulationTree findModularTreeByPath(String path);

}
