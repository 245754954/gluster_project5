package cn.edu.nudt.hycloudserver.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class StudentServer implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    public StudentServer() {

    }

    public StudentServer(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudentServer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
