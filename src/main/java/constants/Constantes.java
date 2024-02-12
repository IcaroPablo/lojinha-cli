package src.main.java.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constantes {
  static Properties properties;

//  public static final String LOGIN_USUARIOS = properties.getProperty("login_usuarios");
  public static final String LOGIN_USUARIOS = "/home/joao/Documentos/JV/lojinha-arquivos/login_user.csv";
  public static final String LOGIN_GERENTES = "/home/joao/Documentos/JV/lojinha-arquivos/login_gerente.csv";
//  public static final String LOGIN_GERENTES = properties.getProperty("login_gerentes");
  public static final String BD_USUARIOS = "/home/joao/Documentos/JV/lojinha-arquivos/bd_usuarios.csv";
//  public static final String BD_USUARIOS = properties.getProperty("bd_usuarios");
  public static final String BD_GERENTES = "/home/joao/Documentos/JV/lojinha-arquivos/bd_gerente.csv";
//  public static final String BD_GERENTES = properties.getProperty("bd_gerentes");
//  public static final String BD_PRODUTOS = properties.getProperty("bd_produtos");
  public static final String BD_PRODUTOS = "/home/joao/Documentos/JV/lojinha-arquivos/bd_produtos.csv";
//  public static final String BD_ESTOQUE = properties.getProperty("bd_estoque");
  public static final String BD_ESTOQUE = "/home/joao/Documentos/JV/lojinha-arquivos/bd_estoque.csv";
  public static final String GERENTE = "Gerente";
  public static final String CLIENTE = "Cliente";
  public static final String ADICIONAR = "adicionar";
  public static final String SUBTRAIR = "subtrair";
  public static final String LOGIN_USER_CABECALHO = "CPF, SENHA, ADMINISTRADOR";
  public static final String BD_USER_CABECALHO = "CPF, NOME, TELEFONE, ADMINISTRADOR";
  public static final String BD_GERENTES_CABECALHO = "CPF, NOME, TELEFONE, ADMINISTRADOR";
  public static final String LOGIN_GERENTES_CABECALHO = "CPF, SENHA, ADMINISTRADOR";
  public static final String BD_PRODUTOS_CABECALHO = "CÓDIGO, DESCRIÇAO, VALOR";
  public static final String BD_ESTOQUE_CABECALHO = "CÓDIGO, DESCRIÇAO, VALOR";
  public static final String SAINDO = "Saindo do sistema. Até breve! \n ";

  public Constantes() throws IOException {
  }

  static {
    try {
      properties = getProp();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Properties getProp() throws IOException {
    Properties props = new Properties();
    FileInputStream file = new FileInputStream(
        "src/main/resources/application.properties");
    props.load(file);
    return props;

  }
}
