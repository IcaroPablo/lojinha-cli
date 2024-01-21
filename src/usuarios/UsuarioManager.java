package src.usuarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioManager {
  private static final String NOME_ARQUIVO = "usuarios.txt";
  private List<Usuario> usuarios;

  public UsuarioManager() throws FileNotFoundException {
    this.usuarios = new ArrayList<>();
    carregarUsuarios();
  }

  private void carregarUsuarios() throws FileNotFoundException {
    try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4) {
          boolean administrador = false;
          if (dados.length == 5 && Boolean.parseBoolean(dados[4])) {
            administrador = true;
          }
          Usuario usuario;
          if(administrador) {
            usuario = new Gerente(dados[0], dados[1], dados[2], dados[3], true);
          } else {
            usuario = new Cliente(dados[0], dados[1], dados[2], dados[3]);
          }

        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void createIfNotExists() {
    try {
      if (!arquivoExiste(NOME_ARQUIVO)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
          writer.write("Usuário,Senha");
          writer.newLine();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean autenticarUsuario(String usuario, String senha) {
    try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length == 2 && dados[0].equals(usuario) && dados[1].equals(senha)) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void cadastrarUsuario(String usuario, String senha) {
    try {
      if (!usuarioExiste(usuario)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, true))) {
          writer.write(usuario + "," + senha);
          writer.newLine();
        }
      } else {
        System.out.println("Usuário já cadastrado.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean arquivoExiste(String nomeArquivo) {
    return new File(nomeArquivo).exists();
  }

  private boolean usuarioExiste(String usuario) {
    try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(usuario)) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
