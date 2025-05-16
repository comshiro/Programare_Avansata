package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "src/main/resources/app.properties";
    private static final Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load app.properties", e);
        }
    }

    public static String getDaoType() {
        return props.getProperty("dao.type", "JPA");
    }
}
