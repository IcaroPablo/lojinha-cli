package src.main.java.repositories.repository;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.repositories.ProdutoRepositoryVIew;
import src.main.java.rest.dtos.ProdutoDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import static src.main.java.constants.Constantes.ADICIONAR;
import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.ERROR_CREATE_PRODUCTS;
import static src.main.java.constants.Constantes.ERROR_DELETE_PRODUCT;
import static src.main.java.constants.Constantes.ERROR_RETRIEVE_PRODUCTS;
import static src.main.java.constants.Constantes.ERROR_TEMP_FILE;
import static src.main.java.constants.Constantes.SUBSTITUIR;
import static src.main.java.constants.Constantes.SUBTRAIR;
import static src.main.java.infrastructure.utils.Present.print;

public class ProdutoRepository implements ProdutoRepositoryVIew {

  @Override
  public void cadastrarProduto(String codigo, String descricaoProduto, Double valor, int quantidade) throws BusinessException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(BD_PRODUTOS, true))) {
      writer.write(codigo + "," + descricaoProduto + "," + valor + "," + quantidade);
      writer.newLine();
    } catch (IOException e) {
      throw new BusinessException(ERROR_CREATE_PRODUCTS);
    }
  }

  @Override
  public boolean produtoExiste(String codigo, String fileName) throws BusinessException {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length > 0 && dados[0].equals(codigo)) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new BusinessException("Erro ao verificar se o produto existe");
    }
    return false;
  }

  @Override
  public void changeQuantity(String codigo, Integer quantidade, String method) throws BusinessException {
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
      throw new BusinessException("Erro ao verificar se o produto existe");
    }
  }

  @Override
  public void visualizarCadastroProdutos() throws BusinessException {
    try (BufferedReader reader = new BufferedReader(new FileReader(BD_PRODUTOS))) {
      reader.readLine();
      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 3) {
          String codigo = dados[0];
          String descricaoProduto = dados[1];
          String valor = dados[2];
          String quantidade = dados[3];
          print("CÓDIGO: " + codigo + "\nDESCRIÇÃO: " + descricaoProduto + "\nVALOR: " + valor + "\nQUANTIDADE: " + quantidade + "\n");
          print("=".repeat(70) + "\n");
        }
      }
    } catch (IOException e) {
      throw new BusinessException(ERROR_RETRIEVE_PRODUCTS);
    }
  }

  @Override
  public void alterarCadastroDeProduto(String codigo, String novaDescricao, Double novoValor, Integer novaQuantidade) throws BusinessException {
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
      throw new BusinessException("Erro ao alterar cadastro de produto");
    }

    File file = new File(BD_PRODUTOS);
    File tempFile = new File("temp.txt");
    if (tempFile.renameTo(file)) {
      print("Cadastro alterado com sucesso. \n ");
    } else {
      throw new BusinessException("Erro ao renomear o arquivo temporário.");
    }
  }

  @Override
  public void excluirProduto(String codigo) throws BusinessException {
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
      throw new BusinessException(ERROR_DELETE_PRODUCT);
    }

    File fileBD = new File(BD_PRODUTOS);
    File tempFileBD = new File("temp_bd.txt");
    if (!tempFileBD.renameTo(fileBD)) {
      throw new BusinessException(ERROR_TEMP_FILE);
    }

    print("Produto removido com sucesso.");
  }

  @Override
  public boolean haQuantidadeDisponivel(String codigo, int quantidade) throws FileNotFoundException {
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
      e.printStackTrace();
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
      e.printStackTrace();
    }
    return produtoDto;
  }
}
