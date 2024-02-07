package src.main;

import src.produto.Produto;
import src.produto.ProdutoManager;
import src.service.LojinhaService;
import src.usuarios.UsuarioManager;

import java.io.FileNotFoundException;

public class Lojinha {
  public static void main(String[] args) throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    ProdutoManager produtoManager = new ProdutoManager();
    LojinhaService lojinhaService = new LojinhaService(usuarioManager, produtoManager);
    lojinhaService.iniciar();
  }
}
