package src.main;

import src.service.LojinhaService;
import src.usuarios.UsuarioManager;

import java.io.FileNotFoundException;

public class Lojinha {
  public static void main(String[] args) throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    LojinhaService lojinhaService = new LojinhaService(usuarioManager);
    lojinhaService.iniciar();
  }
}
