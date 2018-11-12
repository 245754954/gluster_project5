package cn.edu.nudt.hycloudserver.vo;

public class TreeTableVo {

    private String filename;
    private String modulationTree;

    public TreeTableVo() {
    }

    public TreeTableVo(String filename, String modulationTree) {
        this.filename = filename;
        this.modulationTree = modulationTree;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "TreeTableVo{" +
                "filename='" + filename + '\'' +
                ", modulationTree='" + modulationTree + '\'' +
                '}';
    }

    public String getModulationTree() {
        return modulationTree;
    }

    public void setModulationTree(String modulationTree) {
        this.modulationTree = modulationTree;
    }
}
