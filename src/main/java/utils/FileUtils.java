package src.main.java.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
  private boolean arquivoExiste(String nomeArquivo) {
    return new File(nomeArquivo).exists();
  }

  public void createIfNotExists(String fileName, String cabecalho) {
    try {
      if (!arquivoExiste(fileName)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
          writer.write(cabecalho);
          writer.newLine();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
