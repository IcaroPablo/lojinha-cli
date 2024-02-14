package src.main.java.repositories.repository;

import src.main.java.repositories.ProdutoRepositoryVIew;
import src.main.java.rest.dtos.ProdutoDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static src.main.java.constants.Constantes.ADICIONAR;
import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.ERROR_CREATE_PRODUCT;
import static src.main.java.constants.Constantes.ERROR_DELETE_PRODUCT;
import static src.main.java.constants.Constantes.ERROR_PRODUCT_EXISTS;
import static src.main.java.constants.Constantes.ERROR_RETRIEVE_PRODUCTS;
import static src.main.java.constants.Constantes.ERROR_TEMP_FILE;
import static src.main.java.constants.Constantes.ERROR_UPDATE_PRODUCT;
import static src.main.java.constants.Constantes.SUBSTITUIR;
import static src.main.java.constants.Constantes.SUBTRAIR;
import static src.main.java.constants.Constantes.SUCCESS_DELETE_PRODUCT;
import static src.main.java.constants.Constantes.SUCCESS_UPDATE;
import static src.main.java.infrastructure.utils.Present.print;
import static src.main.java.infrastructure.utils.Present.printf;

public class ProdutoRepository implements ProdutoRepositoryVIew {

  @Override
  public void cadastrarProduto(String codigo, String descricaoProduto, Double valor, int quantidade) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_PRODUTOS, true))) {
      writer.write(codigo + "," + descricaoProduto + "," + valor + "," + quantidade);
      writer.newLine();
    } catch (IOException e) {
      print(ERROR_CREATE_PRODUCT);
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
      print(ERROR_PRODUCT_EXISTS);
    }
    return false;
  }

  @Override
  public void changeQuantity(String codigo, Integer quantidade, String method) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(codigo)) {
          codigo = dados[0];
          Integer quantidadeEstoque = Integer.parseInt(dados[3]);
          int novaquantidade = 0;
          if (ADICIONAR.equals(method)) novaquantidade = quantidadeEstoque + quantidade;
          else if (SUBTRAIR.equals(method)) novaquantidade = quantidadeEstoque - quantidade;
          else if (SUBSTITUIR.equals(method)) novaquantidade = quantidade;
          alterarCadastroDeProduto(codigo, null, null, novaquantidade);
        }
      }
    } catch (IOException e) {
      print(ERROR_PRODUCT_EXISTS);
    }
  }

  @SuppressWarnings("java:S2677")
  @Override
  public List<ProdutoDto> visualizarCadastroProdutos() {
    List<ProdutoDto> produtos = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      reader.readLine();
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 3) {
          String codigo = dados[0];
          String descricaoProduto = dados[1];
          Double valor = Double.valueOf(dados[2]);
          int quantidade = Integer.parseInt(dados[3]);
          ProdutoDto produto = new ProdutoDto(codigo, descricaoProduto, valor, quantidade);
          produtos.add(produto);
        }
      }
    } catch (IOException e) {
      print(ERROR_RETRIEVE_PRODUCTS);
    }
    return produtos;
  }

  @Override
  public void alterarCadastroDeProduto(String codigo, String novaDescricao, Double novoValor, Integer novaQuantidade) {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4 && codigo.equals(dados[0])) {
          if (novaDescricao != null) {
            dados[1] = novaDescricao;
          }
          if (novoValor != null) {
            dados[2] = String.format("%.2f", novoValor);
          }
          if (novaQuantidade != null) {
            dados[3] = String.valueOf(novaQuantidade);
          }
        }
        writer.write(String.join(",", dados));
        writer.newLine();
      }
    } catch (IOException e) {
      print(ERROR_UPDATE_PRODUCT);
    }

    File file = new File(BD_PRODUTOS);
    File tempFile = new File("temp.txt");
    if (tempFile.renameTo(file)) {
      print(SUCCESS_UPDATE);
    } else {
      print(ERROR_TEMP_FILE);
    }
  }

  @Override
  public void excluirProduto(String codigo){
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp_bd.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4 && codigo.equals(dados[0].trim())) {
          continue;
        }
        writer.write(linha);
        writer.newLine();
      }
    } catch (IOException e) {
      print(ERROR_DELETE_PRODUCT);
    }

    File fileBD = new File(BD_PRODUTOS);
    File tempFileBD = new File("temp_bd.txt");
    if (!tempFileBD.renameTo(fileBD)) {
      print(ERROR_TEMP_FILE);
    }

    print(SUCCESS_DELETE_PRODUCT);
  }

  @Override
  public boolean haQuantidadeDisponivel(String codigo, int quantidade) {
    try(BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 4 && codigo.equals(dados[0])) {
          int quantidadeDisponivel = Integer.parseInt(dados[3]);
          return quantidadeDisponivel >= quantidade;
        }
      }
    } catch (IOException e) {
      printf("Exception: %s", e.getMessage());
    }
    return false;
  }

  @Override
  public ProdutoDto getProduto(String codigo) {
    ProdutoDto produtoDto = null;
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] dados = line.split(",");
        if (dados[0].equals(codigo)) {
          String descricao = dados[1];
          double valor = Double.parseDouble(dados[2]);
          int quantidade = Integer.parseInt(dados[3]);
          produtoDto = new ProdutoDto(codigo, descricao, valor, quantidade);
          break;
        }
      }
    } catch (IOException e) {
      printf("Exception: %s", e.getMessage());
    }
    return produtoDto;
  }
}
