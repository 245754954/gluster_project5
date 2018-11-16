package cn.edu.nudt.hycloud.hadoop.Verify;
import java.math.BigInteger;
import java.net.URI;

import cn.edu.nudt.hycloud.hadoop.config.ProgConfig;
import cn.edu.nudt.hycloudinterface.entity.*;
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
import java.util.StringTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.io.IntWritable;

import java.lang.Integer;

public class VerifyHandler {
//    public static final String INPUT_PATH="hdfs://192.168.6.129:9000/chal/chal.txt";
//    public static String localChalName = "/home/dky/test/chal.txt";
//    public static String chalHdfsPath = "hdfs://192.168.6.129:9000/chal/";
//    public static String OUTPUT_PATH="hdfs://192.168.6.129:9000/output";
//    public static final String blockPathPrefix = "hdfs://192.168.6.129:9000/yhbd/verify/";
    public static final String blockPathMid = "_block_";
//    public static List<BlockVerifyResult> blockRequestList = new ArrayList<BlockVerifyResult>();
//    private BlockVerifyResultList mBlockVerifyResultList = new BlockVerifyResultList();



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

    public BlockVerifyResultList readOutput(String blockPath) {
        BlockVerifyResultList mBlockVerifyResultList = new BlockVerifyResultList();
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
                    int blockSatus;
                    if  (Integer.parseInt(status) == 1){
                        blockSatus = BlockStatus.INTACT;
                    }
                    else {
                        blockSatus = BlockStatus.DAMAGED;
                    }
                    mblockRequest.setStatus(blockSatus);
//                    mblockRequest.setBlockID(Integer.parseInt(blockID));
//                    mblockRequest.setBlockStatus(Integer.parseInt(status));
                    mBlockVerifyResultList.addBlockVerifyResult(mblockRequest);
                }
            }
            reader.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBlockVerifyResultList;
    }

    public static String getTagHdfsPath(String HdfsPath){
        String temp1 = HdfsPath.replaceAll("copyone","verify");
        String temp2 = temp1.replaceAll("copytwo","verify");
        return temp2.replaceAll("_block_","_tag_");
    }

    public VerifyHandler() {
    }

    public void subStartVerify(int copyID, Challenge challenge, Configuration conf) throws IOException, ClassNotFoundException, InterruptedException {
        Path outputpath=new Path(ProgConfig.getConfig().getOutputPath());    //输出路径
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
        FileInputFormat.setInputPaths(job, ProgConfig.getConfig().getInputPath());
        FileOutputFormat.setOutputPath(job,new Path(ProgConfig.getConfig().getOutputPath()));
        job.waitForCompletion(true);

        BlockVerifyResultList mBlockVerifyResultList = readOutput(ProgConfig.getConfig().getOutputPath() + "/part-r-00000");
        for (int i = 0; i < mBlockVerifyResultList.size(); i++) {
            BlockVerifyResult blockVerifyResult = mBlockVerifyResultList.getBlockVerifyResult(i);
            System.out.println(copyID + ",  " + blockVerifyResult.getBlockIdx() + ", " + blockVerifyResult.getStatus());
        }
        VerifyTransfer.submitResult(copyID, challenge.getFilename(), mBlockVerifyResultList);

    }

    public void startVerify() throws Exception{
      Challenge challenge = VerifyTransfer.fetchChallenge();
      System.out.println(challenge.getFilename() + ", " + challenge.getBlockNum());
//        Challenge challenge = new Challenge();
//        challenge.setBlockNum(new Long((long)30));
//        challenge.setFilename("jdk.tar.gz");

        Configuration conf=new Configuration();
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);


        for (int index = 0; index < 3; index++) {
            storeChallenge(CopyID.getCopyID(index), challenge);

            subStartVerify(CopyID.getCopyID(index),challenge, conf);
        }

//
//
//        Path outputpath=new Path(ProgConfig.getConfig().getOutputPath());    //输出路径
//        FileSystem filesystem = outputpath.getFileSystem(conf);
//        if (filesystem.exists(outputpath)) {
//            filesystem.delete(outputpath,true);
//        }
//
//        Job job=Job.getInstance(conf);     //定义一个job，启动任务
//        job.setJobName("sha-256");
//        job.setJarByClass(VerifyHandler.class);
//        job.setMapperClass(MyMapper.class);
//        job.setReducerClass(MyReduce.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);
//        FileInputFormat.setInputPaths(job, challengeFile);
//        FileOutputFormat.setOutputPath(job,new Path(ProgConfig.getConfig().getOutputPath()));
//        job.waitForCompletion(true);
//
//        readOutput(ProgConfig.getConfig().getOutputPath() + "/part-r-00000");
//        //System.out.println(status);
//
////        for (int i = 0; i < this.mBlockVerifyResultList.size(); i++) {
////            BlockVerifyResult blockVerifyResult = this.mBlockVerifyResultList.getBlockVerifyResult(i);
////            helper.print(blockVerifyResult.getBlockIdx() + ", " + blockVerifyResult.getStatus());
////        }
//        VerifyTransfer.submitResult(1, challenge.getFilename(), mBlockVerifyResultList);
    }

    public void storeChallenge(int copyID, Challenge challenge) throws Exception{
        String pathPrefix = null;
        switch (copyID){
            case CopyID.Origin:
                pathPrefix = ProgConfig.getConfig().getBlockPathPrefix();
                break;
            case CopyID.CopyONE:
                pathPrefix = ProgConfig.getConfig().getCopyOnePrefix();
                break;
            case CopyID.CopyTWO:
                pathPrefix = ProgConfig.getConfig().getCopyTwoPrefix();
                break;
        }

        Configuration conf=new Configuration();
        String filename = challenge.getFilename();
        String fileHdfsPath = null;
        long blocknum = challenge.getBlockNum();
        long i = 0;
        PrintWriter writer = null;
        writer = new PrintWriter(ProgConfig.getConfig().getLocalChalName(), "UTF-8");
        while (i < blocknum) {
            String line = "";
            fileHdfsPath = pathPrefix + filename + blockPathMid + i;
            line += fileHdfsPath;
            writer.println(line);
            i++;
        }
        writer.close();
        putToHDFS(ProgConfig.getConfig().getLocalChalName(),ProgConfig.getConfig().getChalHdfsPath(),conf);
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


