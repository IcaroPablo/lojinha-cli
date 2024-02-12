package src.main.java.repositories;

public interface ProdutoRepositoryVIew {
  void cadastrarProduto(String codigo, String descricaoProduto, Double valor);

  void cadastrarProdutoNoEstoque(String codigo, int quantidade);

  boolean produtoExiste(String codigo, String fileName);

  void changeQuantity(String codigo, Integer quantidade, String method);
}
