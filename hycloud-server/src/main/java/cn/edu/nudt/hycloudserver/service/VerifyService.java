package cn.edu.nudt.hycloudserver.service;

import cn.edu.nudt.hycloudinterface.entity.BlockInfo;
import cn.edu.nudt.hycloudserver.entity.VerifyTable;

public interface VerifyService {

    public VerifyTable saveBlockInfo(VerifyTable verifyTable);

    public VerifyTable findVerifyTableByMFilename(String filename);
}
