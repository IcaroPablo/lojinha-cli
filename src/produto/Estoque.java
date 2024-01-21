package src.produto;

import java.util.LinkedHashMap;
import java.util.Map;

public record Estoque(Map<Produto, Integer> produtos) {

  //O Map
  public Estoque() {
    this(new LinkedHashMap<>());
  }

  public void adicionarQuantidadeProduto(Produto produto, int qtd) {
    produtos.put(produto, qtd);
  }

  public void diminuirQuantidadeProduto(Produto produto, int qtd) {
    produtos.remove(produto, produtos.get(produto) - qtd);
  }

}
