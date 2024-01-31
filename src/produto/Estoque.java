package src.produto;

import java.util.LinkedHashMap;
import java.util.Map;

public record Estoque(Map<Produto, Integer> produtos) {
  public Estoque() {
    this(new LinkedHashMap<>());
  }

  public void adicionarQuantidadeProduto(Produto produto, int qtd) {
    produtos.put(produto, qtd);
  }

  public void diminuirQuantidadeProduto(Produto produto, int qtd) {
    produtos.remove(produto, produtos.get(produto) - qtd);
  }

  public void listaDeProdutos() {
    printAsteriscos();
    System.out.println("Estoque: ");
    System.out.println("==== Código ==== Descrição ==== Valor ==== Quantidade ====");
    estoque();
    printAsteriscos();
  }

  public void printAsteriscos() {
    int num = 60;

    for (int i = 0; i < num; i++)
      System.out.print("*");

    System.out.println();
  }

  public void estoque() {
    for (Produto produto : produtos.keySet()) {
      System.out.println(String.format("%d, %s, %.2f, %s", produto.id(), produto.nomeProduto(), produto.preco(), produtos.get(produto)));
    }
  }

}
