package cn.edu.nudt.hycloudserver.entity;

public class BlockChecksum  {

    private String filepath;
    private String filename;
    private String challenge;
    private int blocksize;
    private int blocknumber;
    //需要向客户端返回的hash
    private String calhash;

    BlockChecksum(){


    }

    public BlockChecksum(String filepath, String filename, String challenge, int blocksize, int blocknumber, String calhash) {
        this.filepath = filepath;
        this.filename = filename;
        this.challenge = challenge;
        this.blocksize = blocksize;
        this.blocknumber = blocknumber;
        this.calhash = calhash;
    }


    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public int getBlocksize() {
        return blocksize;
    }

    public void setBlocksize(int blocksize) {
        this.blocksize = blocksize;
    }

    public int getBlocknumber() {
        return blocknumber;
    }

    public void setBlocknumber(int blocknumber) {
        this.blocknumber = blocknumber;
    }

    public String getCalhash() {
        return calhash;
    }

    public void setCalhash(String calhash) {
        this.calhash = calhash;
    }

    @Override
    public String toString() {
        return "BlockChecksum{" +
                "filepath='" + filepath + '\'' +
                ", filename='" + filename + '\'' +
                ", challenge='" + challenge + '\'' +
                ", blocksize=" + blocksize +
                ", blocknumber=" + blocknumber +
                ", calhash='" + calhash + '\'' +
                '}';
    }
}
