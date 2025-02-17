package com.end0katz.jspbl;

public class Main {

    public static void main(String[] args) {
        StringMarcher code = new StringMarcher("Hello, World!");
        System.out.println(code.str("Hello").result());
    }
}
