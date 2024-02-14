package src.main.java.service;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.infrastructure.utils.Valid;
import src.main.java.repositories.UserRepositoryView;
import src.main.java.repositories.repository.UserRepository;
import src.main.java.infrastructure.utils.FileUtils;
import src.main.java.rest.dtos.ClienteDto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static src.main.java.constants.Constantes.BD_GERENTES;
import static src.main.java.constants.Constantes.BD_GERENTES_CABECALHO;
import static src.main.java.constants.Constantes.BD_USER_CABECALHO;
import static src.main.java.constants.Constantes.BD_USUARIOS;
import static src.main.java.constants.Constantes.ERROR_READ_FILE;
import static src.main.java.constants.Constantes.LOGIN_GERENTES;
import static src.main.java.constants.Constantes.LOGIN_GERENTES_CABECALHO;
import static src.main.java.constants.Constantes.LOGIN_USER_CABECALHO;
import static src.main.java.constants.Constantes.LOGIN_USUARIOS;
import static src.main.java.constants.Constantes.NAO_EXISTE_USUARIO;
import static src.main.java.infrastructure.utils.Present.print;

public class UsuarioService {
  private FileUtils fileUtils;
  private static UserRepositoryView repository;
  private final Valid valid;

  public UsuarioService(FileUtils fileUtils, UserRepository repository, Valid valid) throws FileNotFoundException {
    this.fileUtils = fileUtils;
    this.repository = repository;
    this.valid = valid;
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
    if(!valid.isValidCpf(cpf)) throw new BusinessException("O CPF não é válido.");
    return repository.usuarioExiste(cpf, fileName);
  }

  public List<ClienteDto> recuperarCadastros() {
    return repository.visualizarCadastros();
  }

  public static ClienteDto recuperarCliente(String cpf) {
    return repository.getCliente(cpf);
  }

  public void verCadastro(String cpf, String fileName) throws BusinessException {
//    if (!usuarioExiste(cpf, BD_USUARIOS)) throw new Businthrows BusinessExceptionessException(NAO_EXISTE_USUARIO);
    if (!usuarioExiste(cpf, BD_USUARIOS)) print(NAO_EXISTE_USUARIO);
    repository.visualizarCadastro(cpf, fileName);
  }

  public void alterarCadastro(String cpf, String novoNome, String novoTelefone) throws BusinessException {
//    if (!usuarioExiste(cpf, BD_USUARIOS)) throw new BusinessException(NAO_EXISTE_USUARIO);
    if (!usuarioExiste(cpf, BD_USUARIOS)) print(NAO_EXISTE_USUARIO);
    repository.alterarCadastro(cpf, novoNome, novoTelefone);
  }

  public void excluirCadastro(String cpf) throws BusinessException {
//    if (!usuarioExiste(cpf, BD_USUARIOS)) throw new BusinessException(NAO_EXISTE_USUARIO);
    repository.removerCadastro(cpf);
  }
}
