package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudserver.entity.ModulationTreeServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModularDao extends JpaRepository<ModulationTreeServer,String> {

    public ModulationTreeServer findByPath(String path);
}
