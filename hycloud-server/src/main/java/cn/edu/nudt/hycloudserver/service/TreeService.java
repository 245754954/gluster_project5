package cn.edu.nudt.hycloudserver.service;

import cn.edu.nudt.hycloudserver.entity.TreeTable;

public interface TreeService {
    public TreeTable findTreeTableByFilename(String filename);

    public Boolean save(TreeTable tree);


}
