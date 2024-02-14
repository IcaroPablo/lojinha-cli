package src.main.java.service;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.infrastructure.utils.FileUtils;
import src.main.java.repositories.ComprasRepositoryView;
import src.main.java.repositories.repository.ComprasRepository;
import src.main.java.rest.dtos.Carrinho;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static src.main.java.constants.Constantes.CARRINHO;
import static src.main.java.constants.Constantes.CARRINHO_CABECALHO;
import static src.main.java.constants.Constantes.QUANTIDADE_INDISPONIVEL;
import static src.main.java.infrastructure.utils.Present.print;

public class ComprasService {

  private final ComprasRepositoryView repository;
  private final ProdutoService produtoService;
  private final FileUtils fileUtils;

  public ComprasService(ComprasRepository repository, ProdutoService produtoService, FileUtils fileUtils) {
    this.repository = repository;
    this.produtoService = produtoService;
    this.fileUtils = fileUtils;
    fileUtils.createIfNotExists(CARRINHO, CARRINHO_CABECALHO);
  }

  public boolean carrinhoSalvo(String cpf) {
    return repository.carrinhoAberto(cpf);
  }

  public List<Carrinho> verCarrinho(String cpf) {
    print("=".repeat(70) +
        "\nJá existe um carrinho aberto para este CPF. \nVisualizando itens do carrinho.\n ");
    return repository.obterItensCarrinho(cpf);
  }

  public void imprimirCarrinho(List<Carrinho> carrinho) {
    repository.imprimirCarrinho(carrinho);
  }

  public void alterarItemCarrinho(List<Carrinho> carrinho, String codigo, Integer quantidade) {
    repository.alterarItemCarrinho(carrinho, codigo, quantidade);
  }

  public void salvarCarrinho(String cpf, List<Carrinho> carrinho) {
    repository.salvarCarrinho(cpf, carrinho);
  }

  public void limparCarrinho(String cpf) {
    repository.limparCarrinho(cpf);
  }

  public void emitirNotaFiscal(List<Carrinho> itens, String cpf,  String formaPagamento) {
    repository.emitirNotaFiscal(itens, cpf, formaPagamento);
  }
}
