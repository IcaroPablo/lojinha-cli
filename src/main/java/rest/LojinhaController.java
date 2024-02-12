package src.main.java.rest;

import src.main.java.view.Menus;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class LojinhaController {
  private final Menus menus;

  public LojinhaController(Menus menus) {
    this.menus = menus;
  }

  public void iniciar() {
    Scanner scanner = new Scanner(System.in);

    menus.menuInicial();
    int opcao = scanner.nextInt();

    while (opcao != 4) {
      scanner.nextLine();

      switch (opcao) {
        case 1:
          menus.loginUsuario();
          break;
        case 2:
          menus.fazerCadastro();
          break;
        case 3:
          menus.loginAdministrador();
          break;
        default:
          return;
      }
    }
    scanner.close();
  }
}