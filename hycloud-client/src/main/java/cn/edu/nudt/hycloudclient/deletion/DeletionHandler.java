package cn.edu.nudt.hycloudclient.deletion;

import cn.edu.nudt.hycloudclient.database.LocalBase;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.utils.helper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

public class DeletionHandler {
	/**
	 * sput method divides the file into segments according to the given granularity and 
	 * encrypts segments separately with different keys that are derived from the master key by using a key modulation tree.
	 * </br>The master key and some meta information of the file are stored locally at the client side. 
	 * The encrypted data file and Key modulation tree are uploaded to the server.
	 * @param filepath
	 * - the path to the file to be uploaded.
	 * @param granularity
	 * - the size in KB of every segment. 
	 * @throws IOException 
	 */
	public static void sput(String filepath, int granularity) throws IOException {
		if(filepath == null) {
			helper.err("Error: empty filepath");
			System.exit(-1);
		}

		File localFile = new File(filepath);
		
		int segmentSize = granularity * 1024;
		long localFileSize = localFile.length();
		int segmentsNum = (int) (localFileSize / segmentSize );
		if ( (localFileSize - segmentsNum * segmentSize) > 0) {
			segmentsNum++; 
		}

		File inputFile = new File(filepath);
		String filename = inputFile.getName();
		
		ModulationTree mTree = new ModulationTree(segmentsNum);
		BigInteger masterKey = new BigInteger(ModulationTree.ModulatorBits, new SecureRandom());
		List<BigInteger> segmentKeys = mTree.deriveKeys(masterKey);
		
		String remotePath = DeletionTransfer.uploadWithEncryption(segmentSize, inputFile, segmentKeys);
		boolean rv = DeletionTransfer.updateModulationTree(filename, mTree);

		if(!rv){
			throw new IOException("Error: updateModulationTree");
		}

		LocalBase dbh = new LocalBase();
		dbh.insert(filename, filepath, masterKey, granularity, segmentsNum, remotePath);
		dbh.close();
	}
	
	/**
	 * sget method retrieves the file from the server, decrypts it and stores it to a given local path.
	 * </br>The decrypted keys are derived from the master key (stored locally) by using the key modulation tree 
	 * that is downloaded from the server. 
	 * @param filename
	 * - the file to retrieve.
	 * @param localPath
	 * - the local path to store the file.
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException 
	 */
	public static void sget(String filename, String localPath) throws IOException, IllegalBlockSizeException, BadPaddingException {
		if(filename == null || localPath == null) {
			helper.err("Error: filename or localpath is null");
			System.exit(-1);
		}
		
		LocalBase dbh = new LocalBase();
		BigInteger masterKey = dbh.getMasterKey(filename);
		String remotePath = dbh.getRemotePath(filename);
		int granularity = dbh.getGranularity(filename);
		int segmentsNum = dbh.getSegmentsNum(filename);
		dbh.close();

		ModulationTree mTree = DeletionTransfer.obtainRemoteTree(filename);
		List<BigInteger> segmentKeys = mTree.deriveKeys(masterKey);
		
		DeletionTransfer.retrieve(granularity, segmentsNum, segmentKeys, remotePath, localPath);
	}
	
	public static void sdel(String filename, List<String> segStrList) throws IOException {
		if(filename == null) {
			helper.err("Error: filename is null");
			System.exit(-1);
		}
		
		if(segStrList == null) {
			DeletionHandler.sdel(filename);
		}else {
			SegmentList segmentList = new SegmentList(segStrList);
			DeletionHandler.sdel(filename, segmentList);
		}
	}
	
	/**
	 * sdel method deletes the given segments by randomly selecting a new master key and accordingly adjusting the remote key modulation tree.
	 * @param filename
	 * @param segmentsToDelete
	 * - indexes of segments to be deleted. The indexes start with one.
	 * @throws IOException 
	 */
	public static void sdel(String filename, SegmentList segmentsToDelete) throws IOException {
		LocalBase dbh = new LocalBase();
		BigInteger oldMasterKey = dbh.getMasterKey(filename);

		ModulationTree newTree = DeletionTransfer.obtainRemoteTree(filename, segmentsToDelete);
		BigInteger newMasterKey = new BigInteger(ModulationTree.ModulatorBits, new SecureRandom());
		newTree.adjustModulators(newMasterKey, oldMasterKey);
		boolean rv = DeletionTransfer.updateModulationTree(filename, newTree);
		if(!rv){
			throw new IOException("Error: updateModulationTree");
		}
		dbh.update(filename, newMasterKey);
		dbh.close();
	}
	
	/**
	 * Delete the whole file.
	 * @param filename
	 * @throws IOException 
	 */
	public static void sdel(String filename) throws IOException {
		LocalBase dbh = new LocalBase();
		BigInteger oldMasterKey = dbh.getMasterKey(filename);
		int segmentsNum = dbh.getSegmentsNum(filename);
		
		// list of all segments
		SegmentList segmentsToDelete = new SegmentList(segmentsNum);
		
		ModulationTree newTree = DeletionTransfer.obtainRemoteTree(filename, segmentsToDelete);
		BigInteger newMasterKey = new BigInteger(ModulationTree.ModulatorBits, new SecureRandom());
		newTree.adjustModulators(newMasterKey, oldMasterKey);
		boolean rv = DeletionTransfer.updateModulationTree(filename, newTree);

		dbh.update(filename, newMasterKey);
		dbh.close();
	}
	
	/**
	 * show the status of every segment of the given file
	 * @param filename
	 * @throws IOException 
	 */
	public static void sdump(String filename) throws IOException {
		if(filename == null) {
			helper.err("Error: filename is null");
			System.exit(-1);
		}

		ModulationTree tree = DeletionTransfer.obtainRemoteTree(filename);
		List<Boolean> segStatuses = tree.getLeafNodesStatus();
		
		String strStatuses = "||";
		for(int i = 0; i < segStatuses.size(); i++) {
			strStatuses += (i+1) + ", " + segStatuses.get(i) + "||";
		}
		helper.print(strStatuses);
	}
	
}
