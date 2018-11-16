package cn.edu.nudt.hycloudinterface.utils;

public class helper {

    public static void err(String info) {
        System.err.println(info);
    }

    public static void print(String info) {
        System.out.println(info);
    }

    public static void timing(String label, long tstart, long tend){
        System.out.println("Timing: " + label + " -> " + (tend - tstart) + " ms");
    }
}
