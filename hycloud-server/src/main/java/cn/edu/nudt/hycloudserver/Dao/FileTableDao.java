package cn.edu.nudt.hycloudserver.Dao;


import cn.edu.nudt.hycloudserver.entity.FileTable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface FileTableDao extends JpaRepository<FileTable, Long> {
    public FileTable findByFilename(String filename);
}
