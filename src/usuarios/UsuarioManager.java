package src.usuarios;

import com.sun.tools.javac.Main;

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
  private static final String DADOS_LOGIN = "login_usuarios.csv";
  private static final String BD_USUARIOS = "bd_usuarios.csv";
  private List<Usuario> usuarios;

  public UsuarioManager() throws FileNotFoundException {
    this.usuarios = new ArrayList<>();
    createIfNotExists();
//    carregarUsuarios();
  }

  public boolean login(String cpf, String senha) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length == 3 && dados[0].equals(cpf) && dados[1].equals(senha)) {
          return Boolean.parseBoolean(dados[2]);
        }
      }
      return false;
    } catch (IOException e) {
      throw new RuntimeException("Erro ao carregar usuários ", e);
    }
  }

  public void salvarDadosAcessoUsuario(String cpf, String senha, String administrador) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(DADOS_LOGIN, true))) {
      writer.write(cpf + "," + senha + "," + administrador);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar usuário ", e);
    }
  }

  public void salvarUsuario(String cpf, String nome, String telefone, String administrador) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_USUARIOS, true))) {
      writer.write(cpf + "," + nome + "," + telefone + "," + administrador);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar usuário ", e);
    }
  }

  public String bemVindoUsuario(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(DADOS_LOGIN))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if(dados.length >= 3 && dados[0].equals(cpf)) {
          return "Boas-vindas, " + dados[1] + "!";
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao buscar o nome do usuário. ", e);
    }
    return null;
  }

  private boolean arquivoExiste(String nomeArquivo) {
    return new File(nomeArquivo).exists();
  }

  public boolean usuarioExiste(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(cpf)) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao verificar se o usuário existe", e);
    }
    return false;
    }

  public void createIfNotExists() {
    try {
      if (!arquivoExiste(DADOS_LOGIN)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DADOS_LOGIN))) {
          writer.write("CPF, SENHA, ADMINISTRADOR");
          writer.newLine();
        }
      }
      if (!arquivoExiste(BD_USUARIOS)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_USUARIOS))) {
          writer.write("CPF, NOME, TELEFONE, ADMINISTRADOR");
          writer.newLine();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void removerUsuario(String cpf) {
    List<Usuario> listaUsuarios = new ArrayList<>();

    for (Usuario usuario : usuarios) {
      if (!usuario.getCpf().equals(cpf)) {
        listaUsuarios.add(usuario);
      }
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_USUARIOS))) {
      writer.write("CPF, NOME, TELEFONE, ADMINISTRADOR");
      writer.newLine();

      for (Usuario usuario : listaUsuarios) {
        writer.write(usuario.getCpf() + "," + usuario.getNome() + "," + usuario.getTelefone() + "," + usuario.getAdministrador());
        writer.newLine();
      }
      System.out.println("Usuário removido com sucesso.");
    } catch (IOException e) {
      throw new RuntimeException("Erro ao remover usuário.", e);
    }
  }

  public boolean isAdministrador(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(cpf)) {
          return Boolean.parseBoolean(dados[3]);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao verificar se o usuário é administrador", e);
    }
    return false;
  }

  public void visualizarCadastro() {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4) {
          String cpf = dados[0];
          String nome = dados[1];
          String telefone = dados[2];
          String administrador = dados[3];
          System.out.println("CPF: " + cpf);
          System.out.println("Nome: " + nome);
          System.out.println("Telefone: " + telefone);
          System.out.println("Administrador: " + administrador);
          System.out.println("=".repeat(70));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao visualizar cadastro de usuários", e);
    }
  }

  public void visualizarProprioCadastro(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4) {
          if (cpf.equals(dados[0])) {
            cpf = dados[0];
            String nome = dados[1];
            String telefone = dados[2];
            String administrador = dados[3].equals("true") ? "Gerente" : "Cliente";
            System.out.println("=".repeat(70));
            System.out.println("CPF: " + cpf);
            System.out.println("Nome: " + nome);
            System.out.println("Telefone: " + telefone);
            System.out.println("Cadastro de: " + administrador);
            System.out.println("=".repeat(70));
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao visualizar cadastro de usuários", e);
    }
  }

  public void alterarProprioCadastro(String cpf, String novoNome, String novoTelefone) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4 && cpf.equals(dados[0])) {
          dados[1] = novoNome;
          dados[2] = novoTelefone;
        }
        writer.write(String.join(",", dados));
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao alterar cadastro de usuário", e);
    }

    File file = new File(BD_USUARIOS);
    File tempFile = new File("temp.txt");
    if (tempFile.renameTo(file)) {
      System.out.println("Cadastro alterado com sucesso.");
    } else {
      throw new RuntimeException("Erro ao renomear o arquivo temporário.");
    }
  }

  public void removerProprioCadastro(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp_bd.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4 && cpf.equals(dados[0])) {
          continue;
        }
        writer.write(linha);
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao remover cadastro de usuário do arquivo BD_USUARIOS", e);
    }

    File fileBD = new File(BD_USUARIOS);
    File tempFileBD = new File("temp_bd.txt");
    if (!tempFileBD.renameTo(fileBD)) {
      throw new RuntimeException("Erro ao renomear o arquivo temporário para BD_USUARIOS.");
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(DADOS_LOGIN));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp_login.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 3 && cpf.equals(dados[0])) {
          continue;
        }
        writer.write(linha);
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao remover cadastro de usuário do arquivo LOGIN_USUARIOS", e);
    }

    File fileLogin = new File(DADOS_LOGIN);
    File tempFileLogin = new File("temp_login.txt");
    if (!tempFileLogin.renameTo(fileLogin)) {
      throw new RuntimeException("Erro ao renomear o arquivo temporário para LOGIN_USUARIOS.");
    }

    System.out.println("Cadastro removido com sucesso.");
  }
}
