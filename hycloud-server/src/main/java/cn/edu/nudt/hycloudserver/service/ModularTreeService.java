package cn.edu.nudt.hycloudserver.service;

import cn.edu.nudt.hycloudserver.entity.ModulationTreeServer;

public interface ModularTreeService {
    //保存数据
    public Boolean saveData(ModulationTreeServer modulationTreeServer);


    //当对方发送path之后返回modularTree对象

    public ModulationTreeServer findModularTreeByPath(String path);

}
