package cn.edu.nudt.hycloudclient.util;

import java.util.Objects;

public class FileVo {

    private String filename;

    public FileVo() {

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileVo(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "FileVo{" +
                "filename='" + filename + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileVo)) return false;
        FileVo fileVo = (FileVo) o;
        return Objects.equals(getFilename(), fileVo.getFilename());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFilename());
    }
}
