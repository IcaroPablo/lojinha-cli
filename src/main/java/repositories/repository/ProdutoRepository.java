package src.main.java.repositories.repository;

import src.main.java.repositories.ProdutoRepositoryVIew;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static src.main.java.constants.Constantes.ADICIONAR;
import static src.main.java.constants.Constantes.BD_ESTOQUE;
import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.SUBTRAIR;

public class ProdutoRepository implements ProdutoRepositoryVIew {

  @Override
  public void cadastrarProduto(String codigo, String descricaoProduto, Double valor) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_PRODUTOS, true))) {
      writer.write(codigo + "," + descricaoProduto + "," + valor);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao cadastrar produto ", e);
    }
  }

  @Override
  public void cadastrarProdutoNoEstoque(String codigo, int quantidade) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_ESTOQUE, true))) {
      writer.write(codigo + "," + quantidade);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException("Erro ao cadastrar produto ", e);
    }
  }

  @Override
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

  @Override
  public void changeQuantity(String codigo, Integer quantidade, String method) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_ESTOQUE))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(codigo)) {
          codigo = dados[0];
          Integer quantidadeEstoque = Integer.parseInt(dados[1]);
          int novaquantidade = 0;
          if (ADICIONAR.equals(method)) novaquantidade = quantidadeEstoque + quantidade;
          else if (SUBTRAIR.equals(method)) novaquantidade = quantidadeEstoque - quantidade;
          cadastrarProdutoNoEstoque(codigo, novaquantidade);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao verificar se o produto existe", e);
    }
  }
}
