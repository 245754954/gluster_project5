package cn.edu.nudt.hycloudserver.Dao;

import cn.edu.nudt.hycloudserver.entity.BlockCopyOne;
import cn.edu.nudt.hycloudserver.entity.BlockCopyTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface BlockCopyTwoDao extends JpaRepository<BlockCopyTwo, Long> {
    public BlockCopyTwo findByFilenameAndBlockIdx(String filename, int blockIdx);
    public int deleteByFilename(String filename);

//    //利用原生的SQL进行修改操作
//    @Query(value = "update block_info set status=?1 where filename=?2 and block_idx=?3", nativeQuery = true)
//    @Modifying
//    public void updateBlockStatus(Integer status, String filename, Integer blockIdx);

}
