package org.example;
///jupiter, mockito, assertj
public class Main {
    public static void main(String[] args) {
        MageRepository mr = new MageRepository();
        MageController mc = new MageController(mr);
        mr.initialize(new Mage[]{new Mage("m1", 100)});
        System.out.println( mc.save("m1", "100"));
    }
}