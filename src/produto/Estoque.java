package src.produto;

import java.util.Map;

public record Estoque(Map<Produto, Integer> produtos) {

  private static final String ESTOQUE = "estoque.csv";


}
