package src.main;

import src.usuarios.Cliente;
import src.usuarios.Gerente;
import src.usuarios.UsuarioManager;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lojinha {
  public static void main(String[] args) throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    usuarioManager.createIfNotExists();

    Scanner scanner = new Scanner(System.in);

    int opcao = 0;

//    menuPrincipal();
    menuInicial();

    while (opcao != 3) {
      opcao = scanner.nextInt();
      scanner.nextLine();
      switch (opcao) {
        case 1:
          System.out.println("Opção 1 selecionada: FAZER LOGIN");
          System.out.println("Digite o CPF: ");
          String cpf = scanner.nextLine();
          if (usuarioManager.usuarioExiste(cpf)) {
            System.out.println("Digite a SENHA: ");
            String senha = scanner.nextLine();
            if (usuarioManager.login(cpf, senha)) {
//              usuarioManager.bemVindoUsuario(cpf); // não está imprimindo como deveria.
              exibirMenuGerente();
            } else {
//              usuarioManager.bemVindoUsuario(cpf); // mesmo do comentário anterior.
              exibirMenuCliente();
            }
          } else {
            System.out.printf("O usuário com o cpf %s não existe. Retornando ao menu inicial.", cpf);
            System.out.println();
            menuInicial();
          }
          break;
        case 2:
          System.out.println("Opção 2 selecionada: FAZER CADASTRO");
          System.out.println("Você deseja fazer o cadastro de cliente ou administrador? ");
          System.out.println("Digite 1 para Cliente ou 2 para Administrador");

          int tipoCadastro = scanner.nextInt();
          scanner.nextLine();

          System.out.println("Digite o CPF: ");
          cpf = scanner.nextLine();
          if (usuarioManager.usuarioExiste(cpf)) {
            System.out.println("Já existe um usuário com este cpf.");
            System.out.println("Redirecionando ao menu principal");
            menuPrincipal();
            break;
          }
          System.out.println("Digite o nome: ");
          String nome = scanner.nextLine();
          System.out.println("Digite o telefone: ");
          String telefone = scanner.nextLine();
          System.out.println("Digite sua senha de acesso: ");
          String senha = scanner.nextLine();

          if (tipoCadastro == 1) {
            Cliente cliente = new Cliente(cpf, nome, telefone);
            usuarioManager.salvarUsuario(cpf, nome, telefone, "false");
            usuarioManager.salvarDadosAcessoUsuario(cliente.getCpf(), senha, String.valueOf(false));
            System.out.println("Cadastro realizado com sucesso!");
          } else if (tipoCadastro == 2) {
            Gerente gerente = new Gerente(cpf, nome, telefone);
            usuarioManager.salvarUsuario(cpf, nome, telefone, "true");
            usuarioManager.salvarDadosAcessoUsuario(gerente.getCpf(), senha, String.valueOf(true));
            System.out.println("Cadastro realizado com sucesso!");
          }

          System.out.println("Redirecionando para o Menu Inicial");
          System.out.println("Faça login, por favor.");
          menuInicial();
          break;
      }
    }


//    while (opcao != 8) {
//
//      opcao = scanner.nextInt();
//      scanner.nextLine();
//
//      switch (opcao) {
//        case 1:
//
//        case 2:
//
//        case 3:
//          System.out.println("Opção 3 selecionada: VISUALIZAR CADASTRO");
//          // criar método para visualizar cadastro
//          break;
//        case 4:
//          System.out.println("Opção 4 selecionada: ALTERAR DADOS DE CADASTRO");
//          // alterar apenas nome, telefone e senha
//          break;
//        case 5:
//          System.out.println("Opção 5 selecionada: REMOVER CADASTRO");
//          System.out.println("Digite o seu CPF para autenticação novamente: ");
//          cpf = scanner.nextLine();
//          if (usuarioManager.isAdministrador(cpf)) {
//            System.out.println("Digite o CPF do usuário a ser excluído: ");
//            cpf = scanner.nextLine();
//            usuarioManager.removerUsuario(cpf);
//          } else {
//            System.out.println("Apenas administradores podem excluir usuários. ");
//            System.out.println("Retornando ao menu principal.");
//            menuPrincipal();
//          }
//          break;
//        case 6:
//          System.out.println("Opção 6 selecionada: NOVA COMPRA");
//
//          break;
//        case 7:
//          System.out.println("Opção 7 selecionada: VOLTAR AO MENU PRINCIPAL");
//
//          break;
//        case 8:
//          System.out.println("Opção 8 selecionada: SAIR");
//
//          break;
//        default:
//          System.out.println("Opção inválida. Tente novamente!");
//      }
//    }
    scanner.close();
  }

  public static void menuInicial() {
    System.out.print("=".repeat(9));
    System.out.print(" SEJA BEM-VINDO À LOJINHA! ");
    System.out.println("=".repeat(9));
    System.out.println(" POR FAVOR, SELECIONE UMA DAS OPÇÕES ABAIXO: ");
    System.out.println(" 1. FAZER LOGIN ");
    System.out.println(" 2. FAZER CADASTRO ");
    System.out.println(" 3. SAIR ");
    System.out.println("=".repeat(45));
  }

  public static void menuPrincipal() {
    System.out.println("SELECIONE UMA OPÇÃO: ");
    System.out.println("1. Visualizar Cadastro");
    System.out.println("2. Alterar Dados de Cadastro");
    System.out.println("3. Remover Cadastro");
    System.out.println("4. Nova Compra");
    System.out.println("5. Voltar ao menu principal");
    System.out.println("6. Sair");
  }

  public static void exibirMenuCliente() throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    menuCliente();
    int selecionar = 0;
    Scanner scanner = new Scanner(System.in);
    while (selecionar != 5) {
      selecionar = scanner.nextInt();
      scanner.nextLine();

      switch (selecionar) {
        case 1:
          System.out.println("Opção 1 selecionada: Nova Compra");
          break;
        case 2:
          System.out.println("Opção 2 selecionada: Visualizar Cadastro");
          System.out.println("Digite o CPF novamente:");
          String cpf = scanner.nextLine();
          usuarioManager.visualizarProprioCadastro(cpf);
          retornarAoMenu();
          break;
        case 3:
          System.out.println("Opção 3 selecionada: Alterar Cadastro");
          System.out.println("Digite o CPF novamente:");
          cpf = scanner.nextLine();
          usuarioManager.visualizarProprioCadastro(cpf);
          System.out.print("Digite o NOVO NOME: ");
          String novoNome = scanner.nextLine();
          System.out.print("Digite o NOVO TELEFONE: ");
          String novoTelefone = scanner.nextLine();
          usuarioManager.alterarProprioCadastro(cpf, novoNome, novoTelefone);
          retornarAoMenu();
          break;
        case 4:
          System.out.println("Opção 4 selecionada: Remover Cadastro");
          System.out.println("Tem certeza que deseja deletar o cadastro? S/N");
          String resposta = scanner.nextLine();
          if (resposta.equalsIgnoreCase("s")) {
            System.out.println("Digite o CPF para confirmar: ");
            cpf = scanner.nextLine();
            usuarioManager.removerProprioCadastro(cpf);
            System.out.println("Voltando ao menu principal.");
            selecionar = 5;
            menuInicial();
          } else if (resposta.equalsIgnoreCase("n")) {
            System.out.println("OK. Voltando ao menu de cliente");
            menuInicial();
          }
          break;
        case 5:
          System.out.println("Voltando ao menu inicial");
          menuInicial();
          break;
        default:
          System.out.println("Opção inválida. Tente novamente.");
      }
    }
  }

  public static void retornarAoMenu() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Retornar ao menu de cliente? S/N");
    String resposta = scanner.nextLine();
    if (resposta.equalsIgnoreCase("s"))
      menuCliente();
    else menuInicial();
  }

  public static void menuCliente() {
    System.out.println("Selecione uma opção: ");
    System.out.println("1. Nova Compra");
    System.out.println("2. Visualizar Cadastro");
    System.out.println("3. Alterar Cadastro");
    System.out.println("4. Remover Cadastro");
    System.out.println("5. Sair");
  }

  public static void exibirMenuGerente() {
    System.out.println("Bem-vindo!");
    System.out.println("Selecione uma opção: ");
    System.out.println("1. Cadastrar Produto");
    System.out.println("2. Cadastrar Cliente");
    System.out.println("3. Excluir Cadastro de Cliente");
    System.out.println("4. Excluir Cadastro de Produto");
    System.out.println("5. Sair");
  }
}
