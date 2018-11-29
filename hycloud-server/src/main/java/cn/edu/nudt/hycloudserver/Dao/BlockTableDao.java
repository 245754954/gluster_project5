package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudserver.entity.BlockTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BlockTableDao extends JpaRepository<BlockTable, Long> {
    public BlockTable findByFilenameAndBlockIdx(String filename, int blockIdx);
    public List<BlockTable> findByFilenameAndStatus(String filename, int status);
    public int deleteByFilename(String filename);


    //利用原生的SQL进行查询
    @Query(value = "SELECT * FROM block_table WHERE filename=?1 AND status=?2 AND block_idx BETWEEN ?3 AND ?4", nativeQuery = true)
    public List<BlockTable> findByFilenameAndStatusAndBlockIdxBetween(String filename, int status, long startBlockIdx, long endBlockIdx);

//    //利用原生的SQL进行修改操作
//    @Query(value = "update block_info set status=?1 where filename=?2 and block_idx=?3", nativeQuery = true)
//    @Modifying
//    public void updateBlockStatus(Integer status, String filename, Integer blockIdx);
//
//    @Query(value = "update block_table set copy_num=?1 where filename=?2 and block_idx=?3", nativeQuery = true)
//    @Modifying
//    public void updateBlockCopyNum(Integer copyNum, String filename, Integer blockIdx);

}
