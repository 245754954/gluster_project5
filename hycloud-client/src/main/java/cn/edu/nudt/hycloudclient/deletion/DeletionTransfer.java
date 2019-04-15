package cn.edu.nudt.hycloudclient.deletion;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.crypto.AES;
import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.utils.helper;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.List;

public class DeletionTransfer {
	public static final int ObtainRemoteTreeWithDeletion = 1;
	public static final int ObtainRemoteTreeWithoutDeletion = 2;
	public static final int UpdateRemoteTree = 3;
	public static final int UploadWithEncryption = 4;
	public static final int Retrieve = 5;


	



}
