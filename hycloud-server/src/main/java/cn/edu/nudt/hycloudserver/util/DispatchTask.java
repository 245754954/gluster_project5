package cn.edu.nudt.hycloudserver.util;


import cn.edu.nudt.hycloudinterface.entity.UploadInfo;
import cn.edu.nudt.hycloudinterface.utils.helper;
import cn.edu.nudt.hycloudserver.Configure.ServerConfig;

import java.io.IOException;

public class DispatchTask implements Runnable {

    UploadInfo up;
    int i;
    private String hash;

    public String getHash() {
        return this.hash;
    }

    public DispatchTask() {

        up = null;
    }


    public void setUp(UploadInfo up) {
        this.up = up;
        this.i = 0;
    }

    public void setI(int i) {
        this.i = i;
    }

    long tstart, tend;


    @Override
    public void run() {
        try {

            Integer blocknumber1 = Integer.parseInt(String.valueOf(up.getBlocknumber()));
            Integer blocksize1 = Integer.parseInt(String.valueOf(up.getBlocksize()));
            Integer real_size1 = Integer.parseInt(String.valueOf(up.getReal_size()));
            Integer offset = blocknumber1 * blocksize1;

            //根据条待卷的大小和配置信息计算offset
            ServerConfig config = ServerConfig.getConfig();
            long stripe_size = config.getStripe_size();
            long stripe_count = config.getStripe_count();

            long line_size = 0;
            long stripe_num = 0;
            long dest_offset = 0;



            line_size = stripe_size * stripe_count;
            stripe_num = offset / line_size;
            dest_offset = (stripe_num * stripe_size) + (offset % stripe_size);


            if (null == this.up.getHash_result()) {
                System.out.println("The infomation of block " + (this.i) + " wrong please check the the input!");
                return;
            }

            String p = this.up.getP();
            String y = this.up.getY();
            tstart = System.currentTimeMillis();
            this.hash = DispatchHandler.get_hash_with_blocknumber_and_challenge(up.getFilename_and_path(), blocksize1, offset, up.getChallenge(), blocknumber1, real_size1, dest_offset,p,y);

//            if (up.getHash_result().equals(hash)) {
//                System.out.println("the block " + (i) + " is intact");
//            }
//            else {
//                System.out.println("the block " + (i) + " is not intact");
//            }

            tend = System.currentTimeMillis();
            helper.print("spend time :" + (tend - tstart));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
