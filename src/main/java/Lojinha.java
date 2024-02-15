package src.main.java;

import src.main.java.infrastructure.utils.Valid;
import src.main.java.repositories.repository.ComprasRepository;
import src.main.java.repositories.repository.ProdutoRepository;
import src.main.java.repositories.repository.UserRepository;
import src.main.java.service.ComprasService;
import src.main.java.service.ProdutoService;
import src.main.java.rest.LojinhaController;
import src.main.java.service.UsuarioService;
import src.main.java.infrastructure.utils.FileUtils;
import src.main.java.view.Menus;

import java.io.FileNotFoundException;

public class Lojinha {
  public static void main(String[] args) throws FileNotFoundException {
    UsuarioService usuarioService = new UsuarioService(new FileUtils(), new UserRepository(), new Valid());
    ProdutoService produtoService = new ProdutoService(new FileUtils(), new ProdutoRepository());
    ComprasService comprasService = new ComprasService(new ComprasRepository(), produtoService, new FileUtils());
    Menus menus = new Menus(usuarioService, produtoService, comprasService, new Valid());
    LojinhaController lojinhaController = new LojinhaController(menus);
    lojinhaController.iniciar();
  }
}
