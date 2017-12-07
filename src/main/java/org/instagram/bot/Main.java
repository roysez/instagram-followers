package org.instagram.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            InputStream input = new FileInputStream("instagram.properties");
            prop.load(input);

            String login = prop.getProperty("instagram.login");
            String password = prop.getProperty("instagram.password");
            String target = prop.getProperty("instagram.target");

            Instagram instagram = new Instagram(login, password);
            instagram.setTarget(target);
            instagram.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
