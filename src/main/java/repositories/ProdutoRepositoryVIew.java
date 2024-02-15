package src.main.java.repositories;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.rest.dtos.ProdutoDto;

import java.io.FileNotFoundException;
import java.util.List;

public interface ProdutoRepositoryVIew {
  void cadastrarProduto(String codigo, String descricaoProduto, Double valor, int quantidade);

  boolean produtoExiste(String codigo, String fileName);

  void changeQuantity(String codigo, Integer quantidade, String method);

  List<ProdutoDto> visualizarCadastroProdutos();

  void alterarCadastroDeProduto(String codigo, String novaDescricao, Double novoValor, Integer novaQuantidade);

  void excluirProduto(String codigo);

  boolean haQuantidadeDisponivel(String codigo, int quantidade);

  ProdutoDto getProduto(String codigo);

}
