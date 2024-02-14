package src.main.java.constants;

import java.io.IOException;

import static src.main.java.config.AppConfig.properties;

public class Constantes {

  public static final String LOGIN_USUARIOS = properties.getProperty("login_usuarios");
  public static final String LOGIN_GERENTES = properties.getProperty("login_gerentes");
  public static final String BD_USUARIOS = properties.getProperty("bd_usuarios");
  public static final String BD_GERENTES = properties.getProperty("bd_gerentes");
  public static final String BD_PRODUTOS = properties.getProperty("bd_produtos");
  public static final String CARRINHO = properties.getProperty("carrinho_compras");
  public static final String MINHAS_COMPRAS = properties.getProperty("minhas_compras");
  public static final String COMPRAS_CLIENTES = properties.getProperty("compras_clientes");
  public static final String GERENTE = "Gerente";
  public static final String CLIENTE = "Cliente";
  public static final String ADICIONAR = "adicionar";
  public static final String SUBTRAIR = "subtrair";
  public static final String SUBSTITUIR = "substituir";
  public static final String DINHEIRO = "dinheiro";
  public static final String CARTAO = "cartão";
  public static final String LOGIN_USER_CABECALHO = "CPF, SENHA, ADMINISTRADOR";
  public static final String CARRINHO_CABECALHO = "CPF, STATUS, ITENS";
  public static final String BD_USER_CABECALHO = "CPF, NOME, TELEFONE, ADMINISTRADOR";
  public static final String BD_GERENTES_CABECALHO = "CPF, NOME, TELEFONE, ADMINISTRADOR";
  public static final String LOGIN_GERENTES_CABECALHO = "CPF, SENHA, ADMINISTRADOR";
  public static final String BD_PRODUTOS_CABECALHO = "CÓDIGO, DESCRIÇAO, VALOR, QUANTIDADE";
  public static final String SAINDO = "Saindo do sistema. Até breve! \n ";
  public static final String NAO_EXISTE_USUARIO = "Não existe um usuário com o CPF informado";
  public static final String ERROR_READ_FILE = "Houve um erro ao tentar ler os dados do arquivo";
  public static final String ERROR_USER_EXISTS = "Erro ao verificar se o usuário existe";
  public static final String ERROR_UPDATE_USER = "Erro ao alterar cadastro de usuário";
  public static final String ERROR_TEMP_FILE = "Erro ao renomear o arquivo temporário";
  public static final String ERROR_CREATE_USER = "Erro ao tentar criar cadastro de usuário";
  public static final String ERROR_DELETE_USER = "Erro ao remover usuário";
  public static final String ERROR_RETRIEVE_USER = "Erro ao tentar recuperar cadastros de usuários";
  public static final String ERROR_RETRIEVE_PRODUCTS = "Erro ao visualizar cadastro de produtos";
  public static final String ERROR_PRODUCT_EXISTS = "Erro ao verificar se o produto existe";
  public static final String ERROR_CREATE_PRODUCT = "Erro ao cadastrar produto";
  public static final String ERROR_UPDATE_PRODUCT = "Erro ao alterar cadastro de produto";
  public static final String ERROR_DELETE_PRODUCT = "Erro ao excluir produto";
  public static final String SUCCESS_UPDATE = "Cadastro alterado com sucesso.\n";
  public static final String QUANTIDADE_INDISPONIVEL = "Não há quantidade disponível";
  public static final String SUCCESS_DELETE_PRODUCT = "Produto removido com sucesso.";

  public Constantes() throws IOException {
  }
}
