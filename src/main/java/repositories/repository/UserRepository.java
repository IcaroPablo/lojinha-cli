package src.main.java.repositories.repository;

import src.main.java.entities.Usuario;
import src.main.java.repositories.UserRepositoryView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static src.main.java.constants.Constantes.BD_USER_CABECALHO;
import static src.main.java.constants.Constantes.BD_USUARIOS;
import static src.main.java.constants.Constantes.CLIENTE;
import static src.main.java.constants.Constantes.GERENTE;
import static src.main.java.constants.Constantes.LOGIN_GERENTES;
import static src.main.java.constants.Constantes.LOGIN_USUARIOS;
import static src.main.java.utils.Present.print;
import static src.main.java.utils.Present.println;

public class UserRepository implements UserRepositoryView {
  private List<Usuario> usuarios;

  @Override
  public boolean login(String cpf, String senha, String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (LOGIN_USUARIOS.equals(fileName) && dados.length == 3 && dados[0].equals(cpf) && dados[1].equals(senha)) {
          return dados[2].equals(CLIENTE);
        } else if(LOGIN_GERENTES.equals(fileName) && dados.length == 3 && dados[0].equals(cpf) && dados[1].equals(senha)) {
          return dados[2].equals(GERENTE);
        }
      }
      return false;
    } catch (IOException e) {
      throw new RuntimeException("Erro ao carregar usuários ", e);
    }
  }

  @Override
  public boolean usuarioExiste(String cpf, String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
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

  @Override
  public String bemVindoUsuario(String cpf, String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if(dados.length >= 3 && dados[0].equals(cpf)) {
          return "=".repeat(15) + "\n".concat("Boas-vindas, ".concat(dados[1])).concat("=".repeat(15));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao buscar o nome do usuário. ", e);
    }
    return null;
  }

  @Override
  public void visualizarCadastro(String cpf, String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4) {
          if (cpf.equals(dados[0])) {
            cpf = dados[0];
            String nome = dados[1];
            String telefone = dados[2];
            print("=".repeat(70).concat(" \n CPF: "
                .concat(cpf)
                .concat(" \n Nome: ")
                .concat(nome)
                .concat(" \n Telefone: ")
                .concat(telefone)
                .concat(" \n ")
                .concat("=".repeat(70)).concat(" \n ")));
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao visualizar cadastro de usuários", e);
    }
  }

  @Override
  public void alterarCadastro(String cpf, String novoNome, String novoTelefone) {
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
      println("Cadastro alterado com sucesso.");
    } else {
      throw new RuntimeException("Erro ao renomear o arquivo temporário.");
    }
  }

  @Override
  public void salvarDadosUsuario(String cpf, String senha, String userType, String fileName) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
      writer.write(cpf + "," + senha + "," + userType);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar usuário ", e);
    }
  }

  @Override
  public void salvarUsuario(String cpf, String nome, String telefone, String userType, String fileName) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
      writer.write(cpf + "," + nome + "," + telefone + "," + userType);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar usuário ", e);
    }
  }

  @Override
  public void removerUsuario(String cpf) {
    List<Usuario> listaUsuarios = new ArrayList<>();

    for (Usuario usuario : usuarios) {
      if (!usuario.getCpf().equals(cpf)) {
        listaUsuarios.add(usuario);
      }
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_USUARIOS))) {
      writer.write(BD_USER_CABECALHO);
      writer.newLine();

      for (Usuario usuario : listaUsuarios) {
        writer.write(usuario.getCpf() + "," + usuario.getNome() + "," + usuario.getTelefone() + "," + usuario.getAdministrador());
        writer.newLine();
      }
      println("Usuário removido com sucesso.");
    } catch (IOException e) {
      throw new RuntimeException("Erro ao remover usuário.", e);
    }
  }

  @Override
  public void removerCadastro(String cpf) {
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

    try (BufferedReader reader = new BufferedReader(new FileReader(LOGIN_USUARIOS));
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

    File fileLogin = new File(LOGIN_USUARIOS);
    File tempFileLogin = new File("temp_login.txt");
    if (!tempFileLogin.renameTo(fileLogin)) {
      throw new RuntimeException("Erro ao renomear o arquivo temporário para LOGIN_USUARIOS.");
    }

    println("Cadastro removido com sucesso.");
  }

  @Override
  public void visualizarCadastros() {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      reader.readLine();
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4) {
          String cpf = dados[0];
          String nome = dados[1];
          String telefone = dados[2];
          String administrador = dados[3];

          print(" CPF: "
              .concat(cpf)
              .concat(" \n Nome: ")
              .concat(nome)
              .concat(" \n Telefone: ")
              .concat(telefone)
              .concat(" \n Administrador: ")
              .concat(administrador + "\n")
              .concat("=".repeat(70)).concat(" \n "));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao visualizar cadastro de usuários", e);
    }
  }
}
