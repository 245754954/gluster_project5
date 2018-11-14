package cn.edu.nudt.hadoop.Entry.Verify;
import java.math.BigInteger;
import java.net.URI;
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
public class VerifyHandler {
    public static String INPUT_PATH="hdfs://192.168.6.129:9000/chal/chal.txt";
    public static String OUTPUT_PATH="hdfs://192.168.6.129:9000/output";

    static class MyMapper extends Mapper <Object,Text,Text,IntWritable> {     //定义继承mapper类
        //	public IntWritable one = new IntWritable(1);
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException{    //定义map方法
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line,"\n");
            while (tokenizer.hasMoreElements()) {
                StringTokenizer stringTokenizer = new StringTokenizer(tokenizer.nextToken());
                String blockPath = stringTokenizer.nextToken();
                String filename = blockPath.substring(0,blockPath.indexOf("_block_"));
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

                Text name = new Text(filename);
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

    public static String getTagHdfsPath(String HdfsPath){
        return HdfsPath.replaceAll("_block_","_tag_");
    }

    public static void startVerify(String challengeFile) throws Exception{
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
    }
}
