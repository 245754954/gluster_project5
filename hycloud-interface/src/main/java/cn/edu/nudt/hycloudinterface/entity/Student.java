package cn.edu.nudt.hycloudinterface.entity;

import java.io.Serializable;

public class Student implements Serializable {




        private Integer id;

        private String name;

        public Student() {

        }

        public Student(String name) {
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
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


