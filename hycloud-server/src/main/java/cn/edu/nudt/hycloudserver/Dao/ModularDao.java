package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudserver.entity.ModulationTree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModularDao extends JpaRepository<ModulationTree,String> {

    public ModulationTree findByPath(String path);
}
