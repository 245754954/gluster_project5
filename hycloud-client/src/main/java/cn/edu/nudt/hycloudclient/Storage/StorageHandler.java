package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.database.StorageBase;
import cn.edu.nudt.hycloudclient.entity.UploadInfo;
import cn.edu.nudt.hycloudclient.util.HashSaltUtil;
import cn.edu.nudt.hycloudclient.util.HttpConnectionUtil;
import cn.edu.nudt.hycloudclient.util.MD5Util;
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

        //根据配置生成挑战数量
        int num_of_challenge =conf.getNum_of_challenge();
        String []challenge_array = new String[num_of_challenge];
        for(int i=0;i<num_of_challenge;i++){

            challenge_array[i]  = HashSaltUtil.salt();
        }




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
                int n = 1*1024*1024;
                byte[] buffer = new byte[1024];
                long currBlockSize = 0;
                int nread = 0;

                while (currBlockSize < blockSize &&  (nread = fis.read(buffer)) != -1){
                    //digest.update(buffer, 0, nread);
                    //osToHdfs.write(buffer, 0, nread);
                    currBlockSize += nread;
                    String str1 = new String(buffer,0,nread);
                    builder1.append(str1);
                    //copyOneOs.write(buffer, 0, nread);
                    //copyTwoOs.write(buffer, 0, nread);
                }
                //osToHdfs.close();
                //copyOneOs.close();
                //copyTwoOs.close();
                //生成每一块的摘要
                //byte[] hash = digest.digest();
                //得到挑战,每一块都要和三个salt进行hash生成
                String block_plaintext = builder1.toString();
                for(int i=0;i<num_of_challenge;i++)
                {

                    String challenge = challenge_array[i];

                    //System.out.println("the value of plaintxt "+block_plaintext);
                    //System.out.println("the length of plaintext "+block_plaintext.length());
                    String md5 = HashSaltUtil.MD5WithSalt(block_plaintext,challenge);
                    //String md5 = HashSaltUtil.MD5WithoutSalt(block_plaintext);
                    //String md5 = MD5Util.string2MD5(block_plaintext);
                    //String tagHdfsPath = getTagHdfsPath(hdfsPathPrefix, blockIdx);
                    // OutputStream osToHdfsForTag = hdfs.create(new Path(tagHdfsPath));
                    //osToHdfsForTag.write(hash, 0, hash.length);
                    //osToHdfsForTag.close();

                    //            fileInfo.addBlock(blockIdx, hash);
                    //            StorageTransfer.addBlockInfoToManagerServer(sourcefilename, blockIdx, hash);
                    //将上传到hdfs文件系统的每一块文件相关的信息记录下来，这里
                    //保存到服务器
                    //StorageTransfer.addBlock(sourcefilename, blockIdx, conf.getCopyNum(), hash);


                    sbase.insert(sourcefilename, blockIdx, challenge, base_path, currBlockSize, blockSize, md5);
                }
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
        HttpConnectionUtil.downloadFile(requestfilename,localpath);

	}




	public static void verifyBlock(String filename, List<String> blocks,List<String> challenges) throws IOException {
	    long tstart, tend;
	    StorageBase st = new StorageBase();

        if(null!=blocks)
        {
            int len = blocks.size();
            for (int i = 0; i < len; i++) {
                tstart = System.currentTimeMillis();
                String strIdx = blocks.get(i);
                int blockIdx = Integer.parseInt(strIdx);
                UploadInfo up = st.get_uploadinfo_by_filename_and_blocknumber(filename, blockIdx, challenges.get(i));
                if(null==up.getHash_result()){
                    System.out.println("The infomation of block "+(i)+" wrong please check the the input!");
                    continue;
                }
                String hash = StorageTransfer.verifyBlock(up);
                if(up.getHash_result().equals(hash))
                {
                    System.out.println("the block "+(i)+" is intact");
                }
                else
                {

                    System.out.println("the block "+(i)+" is not intact");
                }
                tend = System.currentTimeMillis();
                helper.print("spend time :"+(tend-tstart));
            }
        }
        /*
	    if(blocks != null) {
            for (String strIdx : blocks) {
                tstart = System.currentTimeMillis();


                //对于每一块需要查找本地的数据库，找到filename_and_path key
                //blocksize  real_size


                int blockIdx = Integer.parseInt(strIdx);
                UploadInfo up = st.get_uploadinfo_by_filename_and_blocknumber(filename,blockIdx,challenges.get(0));

                int status = StorageTransfer.verifyBlock(up);

                tend = System.currentTimeMillis();
                helper.print(blockIdx + ", " + BlockStatus.getStatusString(status) + ", " + (tend - tstart));
            }


        }*/
    }



	private static String getBlockHdfsPath(String hdfsPathPrefix, int blockIdx){
        return hdfsPathPrefix+ "_block_" + blockIdx;
    }
    private static String getTagHdfsPath(String hdfsPathPrefix, int blockIdx){
        return hdfsPathPrefix+ "_tag_" + blockIdx;
    }


}
