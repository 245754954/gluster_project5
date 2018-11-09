package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.database.StorageBase;
import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StorageHandler {
	static MessageDigest MD5 = null;
    static {
        try {
        MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
        ne.printStackTrace();
        }
    }
	
	public static void put(String sourcefile) throws IOException {
		Config conf = Config.getConfig();

		File sfile = new File(sourcefile);
		String filename = sfile.getName();
		String hdfsPath = conf.getHdfsHome() + "plain_" + filename;
				
		FileSystem hdfs = FileSystem.get(conf.getHdfsConf()) ;
		InputStream fin = new BufferedInputStream(new FileInputStream(sourcefile));
		OutputStream out = hdfs.create(new Path(hdfsPath));
		
		int flag = 0;
		int index = 0;
		String[] hash;
        hash = new String[999];
		byte[] buffer = new byte[1024*1024];
		int nread = 0;
		String md5 = new String(Hex.encodeHex(MD5.digest()));
		while( ( nread = fin.read(buffer)) != -1) {
			flag++;
           	MD5.update(buffer, 0, nread);
			out.write(buffer, 0, nread);
			if (flag == 128) {
           		md5 = new String(Hex.encodeHex(MD5.digest()));
           		////////////////////////////////////////////////////////System.out.println(md5);
           		hash[index++] = md5;
           		flag = 0;
           		try {
           	        MD5 = MessageDigest.getInstance("MD5");
           	        } catch (NoSuchAlgorithmException ne) {
           	        ne.printStackTrace();
           	        }
           	}
		}
		if (flag > 0) {
           	md5 = new String(Hex.encodeHex(MD5.digest()));
        	////////////////////////////////////////////////////////////System.out.println(md5);
        	hash[index++] = md5;
        } 
		out.close();
		fin.close();
		
		StorageBase sb = new StorageBase();
		sb.insert(filename, hdfsPath);
		sb.close();
	}
	
	public static void get(String filename, String localpath) throws IOException {
		Config conf = Config.getConfig();
		
		StorageBase sb = new StorageBase();
		String hdfsPath = sb.getHdfsPath(filename); 
		sb.close();
		
		FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
		InputStream input = hdfs.open(new Path(hdfsPath));
		FileOutputStream fout = new FileOutputStream(localpath);
		
		byte[] buffer = new byte[1024];
		int nread = 0;
		while( (nread = input.read(buffer)) != -1) {
			fout.write(buffer, 0, nread);
		}
		fout.close();
		input.close();
//		
//		try{
//			FileSystem hdfs = FileSystem.get(conf.getHdfsConf());
////					lPath.getFileSystem(conf.getHdfsConf()) ;
//			hdfs.copyToLocalFile(false, new Path(hdfsPath), new Path(localpath),true) ;
//		} catch(IOException ie){
//			ie.printStackTrace() ;
//		}
	}
}
