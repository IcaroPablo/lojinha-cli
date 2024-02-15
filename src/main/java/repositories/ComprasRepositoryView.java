package src.main.java.repositories;

import src.main.java.rest.dtos.CarrinhoDto;

import java.util.List;

public interface ComprasRepositoryView {

  boolean carrinhoAberto(String cpf);

  List<CarrinhoDto> obterItensCarrinho(String cpf);

  void imprimirCarrinho(List<CarrinhoDto> carrinho);

  void alterarItemCarrinho(List<CarrinhoDto> carrinho, String codigo, Integer quantidade);

  void salvarCarrinho(String cpf, List<CarrinhoDto> carrinho);

  void emitirNotaFiscal(List<CarrinhoDto> itens, String cpf, String formaPagamento);

  void limparCarrinho(String cpf);


}
