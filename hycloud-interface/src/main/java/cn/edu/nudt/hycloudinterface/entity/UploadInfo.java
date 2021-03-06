package cn.edu.nudt.hycloudinterface.entity;

public class UploadInfo {

    private String filename_and_path;
    private String challenge;
    private Long blocknumber;
    private Long real_size;
    private Long blocksize;
    private String hash_result;
    private String filename;
    private String w;
    private String y;
    private String p;

    public UploadInfo() {
    }

    public UploadInfo(String filename_and_path, String challenge, Long blocknumber, Long real_size, Long blocksize, String hash_result, String filename) {
        this.filename_and_path = filename_and_path;
        this.challenge = challenge;
        this.blocknumber = blocknumber;
        this.real_size = real_size;
        this.blocksize = blocksize;
        this.hash_result = hash_result;
        this.filename = filename;
    }

    public UploadInfo(String filename_and_path, String challenge, Long blocknumber, Long real_size, Long blocksize, String hash_result, String filename, String w, String y, String p) {
        this.filename_and_path = filename_and_path;
        this.challenge = challenge;
        this.blocknumber = blocknumber;
        this.real_size = real_size;
        this.blocksize = blocksize;
        this.hash_result = hash_result;
        this.filename = filename;
        this.w = w;
        this.y = y;
        this.p = p;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "UploadInfo{" +
                "filename_and_path='" + filename_and_path + '\'' +
                ", challenge='" + challenge + '\'' +
                ", blocknumber=" + blocknumber +
                ", real_size=" + real_size +
                ", blocksize=" + blocksize +
                ", hash_result='" + hash_result + '\'' +
                ", filename='" + filename + '\'' +
                ", w='" + w + '\'' +
                ", y='" + y + '\'' +
                ", p='" + p + '\'' +
                '}';
    }

    public void setFilename_and_path(String filename_and_path) {
        this.filename_and_path = filename_and_path;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public void setBlocknumber(Long blocknumber) {
        this.blocknumber = blocknumber;
    }

    public void setReal_size(Long real_size) {
        this.real_size = real_size;
    }

    public void setBlocksize(Long blocksize) {
        this.blocksize = blocksize;
    }

    public void setHash_result(String hash_result) {
        this.hash_result = hash_result;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename_and_path() {
        return filename_and_path;
    }

    public String getChallenge() {
        return challenge;
    }

    public Long getBlocknumber() {
        return blocknumber;
    }

    public Long getReal_size() {
        return real_size;
    }

    public Long getBlocksize() {
        return blocksize;
    }

    public String getHash_result() {
        return hash_result;
    }

    public String getFilename() {
        return filename;
    }
}
