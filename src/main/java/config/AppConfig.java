package src.main.java.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
  private static final String CONFIG_FILE_PATH = "src/main/resources/application.properties";
  public static Properties properties;

  static {
    try {
      properties = new Properties();
      properties.load(new FileInputStream(CONFIG_FILE_PATH));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }
}