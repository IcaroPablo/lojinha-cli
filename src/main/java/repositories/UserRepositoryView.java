package src.main.java.repositories;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.rest.dtos.ClienteDto;

import java.util.List;

public interface UserRepositoryView {
  boolean login(String cpf, String senha, String fileName) throws BusinessException;

  boolean usuarioExiste(String cpf, String fileName) throws BusinessException;

  String bemVindoUsuario(String cpf, String fileName) throws BusinessException;

  void visualizarCadastro(String cpf, String fileName) throws BusinessException;

  void alterarCadastro(String cpf, String novoNome, String novoTelefone) throws BusinessException;

  void salvarDadosUsuario(String cpf, String senha, String userType, String fileName) throws BusinessException;

  void salvarUsuario(String cpf, String nome, String telefone, String userType, String fileName) throws BusinessException;

  void removerCadastro(String cpf) throws BusinessException;

  List<ClienteDto> visualizarCadastros() throws BusinessException;

  ClienteDto getCliente(String cpf);
}
