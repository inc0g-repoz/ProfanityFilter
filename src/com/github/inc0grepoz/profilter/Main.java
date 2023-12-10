package com.github.inc0grepoz.profilter;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        ProfanityFilter filter = null;
        try {
            File file = new File("profanities.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            filter = new ProfanityFilter(file);
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        long time;
        String string;

        time = System.currentTimeMillis();
        string = filter.replaceMatches("you're stupid cockeating bitch ass retarded motherfucker, no offense tho", '#');
        System.out.println((System.currentTimeMillis() - time) + " ms");
        System.out.println(string);
        System.out.println();
    }

}
