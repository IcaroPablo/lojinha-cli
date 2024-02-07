package src.produto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static src.constants.Constantes.BD_ESTOQUE;
import static src.constants.Constantes.BD_PRODUTOS;
import static src.constants.Constantes.BD_USUARIOS;

public class ProdutoManager {

  public ProdutoManager() throws FileNotFoundException {
    createIfNotExists();
  }

  public void createIfNotExists() {
    try {
      if (!arquivoExiste(BD_PRODUTOS)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_PRODUTOS))) {
          writer.write("CÓDIGO, DESCRIÇAO, VALOR");
          writer.newLine();
        }
      }
      if (!arquivoExiste(BD_ESTOQUE)) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_ESTOQUE))) {
          writer.write("CÓDIGO, DESCRIÇAO, VALOR, QUANTIDADE");
          writer.newLine();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean arquivoExiste(String nomeArquivo) {
    return new File(nomeArquivo).exists();
  }

  public void cadastrarProduto(String codigo, String descricaoProduto, Double valor) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_PRODUTOS, true))) {
      writer.write(codigo + "," + descricaoProduto + "," + valor);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao cadastrar produto ", e);
    }
  }

  public void cadastrarProdutoNoEstoque(String codigo, int quantidade) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_ESTOQUE, true))) {
      writer.write(codigo + "," + quantidade);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao cadastrar produto ", e);
    }
  }

  public boolean produtoExiste(String codigo) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(codigo)) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao verificar se o usuário existe", e);
    }
    return false;
  }

  public void visualizarProdutos() {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      reader.readLine();
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 3) {
          String codigo = dados[0];
          String descricaoProduto = dados[1];
          String valor = dados[2];
          System.out.println("CÓDIGO: " + codigo);
          System.out.println("DESCRIÇÃO: " + descricaoProduto);
          System.out.println("VALOR: " + valor);
          System.out.println("=".repeat(70));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao visualizar cadastro de usuários", e);
    }
  }
}
