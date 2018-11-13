package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;
import cn.edu.nudt.hycloudserver.entity.VerifyTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyDao extends JpaRepository<VerifyTable,String> {

   public VerifyTable findVerifyTableByFilenameEqualsAndBlockidEquals(String filename,Integer blockid);
}
