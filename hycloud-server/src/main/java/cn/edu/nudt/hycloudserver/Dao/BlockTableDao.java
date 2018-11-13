package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudserver.entity.BlockTable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface BlockTableDao extends JpaRepository<BlockTable, Long> {
    public BlockTable findByFilenameAndBlockIdx(String filename, int blockIdx);
}
