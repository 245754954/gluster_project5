package cn.edu.nudt.hycloudserver.vo;

import cn.edu.nudt.hycloudinterface.entity.ModulationTree;

public class FileNameModuleTreeVo {

    private String filename;
    private ModulationTree modulationTree;

    @Override
    public String toString() {
        return "FileNameModuleTreeVo{" +
                "filename='" + filename + '\'' +
                ", modulationTree=" + modulationTree +
                '}';
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ModulationTree getModulationTree() {
        return modulationTree;
    }

    public void setModulationTree(ModulationTree modulationTree) {
        this.modulationTree = modulationTree;
    }
}
