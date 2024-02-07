package src.main.java.service;

import src.main.java.usuarios.UsuarioManager;

public class Menus {

  private final UsuarioManager usuarioManager;

  public Menus(UsuarioManager usuarioManager) {
    this.usuarioManager = usuarioManager;
  }

  public static void menuInicial() {
    System.out.print("=".repeat(9));
    System.out.print(" SEJA BEM-VINDO À LOJINHA! ");
    System.out.println("=".repeat(9));
    System.out.println(" POR FAVOR, SELECIONE UMA DAS OPÇÕES ABAIXO: ");
    System.out.println(" 1. FAZER LOGIN ");
    System.out.println(" 2. FAZER CADASTRO ");
    System.out.println(" 3. FAZER LOGIN COMO ADMINISTRADOR ");
    System.out.println(" 4. SAIR ");
    System.out.println("=".repeat(45));
    System.out.print("DIGITE SUA OPÇÃO: ");
  }

  public static void menuCliente() {
    System.out.println("Selecione uma opção: ");
    System.out.println("1. Nova Compra");
    System.out.println("2. Visualizar Cadastro");
    System.out.println("3. Alterar Cadastro");
    System.out.println("4. Remover Cadastro");
    System.out.println("5. Sair");
  }

  public static void menuGerente() {
    System.out.println("Selecione uma opção: ");
    System.out.println("1. Cadastrar Cliente");
    System.out.println("2. Ver cadastros de clientes");
    System.out.println("3. Alterar cadastro de cliente");
    System.out.println("4. Excluir Cadastro de Cliente");
    System.out.println("5. Cadastrar Produto"); // para implementar depois (usando a quantidade do estoque)
    System.out.println("5. Visualizar cadastro de Produto");
    System.out.println("6. Alterar Cadastro de Produto");
    System.out.println("7. Alterar Preço de Produto");
    System.out.println("8. Inserir Produto no Estoque");
    System.out.println("9. Ver Estoque");
    System.out.println("10. Excluir Cadastro de Produto");
    System.out.println("11. Sair");
    System.out.println("=".repeat(50));
    System.out.print("OPÇÃO: ");
  }
}