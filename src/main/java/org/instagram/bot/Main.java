package org.instagram.bot;

import org.brunocvcunha.instagram4j.Instagram4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            InputStream input = new FileInputStream("application.properties");
            prop.load(input);
            String login = prop.getProperty("instagram.login");
            String password = prop.getProperty("instagram.password");

            // Login to instagram
            Instagram4j instagram = Instagram4j.builder().username("username").password("password").build();
            instagram.setup();
            instagram.login();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
