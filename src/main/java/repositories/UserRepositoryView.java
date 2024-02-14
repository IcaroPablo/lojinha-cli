package src.main.java.repositories;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.rest.dtos.ClienteDto;

import java.util.List;

public interface UserRepositoryView {
  boolean login(String cpf, String senha, String fileName);

  boolean usuarioExiste(String cpf, String fileName);

  String bemVindoUsuario(String cpf, String fileName);

  void visualizarCadastro(String cpf, String fileName);

  void alterarCadastro(String cpf, String novoNome, String novoTelefone);

  void salvarDadosUsuario(String cpf, String senha, String userType, String fileName);

  void salvarUsuario(String cpf, String nome, String telefone, String userType, String fileName);

  void removerCadastro(String cpf);

  List<ClienteDto> visualizarCadastros();

  ClienteDto getCliente(String cpf);
}
