package cn.edu.nudt.hycloudserver.service.serviceimpl;

import cn.edu.nudt.hycloudserver.Dao.ModularDao;
import cn.edu.nudt.hycloudserver.entity.ModulationTree;
import cn.edu.nudt.hycloudserver.entity.Node;
import cn.edu.nudt.hycloudserver.service.ModularTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ModularServiceImpl implements ModularTreeService {

    @Autowired
    private ModularDao modularDao;

    @Override
    @Transactional
    public Boolean saveData(ModulationTree modulationTree) {

        modularDao.save(modulationTree);

        return true;
    }

    @Override
    public ModulationTree findModularTreeByPath(String path) {

        ModulationTree mo = modularDao.findByPath(path);
        System.out.println("the value of mo is \n\n\n\n\n\n"+mo);
        return mo;

    }
}
