package src.main.java.rest;

import src.main.java.view.Menus;

import java.io.FileNotFoundException;

public class LojinhaController {
  private final Menus menus;

  public LojinhaController(Menus menus) {
    this.menus = menus;
  }

  public void iniciar() throws FileNotFoundException {
    menus.menuInicial();
  }
}