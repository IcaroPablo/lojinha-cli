package src.main.java.repositories.repository;

import src.main.java.repositories.UserRepositoryView;
import src.main.java.rest.dtos.ClienteDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static src.main.java.constants.Constantes.BD_USUARIOS;
import static src.main.java.constants.Constantes.CLIENTE;
import static src.main.java.constants.Constantes.ERROR_CREATE_USER;
import static src.main.java.constants.Constantes.ERROR_DELETE_USER;
import static src.main.java.constants.Constantes.ERROR_READ_FILE;
import static src.main.java.constants.Constantes.ERROR_RETRIEVE_USER;
import static src.main.java.constants.Constantes.ERROR_TEMP_FILE;
import static src.main.java.constants.Constantes.ERROR_UPDATE_USER;
import static src.main.java.constants.Constantes.ERROR_USER_EXISTS;
import static src.main.java.constants.Constantes.GERENTE;
import static src.main.java.constants.Constantes.LOGIN_GERENTES;
import static src.main.java.constants.Constantes.LOGIN_USUARIOS;
import static src.main.java.infrastructure.utils.Present.print;
import static src.main.java.infrastructure.utils.Present.println;

public class UserRepository implements UserRepositoryView {

  @Override
  public boolean login(String cpf, String senha, String fileName){
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
      print(ERROR_READ_FILE);
    }
    return false;
  }

  @Override
  public boolean usuarioExiste(String cpf, String fileName){
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(cpf)) {
          return true;
        }
      }
    } catch (IOException e) {
      print(ERROR_USER_EXISTS);
    }
    return false;
  }

  @Override
  public String bemVindoUsuario(String cpf, String fileName){
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if(dados.length >= 3 && dados[0].equals(cpf)) {
          return "=".repeat(15) + "\n".concat("Boas-vindas, ".concat(dados[1])).concat("=".repeat(15));
        }
      }
    } catch (IOException e) {
      print(ERROR_READ_FILE);
    }
    return null;
  }

  @Override
  public void visualizarCadastro(String cpf, String fileName){
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
      print(ERROR_READ_FILE);
    }
  }

  @Override
  public void alterarCadastro(String cpf, String novoNome, String novoTelefone){
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
      print(ERROR_UPDATE_USER);
    }

    File file = new File(BD_USUARIOS);
    File tempFile = new File("temp.txt");
    if (tempFile.renameTo(file)) {
      println("Cadastro alterado com sucesso.");
    } else {
      print(ERROR_TEMP_FILE);
    }
  }

  @Override
  public void salvarDadosUsuario(String cpf, String senha, String userType, String fileName){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
      writer.write(cpf + "," + senha + "," + userType);
      writer.newLine();
    } catch (IOException e) {
      print(ERROR_CREATE_USER);
    }
  }

  @Override
  public void salvarUsuario(String cpf, String nome, String telefone, String userType, String fileName){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
      writer.write(cpf + "," + nome + "," + telefone + "," + userType);
      writer.newLine();
    } catch (IOException e) {
      print(ERROR_CREATE_USER);
    }
  }

  @Override
  public void removerCadastro(String cpf){
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
      print(ERROR_DELETE_USER);
    }

    File fileBD = new File(BD_USUARIOS);
    File tempFileBD = new File("temp_bd.txt");
    if (!tempFileBD.renameTo(fileBD)) {
      print(ERROR_TEMP_FILE);
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
      print(ERROR_DELETE_USER);
    }

    File fileLogin = new File(LOGIN_USUARIOS);
    File tempFileLogin = new File("temp_login.txt");
    if (!tempFileLogin.renameTo(fileLogin)) {
      print(ERROR_TEMP_FILE);
    }

    println("Cadastro removido com sucesso.");
  }

  @Override
  public List<ClienteDto> visualizarCadastros(){
    List<ClienteDto> clientes = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      reader.readLine();
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4) {
          String cpf = dados[0];
          String nome = dados[1];
          String telefone = dados[2];
          ClienteDto cliente = new ClienteDto(cpf, nome, telefone);
          clientes.add(cliente);
        }
      }
    } catch (IOException e) {
      print(ERROR_RETRIEVE_USER);
    }
    return clientes;
  }

  @Override
  public ClienteDto getCliente(String cpf) {
    ClienteDto clienteDto = null;
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_USUARIOS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] dados = line.split(",");
        if (dados[0].equals(cpf)) {
          String nome = dados[1];
          String telefone = dados[2];
          clienteDto = new ClienteDto(cpf, nome, telefone);
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return clienteDto;
  }

}
