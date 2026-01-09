package com.elves.wallpaper;

class Power {
    public Power(String name) {
        System.out.println("Init: " + name);
    }
}

class Parent {
    static Power p1 = new Power("Parent Static Member"); // 静态变量
    Power p2 = new Power("Parent Instance Member");    // 成员变量

    static {
        System.out.println("Parent Static Block");
    }

    public Parent() {
        System.out.println("Parent Constructor");
    }
}

class Child extends Parent {
    static Power p3 = new Power("Child Static Member");
    Power p4 = new Power("Child Instance Member");

    static {
        System.out.println("Child Static Block");
    }

    public Child() {
        System.out.println("Child Constructor");
    }
}

public class Test {
    public static void main(String[] args) {
        new Child();
    }
}
