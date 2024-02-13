package src.main.java.repositories;

import src.main.java.rest.dtos.Carrinho;

import java.util.List;
import java.util.Map;

public interface ComprasRepositoryView {

  boolean carrinhoAberto(String cpf);

//  void abrirCarrinho(String cpf);
//
//  void adicionarItem(String cpf, String codigo, int quantidade);

  List<Carrinho> obterItensCarrinho(String cpf);

  void imprimirCarrinho(List<Carrinho> carrinho);
  void alterarItemCarrinho(List<Carrinho> carrinho, String codigo, Integer quantidade);

  void salvarCarrinho(String cpf, List<Carrinho> carrinho);

  void emitirNotaFiscal(List<Carrinho> itens, String cpf, String formaPagamento);

  void limparCarrinho(String cpf);


}
