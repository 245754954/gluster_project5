package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.database.StorageBase;
import cn.edu.nudt.hycloudclient.util.HashSaltUtil;
import cn.edu.nudt.hycloudclient.util.HttpConnectionUtil;
import cn.edu.nudt.hycloudinterface.Constants.BlockStatus;
import cn.edu.nudt.hycloudinterface.Constants.FileStatus;
import cn.edu.nudt.hycloudinterface.Constants.RestoreResult;
import cn.edu.nudt.hycloudinterface.entity.BlockList;
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
        //加载文件
        String str = new String(sourcefilepath);
        System.out.println("the sourcefilepath is "+str.trim());
		File sourcefile = new File(str);
		//得到文件名字
		String sourcefilename = sourcefile.getName();
		//得到hdfs相关的信息

        /*
        String hdfsPathPrefix = conf.getHdfsVerifyHome() + sourcefilename;
        String copyOnePathPrefix = conf.getHdfsVerifyCopyOneHome() + sourcefilename;
        String copyTwoPathPrefix = conf.getHdfsVerifyCopyTwoHome() + sourcefilename;
        */
        //拼接得到上传到服务器的详细地址
        String base_path = conf.getStore_directory()+"/"+sourcefilename;
        //得到配置文件中每一块的大小
        int blockSize = conf.getBlockSize();
        helper.print("blocksize is "+blockSize);
        //得到上传文件的长度
		long filesize = sourcefile.length();
		helper.print("the upload filesize is "+filesize);
		//得到文件可以划分为多少块
		long blockNum = filesize / blockSize;
		//如果多余的不够一块的，也要多加一块，不能删除任何多余的内容
		if(blockNum * blockSize < filesize) blockNum++;
		//打印上传的相关信息
        helper.print("Uploading " + sourcefilename + " (size = " + filesize + ", blocks: " + blockNum + ")");
        //得到hash256算法，生成摘要
        //MessageDigest digest = MessageDigest.getInstance("SHA-256");

		// delete duplicate files anb blocks
        //如果重复了的文件名字需要删除一下

        /*
        StorageTransfer.deleteFile(sourcefilename);
        StorageTransfer.deleteFileBlocks(sourcefilename);
        */

        /*
        //得到hdfs的一个实例
		FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
		*/
		//得到文件输入流
        StorageBase sbase = new StorageBase();
		FileInputStream fis = new FileInputStream(sourcefilepath);
        //对文件分块进行一块一块的往hdfs中上传
        StringBuilder builder1 = new StringBuilder();
		for (int blockIdx = 0; blockIdx < blockNum; blockIdx++)
		{
		    builder1.delete(0,builder1.length());
		    //生成每一块的摘要
		    helper.print("handling block: " + blockIdx);
            //digest.reset();
            /*
			String blockHdfsPath = getBlockHdfsPath(hdfsPathPrefix, blockIdx);
			OutputStream osToHdfs = hdfs.create(new Path(blockHdfsPath));

            String blockCopyOnePath = getBlockHdfsPath(copyOnePathPrefix, blockIdx);
            OutputStream copyOneOs = hdfs.create(new Path(blockCopyOnePath));

            String blockCopyTwoPath = getBlockHdfsPath(copyTwoPathPrefix, blockIdx);
            OutputStream copyTwoOs = hdfs.create(new Path(blockCopyTwoPath));
            */

			byte[] buffer = new byte[1024];
            int currBlockSize = 0;
            int nread = 0;

            while (currBlockSize < blockSize &&  (nread = fis.read(buffer)) != -1){
                //digest.update(buffer, 0, nread);
                //osToHdfs.write(buffer, 0, nread);
                currBlockSize += nread;
                builder1.append(buffer.toString());
                //copyOneOs.write(buffer, 0, nread);
                //copyTwoOs.write(buffer, 0, nread);
            }
            //osToHdfs.close();
            //copyOneOs.close();
            //copyTwoOs.close();
            //生成每一块的摘要
            //byte[] hash = digest.digest();
            //得到挑战
            String challenge = HashSaltUtil.salt();
            String block_plaintext = builder1.toString();
            String md5 = HashSaltUtil.MD5WithSalt(block_plaintext,challenge);

            //String tagHdfsPath = getTagHdfsPath(hdfsPathPrefix, blockIdx);
           // OutputStream osToHdfsForTag = hdfs.create(new Path(tagHdfsPath));
            //osToHdfsForTag.write(hash, 0, hash.length);
            //osToHdfsForTag.close();

//            fileInfo.addBlock(blockIdx, hash);
//            StorageTransfer.addBlockInfoToManagerServer(sourcefilename, blockIdx, hash);
            //将上传到hdfs文件系统的每一块文件相关的信息记录下来，这里
            //保存到服务器
            //StorageTransfer.addBlock(sourcefilename, blockIdx, conf.getCopyNum(), hash);


            sbase.insert(sourcefilename, blockIdx, challenge,base_path,md5);
		}
		fis.close();
		//跟新服务器的文件上传信息
        //StorageTransfer.updateFileInfo(sourcefilename, blockNum);
        //打印上传文件名字
        helper.print(sourcefilename + " Uploaded");
        //保存到本地的sqlite
		sbase.close();

		//上传文件到服务器
        HttpConnectionUtil.uploadFile("upload/multiFileUpload1",new String[]{sourcefilepath});
	}

	public static void get(String requestfilename, String localpath) throws IOException, NoSuchAlgorithmException {
        /*
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

        */
        //需要的文件名字
        //文件下载以后本地的保存路径
        HttpConnectionUtil.downloadFile(requestfilename,localpath);

	}

    public static void recoverableBlock(String filename, List<String> blocks) throws IOException {
        if(blocks != null) {
            helper.print("Recoverable testing of blocks of " + filename);
            for (String strIdx : blocks) {
                int blockIdx = Integer.parseInt(strIdx);
                boolean rv = StorageTransfer.recoverableBlock(filename, blockIdx);
                helper.print(filename + ", " + blockIdx + ", recoverable = " + rv);
            }
        }
    }

    public static void restoreBlock(String filename, List<String> blocks) throws IOException {
        if(blocks != null) {
            helper.print("Restoring blocks of " + filename);
            for (String strIdx : blocks) {
                int blockIdx = Integer.parseInt(strIdx);
                int rv = StorageTransfer.restoreBlock(filename, blockIdx);
                helper.print(filename + ", " + blockIdx + ", restored = " + RestoreResult.getString(rv));
            }
        }
    }

	public static void verifyBlock(String filename, List<String> blocks) throws IOException {
	    long tstart, tend;

	    if(blocks != null) {
            for (String strIdx : blocks) {
                tstart = System.currentTimeMillis();

                int blockIdx = Integer.parseInt(strIdx);
                int status = StorageTransfer.verifyBlock(filename, blockIdx);

                tend = System.currentTimeMillis();
                helper.print(blockIdx + ", " + BlockStatus.getStatusString(status) + ", " + (tend - tstart));
            }
        }
    }

    public static void verifyFile(String filename) throws IOException {
        int status = FileStatus.NOFOUND;
//        helper.print("Checking statuses of files ");

        long tstart = System.currentTimeMillis();
        if(filename != null) {
            status = StorageTransfer.verifyFile(filename);
        }
        long tend = System.currentTimeMillis();
        helper.print(filename + ", " + FileStatus.getStatusString(status) + ", " + (tend - tstart));
    }

    public static void locateDamaged(String filename) throws IOException {
        if(filename != null) {
            helper.print("Locate damaged blocks of " + filename);
            BlockList damaged = StorageTransfer.locateDamaged(filename);

            String damagedStr = "";
            for (int i = 0; i < damaged.size(); i++) {
                damagedStr += damaged.get(i) + ", ";
            }
            helper.print(filename + ", damaged blocks: " + damagedStr);
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
