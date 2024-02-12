package src.main.java.service;

import src.main.java.repositories.repository.UserRepository;
import src.main.java.utils.FileUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static src.main.java.constants.Constantes.BD_GERENTES;
import static src.main.java.constants.Constantes.BD_GERENTES_CABECALHO;
import static src.main.java.constants.Constantes.BD_USER_CABECALHO;
import static src.main.java.constants.Constantes.BD_USUARIOS;
import static src.main.java.constants.Constantes.LOGIN_GERENTES;
import static src.main.java.constants.Constantes.LOGIN_GERENTES_CABECALHO;
import static src.main.java.constants.Constantes.LOGIN_USER_CABECALHO;
import static src.main.java.constants.Constantes.LOGIN_USUARIOS;

public class UsuarioService {
  private FileUtils fileUtils;
  private final UserRepository repository;

  public UsuarioService(FileUtils fileUtils, UserRepository repository) throws FileNotFoundException {
    this.fileUtils = fileUtils;
    this.repository = repository;
    fileUtils.createIfNotExists(BD_USUARIOS, BD_USER_CABECALHO);
    fileUtils.createIfNotExists(BD_GERENTES, BD_GERENTES_CABECALHO);
    fileUtils.createIfNotExists(LOGIN_USUARIOS, LOGIN_USER_CABECALHO);
    fileUtils.createIfNotExists(LOGIN_GERENTES, LOGIN_GERENTES_CABECALHO);
  }

  public boolean login(String cpf, String senha, String fileName) {
    return repository.login(cpf, senha, fileName);
  }

  public void salvarDadosAcessoUsuario(String cpf, String senha, String userType, String fileName) {
    repository.salvarDadosUsuario(cpf, senha, userType, fileName);
  }

  public void salvarUsuario(String cpf, String nome, String telefone, String userType, String fileName) {
    repository.salvarUsuario(cpf, nome, telefone, userType, fileName);
  }

  public String bemVindoUsuario(String cpf, String fileName) {
    return repository.bemVindoUsuario(cpf, fileName);
  }

  public boolean usuarioExiste(String cpf, String fileName) {
    return repository.usuarioExiste(cpf, fileName);
  }

  public void excluirUsuario(String cpf) {
    repository.removerUsuario(cpf);
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

  public void recuperarCadastros() {
    repository.visualizarCadastros();
  }

  public void verCadastro(String cpf, String fileName) {
    repository.visualizarCadastro(cpf, fileName);
  }

  public void alterarCadastro(String cpf, String novoNome, String novoTelefone) {
    repository.alterarCadastro(cpf, novoNome, novoTelefone);
  }

  public void excluirCadastro(String cpf) {
    repository.removerCadastro(cpf);
  }
}
