package cn.edu.nudt.hycloudclient.Storage;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudclient.database.StorageBase;
import cn.edu.nudt.hycloudclient.util.HashSaltUtil;
import cn.edu.nudt.hycloudclient.util.HttpConnectionUtil;
import cn.edu.nudt.hycloudclient.util.SignUtil;
import cn.edu.nudt.hycloudinterface.entity.UploadInfo;
import cn.edu.nudt.hycloudinterface.utils.helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

		//得到文件输入流
        StorageBase sbase = new StorageBase();
		FileInputStream fis = new FileInputStream(sourcefilepath);


        //根据配置生成挑战数量
        int num_of_challenge =conf.getNum_of_challenge();
        String []challenge_array = new String[num_of_challenge];
        for(int i=0;i<num_of_challenge;i++){

            challenge_array[i]  = HashSaltUtil.salt();
        }
        HashSaltUtil ha1 = new HashSaltUtil();
        HashSaltUtil ha2 = new HashSaltUtil();
        HashSaltUtil ha3 = new HashSaltUtil();
        //生成系统参数x  y  p
        //其中x代表私侥，y代表公钥，p代表循环群系统参数

        String []param = SignUtil.generate_x_and_p();

        String x = param[0];
        String y = param[1];
        String p = param[2];
        sbase.insert_param(x,y,p);

        for (int blockIdx = 0; blockIdx < blockNum; blockIdx++)
        {
                //生成每一块的摘要
                helper.print("handling block: " + blockIdx);

                byte[] buffer = new byte[1024];
                long currBlockSize = 0;
                int nread = 0;

                while (currBlockSize < blockSize &&  (nread = fis.read(buffer)) != -1)
                {

                    currBlockSize += nread;
                    String str1 = new String(buffer,0,nread);

                    ha1.md5_with_update(str1);
                    ha2.md5_with_update(str1);
                    ha3.md5_with_update(str1);

                }

                String challenge1 = challenge_array[0];
                String challenge2 = challenge_array[1];
                String challenge3 = challenge_array[2];

                String md5_1 = ha1.md5_with_salt_final(challenge1);
                String md5_2 = ha2.md5_with_salt_final(challenge2);
                String md5_3 = ha3.md5_with_salt_final(challenge3);

                //采用BLS进行数据签名
                //sign[]代表返回的数据签名
                //sign[0] == w_str sign[1] == y_str


                String []sign1 =   SignUtil.Sign(md5_1,48,x,p);

                String []sign2 =    SignUtil.Sign(md5_2,48,x,p);
                String []sign3 =   SignUtil.Sign(md5_3,48,x,p);



                sbase.insert(sourcefilename, blockIdx, challenge1, base_path, currBlockSize, blockSize, md5_1,sign1[0],sign1[1],p);
                sbase.insert(sourcefilename, blockIdx, challenge2, base_path, currBlockSize, blockSize, md5_2,sign2[0],sign2[1],p);
                sbase.insert(sourcefilename, blockIdx, challenge3, base_path, currBlockSize, blockSize, md5_3,sign3[0],sign3[1],p);
        }




		fis.close();
		//跟新服务器的文件上传信息
        //StorageTransfer.updateFileInfo(sourcefilename, blockNum);
        //打印上传文件名字
        helper.print(sourcefilename + " Uploaded");
        //保存到本地的sqlite
		sbase.close();
		//上传文件到服务器
//        HttpConnectionUtil.uploadFile("upload/multiFileUpload1",new String[]{sourcefilepath});
	}

	public static void get(String requestfilename, String localpath) throws IOException, NoSuchAlgorithmException {
        HttpConnectionUtil.downloadFile(requestfilename,localpath);

	}




	public static void verifyBlock(String filename, List<String> blocks,List<String> challenges) throws IOException {



        StorageBase st = new StorageBase();
        List<UploadInfo> ups = new ArrayList<UploadInfo>();
        if(null!=blocks)
        {
            int len = blocks.size();
            for (int i = 0; i < len; i++)
            {
                String strIdx = blocks.get(i);
                int blockIdx = Integer.parseInt(strIdx);
                UploadInfo up = st.get_uploadinfo_by_filename_and_blocknumber(filename, blockIdx, challenges.get(i));
                ups.add(up);
            }


            List<String> result_set = StorageTransfer.verifyBlock(ups);

            for(int i=0;i<len;i++)
            {
                if(SignUtil.Verify(ups.get(i).getY(), ups.get(i).getP(), ups.get(i).getW(),result_set.get(i))==0)
                {
                    helper.print("block "+i+" is intact ");
                    helper.print("origin signature is "+ups.get(i).getHash_result());
                    helper.print("returned signature is "+result_set.get(i));
                }
                else
                {
                    helper.print("block "+i+" is not  intact");
                    helper.print("origin signature is "+ups.get(i).getHash_result());
                    helper.print("returned signature is "+result_set.get(i));

                }

            }

        }


    }



	private static String getBlockHdfsPath(String hdfsPathPrefix, int blockIdx){
        return hdfsPathPrefix+ "_block_" + blockIdx;
    }
    private static String getTagHdfsPath(String hdfsPathPrefix, int blockIdx){
        return hdfsPathPrefix+ "_tag_" + blockIdx;
    }


}
