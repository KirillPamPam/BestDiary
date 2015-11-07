package ru.kir.diary.client;

/**
 * Created by Kirill Zhitelev on 04.11.2015.
 */
public class Test {
    public static void main(String[] args) {
        String str = "15.10.1995";

        System.out.println(str.matches("(\\d{2}\\.){2}\\d{4}"));
    }
}
