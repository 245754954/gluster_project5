package cn.edu.nudt.hycloudserver.service.serviceimpl;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;
import cn.edu.nudt.hycloudserver.Dao.VerifyDao;
import cn.edu.nudt.hycloudserver.entity.VerifyTable;
import cn.edu.nudt.hycloudserver.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerifyServiceImpl implements VerifyService {

    @Autowired
    private VerifyDao verifyDao;


    @Override
    @Transactional
    public VerifyTable saveBlockInfo(VerifyTable verifyTable) {

        verifyDao.save(verifyTable);
        return verifyTable;
    }

    @Override
    public VerifyTable findVerifyTableByMFilename(String filename) {

        return verifyDao.findVerifyTableByMFilename(filename);
    }




}
