package cn.edu.nudt.hycloudserver.service.serviceimpl;

import cn.edu.nudt.hycloudserver.Dao.TreeTableDao;
import cn.edu.nudt.hycloudserver.entity.TreeTable;
import cn.edu.nudt.hycloudserver.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TreeServiceImpl implements TreeService {

    @Autowired
    private TreeTableDao treeTableDao;
    @Override
    public TreeTable findTreeTableByFilename(String filename) {
        return treeTableDao.findTreeTableByFilename(filename);
    }

    @Transactional
    @Override
    public Boolean save(TreeTable tree) {

        treeTableDao.save(tree);
        return true;

    }
}
