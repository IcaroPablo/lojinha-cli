package src.main.java.repositories;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.rest.dtos.ProdutoDto;

import java.io.FileNotFoundException;

public interface ProdutoRepositoryVIew {
  void cadastrarProduto(String codigo, String descricaoProduto, Double valor, int quantidade) throws BusinessException;

//  void cadastrarProdutoNoEstoque(String codigo, int quantidade);

  boolean produtoExiste(String codigo, String fileName) throws BusinessException;

  void changeQuantity(String codigo, Integer quantidade, String method) throws BusinessException;

  void visualizarCadastroProdutos() throws BusinessException;

  void alterarCadastroDeProduto(String codigo, String novaDescricao, Double novoValor, Integer novaQuantidade) throws BusinessException;

  void excluirProduto(String codigo);

  boolean haQuantidadeDisponivel(String codigo, int quantidade) throws FileNotFoundException;

  ProdutoDto getProduto(String codigo);

}
