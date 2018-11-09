package cn.edu.nudt.hycloudserver.vo;

import java.io.Serializable;
import java.util.Objects;

public class FileNameParameter implements Serializable{

    private String filename;

    public FileNameParameter() {

    }

    public FileNameParameter(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "FileNameParameter{" +
                "filename='" + filename + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileNameParameter)) return false;
        FileNameParameter that = (FileNameParameter) o;
        return Objects.equals(getFilename(), that.getFilename());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFilename());
    }
}
