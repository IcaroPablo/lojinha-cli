package src.main.java.infrastructure.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HandlerProperties {
  public static Properties getProp() throws IOException {
    Properties properties = new Properties();
    FileInputStream file = new FileInputStream("src/main/resources/application.properties");
    properties.load(file);
    return properties;
  }
}
