package src.main.java.repositories.repository;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.repositories.ComprasRepositoryView;
import src.main.java.rest.dtos.CarrinhoDto;
import src.main.java.rest.dtos.ClienteDto;
import src.main.java.service.UsuarioService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static src.main.java.constants.Constantes.CARRINHO;
import static src.main.java.constants.Constantes.CARTAO;
import static src.main.java.constants.Constantes.DINHEIRO;
import static src.main.java.constants.Constantes.ERROR_TEMP_FILE;
import static src.main.java.infrastructure.utils.Present.print;
import static src.main.java.infrastructure.utils.Present.printf;

public class ComprasRepository implements ComprasRepositoryView {

  public boolean carrinhoAberto(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(CARRINHO))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length >= 2 && parts[0].equals(cpf) && parts[1].equals("aberto")) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public List<CarrinhoDto> obterItensCarrinho(String cpf) {
    List<CarrinhoDto> carrinhoItens = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(CARRINHO))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length >= 3 && parts[0].equals(cpf) && parts[1].equals("aberto")) {
          String[] itemParts = parts[2].split(",");
          for (String itemPart : itemParts) {
            String[] itemValues = itemPart.split(":");
            String codigo = itemValues[0];
            String descricao = itemValues[1];
            double valor = Double.parseDouble(itemValues[2]);
            int quantidade = Integer.parseInt(itemValues[3]);
            carrinhoItens.add(new CarrinhoDto(codigo, descricao, valor, quantidade));
          }
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return carrinhoItens;
  }

  public void imprimirCarrinho(List<CarrinhoDto> itensCarrinho) {
    if(itensCarrinho.isEmpty()) {
      print("O carrinho está vazio \n");
    } else {
      printf("Itens do carrinho: \n");
      printf("%-10s | %-20s | %-10s | %-5s | %-12s |%n",
          "CÓDIGO", "DESCRIÇÃO", "VALOR", "QTD", "SUBTOTAL");
      for (CarrinhoDto item : itensCarrinho) {
        double subtotal = item.getValor() * item.getQuantidade();
        printf("%-10s | %-20s | R$ %-7.2f | %-5d | R$ %-9.2f |%n",
            item.getCodigo(), item.getDescricao(), item.getValor(), item.getQuantidade(), subtotal);
      }
      print("=".repeat(70) + "\n");
    }
  }

  @Override
  public void alterarItemCarrinho(List<CarrinhoDto> carrinho, String codigo, Integer quantidade) {
    if(carrinho.isEmpty()) {
      print("O carrinho está vazio");
      return;
    }
    for (int i = 0; i < carrinho.size(); i++) {
      CarrinhoDto item = carrinho.get(i);
      if (item.getCodigo().equals(codigo)) {
        if (quantidade == 0) {
          carrinho.remove(i);
          i--;
        } else {
          item.setQuantidade(quantidade);
        }
      }
    }
  }

  public void salvarCarrinho(String cpf, List<CarrinhoDto> itensCarrinho) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(CARRINHO, true))) {
      StringBuilder linha = new StringBuilder(cpf + ",aberto");
      for (CarrinhoDto item : itensCarrinho) {
        linha.append(",").append(item.getCodigo()).append(":")
            .append(item.getDescricao()).append(":")
            .append(item.getValor()).append(":")
            .append(item.getQuantidade());
      }
      writer.write(linha.toString());
      writer.newLine();
      print("Carrinho salvo com sucesso. \n");
    } catch (IOException e) {
      printf("Exception: %s", e.getMessage());
    }
  }

  @Override
  public void limparCarrinho(String cpf) {
    try (BufferedReader reader = new BufferedReader(new FileReader(CARRINHO));
         BufferedWriter writer = new BufferedWriter(new FileWriter("temp_carrinho.txt"))) {

      String linha;
      while ((linha = reader.readLine()) != null) {
        String[] dados = linha.split(",");
        if (dados.length >= 3 && cpf.equals(dados[0].trim()) && "aberto".equals(dados[1].trim())) {
          continue;
        }
        writer.write(linha);
        writer.newLine();
      }
    } catch (IOException e) {
      printf("Exception: %s", e.getMessage());
    }

    File fileCarrinho = new File(CARRINHO);
    File tempFileCarrinho = new File("temp_carrinho.txt");
    if (!tempFileCarrinho.renameTo(fileCarrinho)) {
      print(ERROR_TEMP_FILE);
    }

    print("Carrinho limpo com sucesso. \n");
  }

  @Override
  public void emitirNotaFiscal(List<CarrinhoDto> carrinho, String cpf, String formaPagamento) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    ClienteDto cliente = UsuarioService.recuperarCliente(cpf);
    String nomeArquivo = "nota_fiscal_CPF_" + cpf + "_" + dateFormat.format(new Date()) + ".txt";
    String nf = "NF_CPF" + cpf + "_" + dateFormat.format(new Date());
    double total = calcularTotal(carrinho, formaPagamento);
    double desconto = total * 0.1;
    double totalComDesconto = total - desconto;

    print("=".repeat(70) +
        "\nNOTA FISCAL: " + nf + "\n" +
        "DATA: " + LocalDate.now() + "\n" +
        "CPF: " + ClienteDto.cpfFormatter(cpf) + "\n" +
        "NOME: " + cliente.getNome() + "\n" +
        "TELEFONE: " + ClienteDto.telefoneFormatter(cliente.getTelefone()) + "\n" +
        "FORMA DE PAGAMENTO: " + formaPagamento.toUpperCase() + "\n" +
        "=".repeat(31) + " Itens " + "=".repeat(32).concat("\n") +
        "=".repeat(70) + "\n");
    printf("%-10s | %-20s | %-8s | %-10s | %-10s |%n",
        "CÓDIGO", "DESCRIÇÃO", "R$ UNIT", "QUANTIDADE", "R$ TOTAL");
    for (CarrinhoDto item : carrinho) {
      printf("%-10s | %-20s | R$ %-6.2f | %-10d | R$ %-7.2f |%n",
          item.getCodigo(), item.getDescricao(), item.getValor(),
          item.getQuantidade(), (item.getValor()*item.getQuantidade()));
    }
    print("=".repeat(70) + "\n");
    if (DINHEIRO.equals(formaPagamento)) {
      printf("%-25s | R$ %-7.2f%n", "VALOR DO DESCONTO: ", desconto);
      printf("%-25s | R$ %-7.2f%n", "TOTAL COM DESCONTO: ", totalComDesconto);
    } else if (CARTAO.equals(formaPagamento)){
      printf("%-25s | R$ %-10.2f%n", "TOTAL:", total);
    }

    print("=".repeat(70) + "\n");

    try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
      writer.printf("%-10" + "s%n", "Nota fiscal: " + nf);
      writer.println("DATA: " + LocalDate.now());
      writer.println("CPF: " + ClienteDto.cpfFormatter(cpf));
      writer.println("NOME: " + cliente.getNome());
      writer.println("TELEFONE: " + ClienteDto.telefoneFormatter(cliente.getTelefone()));
      writer.println("FORMA DE PAGAMENTO: " + formaPagamento.toUpperCase());
      writer.println("=".repeat(31).concat("Itens").concat("=".repeat(31)).concat("\n"));
      writer.println("=".repeat(70));
      writer.printf("%-10s | %-20s | %-8s | %-10s | %-10s |%n",
          "CÓDIGO", "DESCRIÇÃO", "R$ UNIT", "QUANTIDADE", "R$ TOTAL");

      for (CarrinhoDto item : carrinho) {
        writer.printf("%-10s | %-20s | R$ %-6.2f | %-10d | R$ %-7.2f |%n",
            item.getCodigo(), item.getDescricao(), item.getValor(), item.getQuantidade(), (item.getValor() * item.getQuantidade()));
      }
      writer.println("=".repeat(70));
      if (DINHEIRO.equals(formaPagamento)) {
        writer.printf("%-25s | R$ %-10.2f%n", "VALOR DO DESCONTO:", desconto);
        writer.printf("%-25s | R$ %-10.2f%n", "TOTAL COM DESCONTO:", totalComDesconto);
      } else if (CARTAO.equals(formaPagamento)){
        writer.printf("%-25s | R$ %-10.2f%n", "TOTAL:", total);
      }

      print("Nota fiscal emitida com sucesso.\nArquivo gerado: " + nomeArquivo + "\n");
    } catch (IOException e) {
      printf("Exception: %s", e.getMessage());
    }
  }

  private double calcularTotal(List<CarrinhoDto> carrinho, String formaPagamento) {
    double valorTotal = 0.0;
    for (CarrinhoDto item : carrinho) {
      valorTotal += item.getValor() * item.getQuantidade();
    }
    if ("DINHEIRO".equals(formaPagamento)) {
      valorTotal *= 0.9;
    }
    return valorTotal;
  }
}
