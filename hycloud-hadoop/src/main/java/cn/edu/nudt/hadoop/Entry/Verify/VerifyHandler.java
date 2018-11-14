package cn.edu.nudt.hadoop.Entry.Verify;
import java.math.BigInteger;
import java.net.URI;

import cn.edu.nudt.hadoop.Entry.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResult;
import cn.edu.nudt.hycloudinterface.entity.BlockVerifyResultList;
import cn.edu.nudt.hycloudinterface.entity.Challenge;
import cn.edu.nudt.hycloudinterface.utils.helper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.io.IntWritable;

import java.lang.Integer;

public class VerifyHandler {
    public static final String INPUT_PATH="hdfs://192.168.6.129:9000/chal/chal.txt";
    public static String localChalName = "/home/dky/test/chal.txt";
    public static String chalHdfsPath = "hdfs://192.168.6.129:9000/chal/";
    public static String OUTPUT_PATH="hdfs://192.168.6.129:9000/output";
    public static int postFileStatus = -1;
    public static final String blockPathPrefix = "hdfs://192.168.6.129:9000/yhbd/verify/";
    public static final String blockPathMid = "_block_";
//    public static List<BlockVerifyResult> blockRequestList = new ArrayList<BlockVerifyResult>();
    private BlockVerifyResultList mBlockVerifyResultList = new BlockVerifyResultList();



    static class MyMapper extends Mapper <Object,Text,Text,IntWritable> {     //定义继承mapper类
        //	public IntWritable one = new IntWritable(1);
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException{    //定义map方法
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line,"\n");
            while (tokenizer.hasMoreElements()) {
                StringTokenizer stringTokenizer = new StringTokenizer(tokenizer.nextToken());
                String blockPath = stringTokenizer.nextToken();
                //String filename = blockPath.substring(0,blockPath.indexOf("_block_"));
                int val = 0;
                try {
                    BigInteger hash = getBlockHash(blockPath);
                    BigInteger tag = readBlockTag(blockPath);
                    boolean rv = tag.equals(hash);
                    val = (rv == true)? 1: 0;
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Text name = new Text(blockPath);
                IntWritable intWritable = new IntWritable(val);
                context.write(name,intWritable);
            }
        }
    }

    static class MyReduce extends Reducer<Text,IntWritable,Text,IntWritable>{     //定义继承reducer类
        @Override
        protected void reduce(Text key,Iterable<IntWritable> values,Context context)
                throws IOException,InterruptedException{      //定义reduce方法
            int sum = 1;
            for (IntWritable val : values) {
                sum *= val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
    public static BigInteger getBlockHash(String blockPath) throws NoSuchAlgorithmException, IOException{
       Configuration conf = new Configuration();
//        Configuration conf = ProgConfig.getConfig();
        FileSystem fs = null;
        FSDataInputStream hdfsInStream = null;
//	    fs = FileSystem.get(conf);
        fs = FileSystem.get(URI.create(blockPath), conf);
        hdfsInStream = fs.open(new Path(blockPath));

        byte[] buffer = new byte[1024 * 1024];
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        int nread = 0;
        while( (nread = hdfsInStream.read(buffer)) != -1){
            hash.update(buffer, 0, nread);
        }
        hdfsInStream.close();
        byte[] hashBytes = hash.digest();
        return new BigInteger(hashBytes);
    }


    public static BigInteger readBlockTag(String blockPath) {
        String tagPath = getTagHdfsPath(blockPath);

        BigInteger tag = null;
        byte[] ioBuffer = new byte[1024];
        try {
            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(tagPath), conf);
            FSDataInputStream hdfsInStream = fs.open(new Path(tagPath));
            int nread = hdfsInStream.read(ioBuffer);

            byte[] tagBytes = new byte[nread];
            System.arraycopy(ioBuffer, 0, tagBytes, 0, nread);
            tag = new BigInteger(tagBytes);

            hdfsInStream.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public int readOutput(String blockPath) {
        try {
            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(blockPath), conf);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(new Path(blockPath))));
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line,"\n");
                while (tokenizer.hasMoreElements()) {
                    StringTokenizer stringTokenizer = new StringTokenizer(tokenizer.nextToken());
                    String filePath = stringTokenizer.nextToken();
                    String status = stringTokenizer.nextToken();
                    String blockID = filePath.substring(filePath.indexOf("_block_")+7);
                    BlockVerifyResult mblockRequest = new BlockVerifyResult();
                    mblockRequest.setBlockIdx(Integer.parseInt(blockID));
                    mblockRequest.setStatus(Integer.parseInt(status));
//                    mblockRequest.setBlockID(Integer.parseInt(blockID));
//                    mblockRequest.setBlockStatus(Integer.parseInt(status));
                    this.mBlockVerifyResultList.addBlockVerifyResult(mblockRequest);
                }
            }
            reader.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postFileStatus;
    }

