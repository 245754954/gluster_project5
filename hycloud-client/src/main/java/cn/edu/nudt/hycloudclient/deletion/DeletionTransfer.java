package cn.edu.nudt.hycloudclient.deletion;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.crypto.AES;
import cn.edu.nudt.hycloudclient.network.Transfer;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

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

	
	/**
	 * Obtain the key modulation tree at the remote server with the given segments deleted.
	 * @param filename
	 * - filename corresponds to the requested ModulationTree
	 * @param segmentsToDelete
	 * - indexes of segments to be deleted. The indexes start with one.
	 * @return
	 * - an object of obtained key modulation tree.
	 */
	public static ModulationTree obtainRemoteTree(String filename, SegmentList segmentsToDelete){
		ModulationTree tree = null;
		try {
			tree = Transfer.obtainRemoteTree(filename, segmentsToDelete);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tree;
	}

	/**
	 *
	 * @param filename
	 * - filename corresponds to the requested ModulationTree
	 * @return
	 */
	public static ModulationTree obtainRemoteTree(String filename){
		ModulationTree tree = null;
		try {
			tree = Transfer.obtainRemoteTree(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tree;
	}
	
	/**
	 * Update the key modulation tree for the file at the remote server. 
	 * @param tree
	 * - the key modulation tree object to be uploaded.
	 * @param filename
	 * @return
	 * - true if updating successes, false otherwise.
	 */
	public static boolean updateModulationTree(String filename, ModulationTree tree) {
		boolean rv = false;
		try {
			rv = Transfer.updateModulationTree(filename, tree);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  rv;
	}
	

	/**
	 * Upload and download the whole file, including the segments deleted
	 * @param segmentSize
	 * - size of segment in KB.
	 * @param inputFile
	 * - path of file to upload.
	 * @param segmentKeys
	 * - list of keys for every segments.
	 * @return
	 * - path at the remote server for the uploaded file.
	 * @throws IOException 
	 */
	public static String uploadWithEncryption(int segmentSize, File inputFile, List<BigInteger> segmentKeys) throws IOException {
		Config conf = Config.getConfig();
		
		String filename = inputFile.getName();
		
		String hdfsHome = conf.getHdfsDeleteHome();
		String remotePath = hdfsHome + filename;
		FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
		
		OutputStream output = hdfs.create(new Path(remotePath));
		FileInputStream fis = new FileInputStream(inputFile);
		byte[] inBuf = new byte[segmentSize];
		byte[] outBuf;
		int i = 0;
		int nread = 0;
		while( (nread = fis.read(inBuf)) != -1) {
			outBuf = AES.encrypt(inBuf, nread, segmentKeys.get(i));
			
			output.write(outBuf, 0, outBuf.length);
			output.flush();
			i++;
			if(i % 500 == 0){
				helper.print("\tfinished " + i + "/" + segmentKeys.size());
			}
		}
		fis.close();
		output.close();
		return remotePath;
	}

	/**
	 * Upload and download the whole file, including the segments deleted
	 * @param granularity
	 * - size of segment in KB.
	 * @param segmentKeys
	 * - list of keys for segments. The keys are null for deleted segments.
	 * @param remotePath
	 * - path at the remote server for the file to be deleted. 
	 * @param localPath
	 * - path at the local client to save the file.
	 * @throws IOException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException
	 */
	public static void retrieve(int granularity, int segmentsNum, List<BigInteger> segmentKeys, String remotePath, String localPath) throws IOException, IllegalBlockSizeException, BadPaddingException {
		Config conf = Config.getConfig();
		
		FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
		InputStream input = hdfs.open(new Path(remotePath));
		FileOutputStream fos = new FileOutputStream(localPath);
		
		int segmentSize = granularity * 1024;
		int inBufferSize = 1024;
		byte[] inBuffer = new byte[inBufferSize];
		byte[] outBuffer;
		Cipher cipher;
		int nread = 0, currBlockSize = 0;
		for(int i = 0; i < segmentsNum; i++) {
			if(segmentKeys.get(i) == null) {
				currBlockSize = 0;
				while(currBlockSize < segmentSize && (nread = input.read(inBuffer, 0, inBufferSize)) != -1) {
					currBlockSize += nread;
					if(nread < inBufferSize) {
						if( (nread = input.read(inBuffer, 0, inBufferSize - nread)) != -1) {
							currBlockSize += nread;
						}
					}
				}
				if(currBlockSize == segmentSize) {
					nread = input.read(inBuffer, 0, AES.AES_Block_Size);
					if(nread != AES.AES_Block_Size) {
						input.close();
						fos.close();
						throw new IOException("Error: reading file from HDFS");
					}
				}
				String unrecoverable = "[Unrecoverable segment (index = " + (i+1) + ", size = " + (currBlockSize - 16) + " bytes)]";
				fos.write(unrecoverable.getBytes());
				fos.flush();
			}else {
				cipher = AES.initAESCipher(segmentKeys.get(i), Cipher.DECRYPT_MODE);
				currBlockSize = 0;
				while(currBlockSize < segmentSize && (nread = input.read(inBuffer, 0, inBufferSize)) != -1) {
					
					outBuffer = cipher.update(inBuffer, 0, nread);
					fos.write(outBuffer, 0, outBuffer.length);
					currBlockSize += nread;
					
					if(nread < inBufferSize) {
//						helper.print("nread: " + nread);
//						helper.print("currBlockSize: " + currBlockSize);
						if( (nread = input.read(inBuffer, 0, inBufferSize - nread)) != -1) {
							outBuffer = cipher.update(inBuffer, 0, nread);
							fos.write(outBuffer, 0, outBuffer.length);
							currBlockSize += nread;
						}
					}
					fos.flush();
				}
				if(currBlockSize == segmentSize) {
					nread = input.read(inBuffer, 0, AES.AES_Block_Size);
					if(nread != AES.AES_Block_Size) {
						input.close();
						fos.close();
						throw new IOException("Error: reading file from HDFS");
					}
					outBuffer = cipher.update(inBuffer, 0, nread);
					fos.write(outBuffer, 0, outBuffer.length);
					fos.flush();
				}
				outBuffer = cipher.doFinal();
				fos.write(outBuffer, 0, outBuffer.length);
				fos.flush();
			}
		}
		fos.close();
		input.close();
	}
}
