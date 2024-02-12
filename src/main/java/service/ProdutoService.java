package src.main.java.service;

import src.main.java.repositories.repository.ProdutoRepository;
import src.main.java.infrastructure.utils.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static src.main.java.constants.Constantes.BD_ESTOQUE;
import static src.main.java.constants.Constantes.BD_ESTOQUE_CABECALHO;
import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.BD_PRODUTOS_CABECALHO;

public class ProdutoService {

  private FileUtils fileUtils;
  private final ProdutoRepository repository;

  public ProdutoService(FileUtils fileUtils, ProdutoRepository repository) throws FileNotFoundException {
    this.repository = repository;
    fileUtils.createIfNotExists(BD_PRODUTOS, BD_PRODUTOS_CABECALHO);
    fileUtils.createIfNotExists(BD_ESTOQUE, BD_ESTOQUE_CABECALHO);
  }

  public void cadastrarProduto(String codigo, String descricaoProduto, Double valor) {
    repository.cadastrarProduto(codigo, descricaoProduto, valor);
  }

  public void cadastrarProdutoNoEstoque(String codigo, int quantidade) {
    repository.cadastrarProdutoNoEstoque(codigo, quantidade);
  }

  public boolean produtoExiste(String codigo, String fileName) {
    return repository.produtoExiste(codigo, fileName);
  }

  public void alterarQuantidadeProduto(String codigo, Integer quantidade, String metodo) {
    repository.changeQuantity(codigo, quantidade, metodo);
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
