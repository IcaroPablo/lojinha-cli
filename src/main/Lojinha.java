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

    menuPrincipal();

    while (opcao != 8) {

      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          System.out.println("Opção 1 selecionada: FAZER LOGIN");
          System.out.println("Digite o CPF: ");
          String cpf = scanner.nextLine();
          System.out.println("Digite a SENHA: ");
          String senha = scanner.nextLine();
          if (usuarioManager.usuarioExiste(cpf)) {
            if (usuarioManager.login(cpf, senha)) {
              usuarioManager.bemVindoUsuario(cpf);
              exibirMenuGerente();
            } else {
              usuarioManager.bemVindoUsuario(cpf);
              exibirMenuCliente();
            }
          } else {
            System.out.printf("O usuário com este cpf %s não existe. Retornando ao menu principal.", cpf);
            menuPrincipal();
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
          senha = scanner.nextLine();

          if (tipoCadastro == 1) {
            Cliente cliente = new Cliente(cpf, nome, telefone);
            usuarioManager.salvarUsuario(cpf, nome, telefone, "false");
            usuarioManager.salvarDadosAcessoUsuario(cliente.getCpf(), senha, String.valueOf(false));
          } else if (tipoCadastro == 2) {
            Gerente gerente = new Gerente(cpf, nome, telefone);
            usuarioManager.salvarUsuario(cpf, nome, telefone, "true");
            usuarioManager.salvarDadosAcessoUsuario(gerente.getCpf(), senha, String.valueOf(true));
          }

          System.out.println("Redirecionando para o Menu Principal");
          menuPrincipal();
          break;
        case 3:
          System.out.println("Opção 3 selecionada: VISUALIZAR CADASTRO");

          break;
        case 4:
          System.out.println("Opção 4 selecionada: ALTERAR DADOS DE CADASTRO");

          break;
        case 5:
          System.out.println("Opção 5 selecionada: REMOVER CADASTRO");
          System.out.println("Digite o seu CPF para autenticação novamente: ");
          cpf = scanner.nextLine();
          if (usuarioManager.isAdministrador(cpf)) {
            System.out.println("Digite o CPF do usuário a ser excluído: ");
            cpf = scanner.nextLine();
            usuarioManager.removerUsuario(cpf);
          } else {
            System.out.println("Apenas administradores podem excluir usuários. ");
            System.out.println("Retornando ao menu principal.");
            menuPrincipal();
          }
          break;
        case 6:
          System.out.println("Opção 6 selecionada: NOVA COMPRA");

          break;
        case 7:
          System.out.println("Opção 7 selecionada: VOLTAR AO MENU PRINCIPAL");

          break;
        case 8:
          System.out.println("Opção 8 selecionada: SAIR");

          break;
        default:
          System.out.println("Opção inválida. Tente novamente!");
      }
    }
    scanner.close();
  }

  public static void menuPrincipal() {
    System.out.println("SELECIONE UMA OPÇÃO: ");
    System.out.println("1. Fazer Login");
    System.out.println("2. Fazer Cadastro");
    System.out.println("3. Visualizar Cadastro");
    System.out.println("4. Alterar Dados de Cadastro");
    System.out.println("5. Remover Cadastro");
    System.out.println("6. Nova Compra");
    System.out.println("7. Sair");
  }

  public static void exibirMenuCliente() {
    menuCliente();
    int selecionar = 0;
    Scanner scanner = new Scanner(System.in);
    while (selecionar != 4) {
      selecionar = scanner.nextInt();
      scanner.nextLine();

      switch (selecionar) {
        case 1:
          System.out.println("Opção 1 selecionada: Nova Compra");

          break;
        case 2:
          System.out.println("Opção 2 selecionada: Alterar Cadastro");
          break;
        case 3:
          System.out.println("Opção 3 selecionada: Remover Cadastro");
          break;
        case 4:
          System.out.println("Saindo do menu...");
          menuPrincipal();
          break;
        default:
          System.out.println("Opção inválida. Tente novamente.");
      }
    }
  }

  public static void menuCliente() {
    System.out.println("Bem-vindo, Cliente!");
    System.out.println("Selecione uma opção: ");
    System.out.println("1. Nova Compra");
    System.out.println("2. Alterar Cadastro");
    System.out.println("3. Remover Cadastro");
    System.out.println("4. Sair");
  }

  public static void exibirMenuGerente() {
    System.out.println("Bem-vindo, Gerente!");
    System.out.println("Selecione uma opção: ");
    System.out.println("1. Cadastrar Produto");
    System.out.println("2. Cadastrar Cliente");
    System.out.println("3. Excluir Cadastro de Cliente");
    System.out.println("4. Excluir Cadastro de Produto");
    System.out.println("4. Sair");
  }
}
