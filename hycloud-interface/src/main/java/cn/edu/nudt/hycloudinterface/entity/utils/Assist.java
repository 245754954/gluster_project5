package cn.edu.nudt.hycloudinterface.entity.utils;


public class Assist {
    public static int pow(int base, int power) {
        int res = 1;
        for (int i = 0; i < power; i++) {
            res *= base;
        }
        return res;
    }

    /**
     * get appropriate buffer to fit the 64 KB restriction of reading from socket
     * @param blockSize
     * - size of block in bytes
     * @return
     * @throws Exception
     */
    public static int getBufferSize(int blockSize) throws Exception {
        int bufferSize = blockSize;
        int i = 0;
        while(bufferSize > 65535 && i <= 4) {
            bufferSize /= 2;
            i++;
        }
        if(bufferSize > 65535) {
            bufferSize = -1;
            throw new Exception("Error Segment Size");
        }
        return bufferSize;
    }
}

