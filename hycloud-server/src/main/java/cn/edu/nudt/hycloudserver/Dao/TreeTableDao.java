package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudserver.entity.ModulationTreeServer;
import cn.edu.nudt.hycloudserver.entity.TreeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeTableDao extends JpaRepository<TreeTable,String> {
    public TreeTable findTreeTableByFilename(String filename);
}
