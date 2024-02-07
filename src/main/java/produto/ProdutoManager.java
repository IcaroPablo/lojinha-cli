package src.main.java.produto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static src.main.java.constants.Constantes.BD_ESTOQUE;
import static src.main.java.constants.Constantes.BD_PRODUTOS;

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
          writer.write("CÓDIGO, QUANTIDADE");
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

  public boolean produtoExiste(String codigo, String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(codigo)) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao verificar se o produto existe", e);
    }
    return false;
  }

  public void adicionarQuantidadeProduto(String codigo, Integer quantidade) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_ESTOQUE))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(codigo)) {
          codigo = dados[0];
          Integer quantidadeEstoque = Integer.parseInt(dados[1]);
          int novaquantidade = quantidadeEstoque + quantidade;
          cadastrarProdutoNoEstoque(codigo, novaquantidade);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao verificar se o produto existe", e);
    }
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

  public void alterarCadastroDeProduto(String codigo, String novaDescricao, Double novoValor) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 3 && codigo.equals(dados[0])) {
          dados[1] = novaDescricao;
          dados[2] = String.valueOf(novoValor);
        }
        writer.write(String.join(",", dados));
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao alterar cadastro de produto", e);
    }

    File file = new File(BD_PRODUTOS);
    File tempFile = new File("temp.txt");
    if (tempFile.renameTo(file)) {
      System.out.println("Cadastro alterado com sucesso.");
    } else {
      throw new RuntimeException("Erro ao renomear o arquivo temporário.");
    }
  }
}
