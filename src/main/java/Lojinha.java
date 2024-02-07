package src.main.java;

import src.main.java.produto.ProdutoManager;
import src.main.java.service.LojinhaService;
import src.main.java.usuarios.UsuarioManager;

import java.io.FileNotFoundException;

public class Lojinha {
  public static void main(String[] args) throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    ProdutoManager produtoManager = new ProdutoManager();
    LojinhaService lojinhaService = new LojinhaService(usuarioManager, produtoManager);
    lojinhaService.iniciar();
  }
}
