package cn.edu.nudt.hycloudserver.service.serviceimpl;

import cn.edu.nudt.hycloudserver.Dao.ModularDao;
import cn.edu.nudt.hycloudserver.entity.ModulationTreeServer;
import cn.edu.nudt.hycloudserver.service.ModularTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ModularServiceImpl implements ModularTreeService {

    @Autowired
    private ModularDao modularDao;

    @Override
    @Transactional
    public Boolean saveData(ModulationTreeServer modulationTreeServer) {

        modularDao.save(modulationTreeServer);

        return true;
    }

    @Override
    public ModulationTreeServer findModularTreeByPath(String path) {

        ModulationTreeServer mo = modularDao.findByPath(path);
        System.out.println("the value of mo is \n\n\n\n\n\n"+mo);
        return mo;

    }
}
