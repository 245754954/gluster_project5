package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.database.StorageBase;
import cn.edu.nudt.hycloudinterface.Constants.BlockStatus;
import cn.edu.nudt.hycloudinterface.Constants.FileStatus;
import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StorageHandler {


	public static void put(String sourcefilepath) throws IOException, NoSuchAlgorithmException {
		Config conf = Config.getConfig();

		File sourcefile = new File(sourcefilepath);
		String sourcefilename = sourcefile.getName();
        String hdfsPathPrefix = conf.getHdfsVerifyHome() + sourcefilename;
        String copyOnePathPrefix = conf.getHdfsVerifyCopyOneHome() + sourcefilename;
        String copyTwoPathPrefix = conf.getHdfsVerifyCopyTwoHome() + sourcefilename;

        int blockSize = conf.getBlockSize();
		long filesize = sourcefile.length();
		long blockNum = filesize / blockSize;
		if(blockNum * blockSize < filesize) blockNum++;
        helper.print("Uploading " + sourcefilename + " (size = " + filesize + ", blocks: " + blockNum + ")");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

		// delete duplicate files anb blocks
        StorageTransfer.deleteFile(sourcefilename);
        StorageTransfer.deleteFileBlocks(sourcefilename);


		FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
		FileInputStream fis = new FileInputStream(sourcefilepath);
		for (int blockIdx = 0; blockIdx < blockNum; blockIdx++) {
		    helper.print("handling block: " + blockIdx);
            digest.reset();

			String blockHdfsPath = getBlockHdfsPath(hdfsPathPrefix, blockIdx);
			OutputStream osToHdfs = hdfs.create(new Path(blockHdfsPath));

            String blockCopyOnePath = getBlockHdfsPath(copyOnePathPrefix, blockIdx);
            OutputStream copyOneOs = hdfs.create(new Path(blockCopyOnePath));

            String blockCopyTwoPath = getBlockHdfsPath(copyTwoPathPrefix, blockIdx);
            OutputStream copyTwoOs = hdfs.create(new Path(blockCopyTwoPath));

			byte[] buffer = new byte[1024];
            int currBlockSize = 0;
            int nread = 0;
            while (currBlockSize < blockSize &&  (nread = fis.read(buffer)) != -1){
                digest.update(buffer, 0, nread);
                osToHdfs.write(buffer, 0, nread);
                currBlockSize += nread;

                copyOneOs.write(buffer, 0, nread);
                copyTwoOs.write(buffer, 0, nread);
            }
            osToHdfs.close();
            copyOneOs.close();
            copyTwoOs.close();
            byte[] hash = digest.digest();

            String tagHdfsPath = getTagHdfsPath(hdfsPathPrefix, blockIdx);
            OutputStream osToHdfsForTag = hdfs.create(new Path(tagHdfsPath));
            osToHdfsForTag.write(hash, 0, hash.length);
            osToHdfsForTag.close();

//            fileInfo.addBlock(blockIdx, hash);
//            StorageTransfer.addBlockInfoToManagerServer(sourcefilename, blockIdx, hash);
            StorageTransfer.updateBlockInfo(sourcefilename, blockIdx, conf.getCopyNum(), hash);
		}
		fis.close();
        StorageTransfer.updateFileInfo(sourcefilename, blockNum);

        helper.print(sourcefilename + " Uploaded");

		StorageBase sbase = new StorageBase();
		sbase.insert(sourcefilename, blockNum, hdfsPathPrefix);
		sbase.close();
	}

	public static void get(String requestfilename, String localpath) throws IOException, NoSuchAlgorithmException {
        Config conf = Config.getConfig();

        StorageBase sbase = new StorageBase();
        long blockNum = sbase.getBlockNum(requestfilename);
        String hdfsPathPrefix = sbase.getHdfsPath(requestfilename);
        sbase.close();

        FileOutputStream fos = new FileOutputStream(localpath);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
        for (int blockIdx = 0; blockIdx < blockNum; blockIdx++) {
            digest.reset();

            String blockHdfsPath = getBlockHdfsPath(hdfsPathPrefix, blockIdx);
            BufferedInputStream bis = new BufferedInputStream(hdfs.open(new Path(blockHdfsPath)));
            byte[] buffer = new byte[1024];
            int nread = 0;
            while ((nread = bis.read(buffer)) != -1){
                digest.update(buffer, 0, nread);
                fos.write(buffer, 0, nread);
            }
            bis.close();
            byte[] hash = digest.digest();
            BigInteger calHash = new BigInteger(hash);

            String tagHdfsPath = getTagHdfsPath(hdfsPathPrefix, blockIdx);
            bis = new BufferedInputStream(hdfs.open(new Path(tagHdfsPath)));
            nread = bis.read(buffer);
            bis.close();
            byte[] hashBuffer = new byte[nread];
            System.arraycopy(buffer, 0, hashBuffer, 0, nread);
            BigInteger recvHash = new BigInteger(hashBuffer);

            if (!calHash.equals(recvHash)){
                helper.err("Error: block " + blockIdx + " of " + requestfilename);
            }
        }
        fos.close();
	}

    public static void recoverBlock(String filename, List<String> blocks) throws IOException {
        if(blocks != null) {
            helper.print("Recovering blocks of " + filename);
            for (String strIdx : blocks) {
                int blockIdx = Integer.parseInt(strIdx);
                int rv = StorageTransfer.recoverBlock(filename, blockIdx);
                helper.print(filename + ", " + blockIdx + ", recovered = " + rv);
            }
        }
    }

	public static void verifyBlock(String filename, List<String> blocks) throws IOException {
	    if(blocks != null) {
            helper.print("Checking statuses of blocks from " + filename);
            for (String strIdx : blocks) {
                int blockIdx = Integer.parseInt(strIdx);
                int status = StorageTransfer.verifyBlock(filename, blockIdx);
                helper.print(filename + ", " + blockIdx + ", status = " + BlockStatus.getStatusString(status));
            }
        }
    }

    public static void verifyFile(List<String> veifyFiles) throws IOException {
	    if(veifyFiles != null) {
            helper.print("Checking statuses of files ");
            for (String filename: veifyFiles) {
                int status = StorageTransfer.verifyFile(filename);
                helper.print(filename + " status: " + FileStatus.getStatusString(status));
            }
        }
    }

	private static String getBlockHdfsPath(String hdfsPathPrefix, int blockIdx){
        return hdfsPathPrefix+ "_block_" + blockIdx;
    }
    private static String getTagHdfsPath(String hdfsPathPrefix, int blockIdx){
        return hdfsPathPrefix+ "_tag_" + blockIdx;
    }


//	static MessageDigest MD5 = null;
//    static {
//        try {
//        MD5 = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException ne) {
//        ne.printStackTrace();
//        }
//    }
//
//	public static void put(String sourcefile) throws IOException {
//		Config conf = Config.getConfig();
//
//		File sfile = new File(sourcefile);
//		String filename = sfile.getName();
//		String hdfsPath = conf.getHdfsHome() + "plain_" + filename;
//
//		FileSystem hdfs = FileSystem.get(conf.getHdfsConf()) ;
//		InputStream fin = new BufferedInputStream(new FileInputStream(sourcefile));
//		OutputStream out = hdfs.create(new Path(hdfsPath));
//
//		int flag = 0;
//		int index = 0;
//		String[] hash;
//        hash = new String[999];
//		byte[] buffer = new byte[1024*1024];
//		int nread = 0;
//		String md5 = new String(Hex.encodeHex(MD5.digest()));
//		while( ( nread = fin.read(buffer)) != -1) {
//			flag++;
//           	MD5.update(buffer, 0, nread);
//			out.write(buffer, 0, nread);
//			if (flag == 128) {
//           		md5 = new String(Hex.encodeHex(MD5.digest()));
//           		////////////////////////////////////////////////////////System.out.println(md5);
//           		hash[index++] = md5;
//           		flag = 0;
//           		try {
//           	        MD5 = MessageDigest.getInstance("MD5");
//           	        } catch (NoSuchAlgorithmException ne) {
//           	        ne.printStackTrace();
//           	        }
//           	}
//		}
//		if (flag > 0) {
//           	md5 = new String(Hex.encodeHex(MD5.digest()));
//        	////////////////////////////////////////////////////////////System.out.println(md5);
//        	hash[index++] = md5;
//        }
//		out.close();
//		fin.close();
//
//		StorageBase sb = new StorageBase();
//		sb.insert(filename, hdfsPath);
//		sb.close();
//	}
//
//	public static void get(String filename, String localpath) throws IOException {
//		Config conf = Config.getConfig();
//
//		StorageBase sb = new StorageBase();
//		String hdfsPath = sb.getHdfsPath(filename);
//		sb.close();
//
//		FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
//		InputStream input = hdfs.open(new Path(hdfsPath));
//		FileOutputStream fout = new FileOutputStream(localpath);
//
//		byte[] buffer = new byte[1024];
//		int nread = 0;
//		while( (nread = input.read(buffer)) != -1) {
//			fout.write(buffer, 0, nread);
//		}
//		fout.close();
//		input.close();
////
////		try{
////			FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
//////					lPath.getFileSystem(conf.getHdfsConf()) ;
////			hdfs.copyToLocalFile(false, new Path(hdfsPath), new Path(localpath),true) ;
////		} catch(IOException ie){
////			ie.printStackTrace() ;
////		}
//	}
}