    public static String getTagHdfsPath(String HdfsPath){
        return HdfsPath.replaceAll("_block_","_tag_");
    }

    public VerifyHandler() {
    }

    public void startVerify(String challengeFile) throws Exception{
      Challenge challenge = VerifyTransfer.fetchChallenge();
//        Challenge challenge = new Challenge();
//        challenge.setBlockNum(new Long((long)30));
//        challenge.setFilename("jdk.tar.gz");
        storeChallenge(challenge);

        Configuration conf=new Configuration();
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);

        Path outputpath=new Path(OUTPUT_PATH);    //输出路径
        FileSystem filesystem = outputpath.getFileSystem(conf);
        if (filesystem.exists(outputpath)) {
            filesystem.delete(outputpath,true);
        }

        Job job=Job.getInstance(conf);     //定义一个job，启动任务
        job.setJobName("sha-256");
        job.setJarByClass(VerifyHandler.class);
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.setInputPaths(job, challengeFile);
        FileOutputFormat.setOutputPath(job,new Path(OUTPUT_PATH));
        job.waitForCompletion(true);

        readOutput(OUTPUT_PATH + "/part-r-00000");
        //System.out.println(status);

//        for(Iterator iterators = mBlockVerifyResultList.iterator(); iterators.hasNext();){
//            BlockVerifyResult example = (BlockVerifyResult) iterators.next();//获取当前遍历的元素，指定为Example对象
//            int bid = example.getBlockIdx();
//            int bstatus = example.getStatus();
//            System.out.println("BlockID:" + bid);
//            System.out.println("BlockStatus:" + bstatus);
//        }
//        BlockVerifyResultList mBlockVerifyResultList = new BlockVerifyResultList(blockRequestList);
        for (int i = 0; i < this.mBlockVerifyResultList.size(); i++) {
            BlockVerifyResult blockVerifyResult = this.mBlockVerifyResultList.getBlockVerifyResult(i);
            helper.print(blockVerifyResult.getBlockIdx() + ", " + blockVerifyResult.getStatus());
        }
        VerifyTransfer.submitResult(challenge.getFilename(), mBlockVerifyResultList);
    }

    public void storeChallenge(Challenge challenge) throws Exception{
        Configuration conf=new Configuration();
        String filename = challenge.getFilename();
        String fileHdfsPath = null;
        long blocknum = challenge.getBlockNum();
        long i = 0;
        PrintWriter writer = null;
        writer = new PrintWriter(localChalName, "UTF-8");
        while (i < blocknum) {
            String line = "";
            fileHdfsPath = blockPathPrefix + filename + blockPathMid + i;
            line += fileHdfsPath;
            writer.println(line);
            i++;
        }
        writer.close();
        putToHDFS(localChalName,chalHdfsPath,conf);
    }

    public boolean putToHDFS(String src , String dst , Configuration conf){
        Path dstPath = new Path(dst) ;
        try{
            FileSystem hdfs = dstPath.getFileSystem(conf) ;
            hdfs.copyFromLocalFile(false, new Path(src), dstPath) ;
        }
        catch(IOException ie){
            ie.printStackTrace() ;
            return false ;
        }
        return true ;
    }
}


