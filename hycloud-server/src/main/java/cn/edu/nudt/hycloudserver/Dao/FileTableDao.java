package cn.edu.nudt.hycloudserver.Dao;


import cn.edu.nudt.hycloudserver.entity.FileTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import javax.validation.constraints.Max;

@Transactional
public interface FileTableDao extends JpaRepository<FileTable, Long> {
    public FileTable findByFilename(String filename);
    public int deleteByFilename(String filename);

    //利用原生的SQL进行修改操作
    @Query(value = "update file_table set status=?1 where filename=?2", nativeQuery = true)
    @Modifying
    public void updateFileStatus(Integer status, String filename);

//    //利用原生的SQL进行修改操作
//    @Query(value = "update file_table set copy_num =?1 where filename=?2", nativeQuery = true)
//    @Modifying
//    public void updateFileCopyNum(Integer copyNum, String filename);

}
