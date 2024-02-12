package src.main.java;

import src.main.java.repositories.repository.ProdutoRepository;
import src.main.java.repositories.repository.UserRepository;
import src.main.java.service.ProdutoService;
import src.main.java.rest.LojinhaController;
import src.main.java.service.UsuarioService;
import src.main.java.infrastructure.utils.FileUtils;
import src.main.java.view.Menus;

import java.io.FileNotFoundException;

public class Lojinha {
  public static void main(String[] args) throws FileNotFoundException {
    UsuarioService usuarioService = new UsuarioService(new FileUtils(), new UserRepository());
    ProdutoService produtoService = new ProdutoService(new FileUtils(), new ProdutoRepository());
    LojinhaController lojinhaController = new LojinhaController(new Menus());
    Menus menus = new Menus(usuarioService, produtoService, lojinhaController);
    lojinhaController = new LojinhaController(menus);
    lojinhaController.iniciar();
  }
}
