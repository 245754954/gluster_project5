package cn.edu.nudt.hycloudserver.service;


public interface Ident {

    void buildSystem();

    void extractSecretKey();

    void encrypt();

    void decrypt();

}
