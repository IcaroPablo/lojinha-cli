package src.main.java.view;

import src.main.java.entities.Cliente;
import src.main.java.entities.Gerente;
import src.main.java.rest.LojinhaController;
import src.main.java.service.ProdutoService;
import src.main.java.service.UsuarioService;

import java.util.Objects;
import java.util.Scanner;

import static src.main.java.constants.Constantes.ADICIONAR;
import static src.main.java.constants.Constantes.BD_ESTOQUE;
import static src.main.java.constants.Constantes.BD_GERENTES;
import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.BD_USUARIOS;
import static src.main.java.constants.Constantes.CLIENTE;
import static src.main.java.constants.Constantes.GERENTE;
import static src.main.java.constants.Constantes.LOGIN_GERENTES;
import static src.main.java.constants.Constantes.LOGIN_USUARIOS;
import static src.main.java.constants.Constantes.SAINDO;
import static src.main.java.infrastructure.utils.Present.print;
import static src.main.java.infrastructure.utils.Present.printf;
import static src.main.java.infrastructure.utils.Present.println;

public class Menus {
  private final UsuarioService usuarioService;
  private final ProdutoService produtoService;
  private final LojinhaController lojinhaController;

  public Menus() {
    this.lojinhaController = null;
    this.usuarioService = null;
    this.produtoService = null;
  }
  public Menus(UsuarioService usuarioService, ProdutoService produtoService, LojinhaController lojinhaController) {
    this.usuarioService = usuarioService;
    this.produtoService = produtoService;
    this.lojinhaController = lojinhaController;
  }
  static Scanner scanner = new Scanner(System.in);

  public void menuInicial() {
    print("=".repeat(9) +
        " SEJA BEM-VINDO À LOJINHA! " +
        "=".repeat(9) + "\n" +
        " POR FAVOR, SELECIONE UMA DAS OPÇÕES ABAIXO: \n" +
        " 1. FAZER LOGIN " + "\n" +
        " 2. FAZER CADASTRO " + "\n" +
        " 3. FAZER LOGIN COMO ADMINISTRADOR " + "\n" +
        " 4. SAIR " + "\n" +
        "=".repeat(45) + "\n" +
        "DIGITE SUA OPÇÃO: ");
    int option = scanner.nextInt();
    while (option != 4) {
      switch (option) {
        case 1 -> loginUsuario();
        case 2 -> fazerCadastro();
        case 3 -> loginAdministrador();
        default -> {
          print("Escolha uma opção válida \n");
          menuInicial();
          return;
        }
      }
    }
    print(SAINDO);
    System.exit(0);
  }

  public void loginUsuario() {
    print("Opção 1 selecionada: FAZER LOGIN DE CLIENTE \nDigite o CPF: ");
    String cpf = scanner.next();
    scanner.nextLine();
    if (usuarioService.usuarioExiste(cpf, BD_USUARIOS)) {
      print("Digite a SENHA: ");
      String senha = scanner.nextLine();
      if (usuarioService.login(cpf, senha, LOGIN_USUARIOS)) {
        usuarioService.bemVindoUsuario(cpf, BD_USUARIOS);
        menuCliente();
      } else {
        println("Não foi possível fazer login. Por favor tente novamente.");
        menuInicial();
      }
    } else {
      printf("O usuário com o CPF %s não existe. Retornando ao menu inicial. \n", cpf);
      menuInicial();
    }
  }

  public void fazerCadastro(){
    print("Opção 2 selecionada: FAZER CADASTRO \n " +
        "Você deseja fazer o cadastro de cliente ou administrador? \n " +
        "Digite 1 para Cliente ou 2 para Administrador: ");
    int tipoCadastro = scanner.nextInt();
    scanner.nextLine();

    if (tipoCadastro == 1) {
      print("Digite o CPF: ");
      String cpf = scanner.next();
      if (usuarioService.usuarioExiste(cpf, BD_USUARIOS)) {
        System.out.println("Já existe um usuário com este CPF.");
        System.out.println("Redirecionando ao menu principal");
        menuInicial();
        return;
      }
      print("Digite o nome: ");
      String nome = scanner.nextLine();
      scanner.nextLine();
      print("Digite o telefone: ");
      String telefone = scanner.next();
      print("Digite sua senha de acesso: ");
      String senha = scanner.next();
      Cliente cliente = new Cliente(cpf, nome, telefone);
      usuarioService.salvarUsuario(cpf, nome, telefone, CLIENTE, BD_USUARIOS);
      usuarioService.salvarDadosAcessoUsuario(cliente.getCpf(), senha, CLIENTE, LOGIN_USUARIOS);
      print("Cadastro realizado com sucesso! \n");

    } else if (tipoCadastro == 2) {
      print("Digite o CPF: ");
      String cpf = scanner.next();
      scanner.nextLine();
      if (usuarioService.usuarioExiste(cpf, BD_GERENTES)) {
        print("Já existe um administrador com este CPF. \n Redirecionando ao menu principal \n");
        menuInicial();
        return;
      }
      print("Digite o nome: ");
      String nome = scanner.nextLine();
      print("Digite o telefone: ");
      String telefone = scanner.next();
      print("Digite sua senha de acesso: ");
      String senha = scanner.next();
      Gerente gerente = new Gerente(cpf, nome, telefone);
      usuarioService.salvarUsuario(cpf, nome, telefone, GERENTE, BD_GERENTES);
      usuarioService.salvarDadosAcessoUsuario(gerente.getCpf(), senha, GERENTE, LOGIN_GERENTES);
      println("Cadastro realizado com sucesso!");
    }

    print("Redirecionando para o Menu Inicial \n Faça login, por favor.");
    menuInicial();
  }

  public void loginAdministrador() {
    print("Opção 3 selecionada: FAZER LOGIN DE GERENTE \n Digite o CPF: ");
    String cpf = scanner.next();
    scanner.nextLine();
    if (usuarioService.usuarioExiste(cpf, BD_GERENTES)) {
      print("Digite a SENHA: ");
      String senha = scanner.nextLine();
      if (usuarioService.login(cpf, senha, LOGIN_GERENTES)) {
        usuarioService.bemVindoUsuario(cpf, BD_GERENTES);
        exibirMenuGerente();
      } else {
        println("Não foi possível fazer login. Por favor tente novamente.");
        return;
      }
    } else {
      printf("O usuário com o CPF %s não existe. Retornando ao menu inicial. \n", cpf);
      menuInicial();
    }
  }

  public void menuCliente() {
    print("Selecione uma opção: \n " +
            "1. Nova Compra \n " +
            "2. Visualizar Cadastro \n " +
            "3. Alterar Cadastro \n " +
            "4. Remover Cadastro \n " +
            "5. Sair \n " +
            "DIGITE SUA OPÇÃO: ");
    int option = scanner.nextInt();
    while (option != 5) {
      switch (option) {
        case 1 -> novaCompra();
        case 2 -> visualizarCadastro();
        case 3 -> alterarCadastro();
        case 4 -> removerCadastro();
        default -> print("Escolha uma opção válida");
      }
    }
    print(SAINDO);
    menuInicial();
  }

  private void removerCadastro() {
    print("Opção 4 selecionada: Remover Cadastro \n Tem certeza que deseja deletar o cadastro? S/N: ");
    String resposta = scanner.next();
    if (resposta.equalsIgnoreCase("s")) {
      print("Digite o CPF para confirmar: ");
      String cpf = scanner.next();
      usuarioService.excluirCadastro(cpf);
      println("Voltando ao menu principal.");
      menuInicial();
      scanner.close();
    } else if (resposta.equalsIgnoreCase("n")) {
      println("OK. Voltando ao menu de cliente");
      menuInicial();
      scanner.close();
    }
  }

  private void alterarCadastro() {
    print("Opção 3 selecionada: Alterar Cadastro \n Digite o CPF novamente: ");
    String cpf = scanner.next();
    scanner.nextLine();
    usuarioService.verCadastro(cpf, BD_USUARIOS);
    println("NOVO NOME: ");
    String novoNome = scanner.nextLine();
    println("NOVO TELEFONE: ");
    String novoTelefone = scanner.nextLine();
    usuarioService.alterarCadastro(cpf, novoNome, novoTelefone);
    retornarAoMenu(CLIENTE);
  }

  private void visualizarCadastro() {
    print("Opção 2 selecionada: Visualizar Cadastro \n " +
        "Digite o CPF novamente: ");
    String cpf = scanner.next();
    usuarioService.verCadastro(cpf, BD_USUARIOS);
    retornarAoMenu(CLIENTE);
  }

  private static void novaCompra() {
  }

  public static void menuGerente() {
    print("Selecione uma opção: \n " +
        " 1. Cadastrar Cliente \n " +
        " 2. Ver cadastros de clientes \n " +
        " 3. Alterar cadastro de cliente \n " +
        " 4. Excluir Cadastro de Cliente \n " +
        " 5. Cadastrar Produto \n " +
        " 5. Visualizar cadastro de Produto \n " +
        " 6. Alterar Cadastro de Produto \n " +
        " 7. Alterar Preço de Produto \n " +
        " 8. Inserir Produto no Estoque \n " +
        " 9. Ver Estoque \n " +
        " 10. Excluir Cadastro de Produto \n " +
        " 11. Sair" + "\n " +
        "=".repeat(50));
    print("\n OPÇÃO: ");
  }

  public void exibirMenuGerente() {
    menuGerente();
    int selecionar = 0;
    while (selecionar != 8) {
      selecionar = scanner.nextInt();
      switch (selecionar) {
        case 1:
          println("Opção 1 selecionada: Cadastrar Cliente");
          print("Digite o CPF do cliente: ");
          String cpf = scanner.next();
          scanner.nextLine();
          if (usuarioService.usuarioExiste(cpf, BD_USUARIOS)) {
            println("Já existe um cliente com este CPF.");
            println("Redirecionando ao menu");
            menuGerente();
            return;
          }
          print("Digite o NOME do cliente: ");
          String nome = scanner.next();
          print("Digite o TELEFONE do cliente: ");
          String telefone = scanner.next();
          print("Digite a SENHA DE ACESSO do cliente: ");
          String senha = scanner.next();
          Cliente cliente = new Cliente(cpf, nome, telefone);
          usuarioService.salvarUsuario(cpf, nome, telefone, CLIENTE, BD_USUARIOS);
          usuarioService.salvarDadosAcessoUsuario(cliente.getCpf(), senha, CLIENTE, LOGIN_USUARIOS);
          System.out.println("Cadastro realizado com sucesso!");
          System.out.println("Retornando ao menu");
          menuGerente();
          break;
        case 2:
          System.out.println("Opção 2 selecionada: Ver cadastros de clientes");
          System.out.println("=".repeat(70));
          usuarioService.recuperarCadastros();
          retornarAoMenu(GERENTE);
          break;
        case 3:
          System.out.println("Opção 3 selecionada: Alterar cadastro de cliente");
          System.out.println("Listando primeiro os cadastros dos clientes para escolha.");
          System.out.println("=".repeat(70));
          usuarioService.recuperarCadastros();
          print("Digite o CPF do CLIENTE que deseja alterar: ");
          cpf = scanner.next();
          scanner.nextLine();
          System.out.println("DIGITE O NOVO NOME: ");
          String novoNome = scanner.nextLine();
          System.out.println("DIGITE O NOVO TELEFONE: ");
          String novoTelefone = scanner.nextLine();
          usuarioService.alterarCadastro(cpf, novoNome, novoTelefone);
          retornarAoMenu(GERENTE);
          break;
        case 4:
          System.out.println("Opção 4 selecionada: Excluir Cadastro de Cliente");
          System.out.println("=".repeat(70));
          usuarioService.recuperarCadastros();
          print("Digite o CPF do cliente que deseja excluir: ");
          cpf = scanner.next();
          scanner.nextLine();
          usuarioService.excluirCadastro(cpf);
          System.out.println("Voltando ao menu.");
          menuGerente();
          break;
        case 5:
          System.out.println("Opção 5 selecionada: Cadastrar Produto");
          print("Digite o código do produto: ");
          String codigo = scanner.next();
          scanner.nextLine();
          if (produtoService.produtoExiste(codigo, BD_PRODUTOS)) {
            System.out.println("Já existe um produto com este código");
            System.out.println("Redirecionando ao menu principal");
            menuGerente();
            return;
          }
          print("Digite a descrição do produto: ");
          String descricaoProduto = scanner.nextLine();
          print("Digite o valor do produto: ");
          Double valor = scanner.nextDouble();
          scanner.nextLine();
          produtoService.cadastrarProduto(codigo, descricaoProduto, valor);
          retornarAoMenu(GERENTE);
          break;
        case 6:
          System.out.println("Opção 6 selecionada: Alterar cadastro de produto");
          System.out.println("=".repeat(70));
          produtoService.visualizarProdutos();
          print("Digite o código do produto: ");
          codigo = scanner.next();
          print("Digite a nova descrição: ");
          String novaDescricao = scanner.next();
          System.out.println("Digite o novo valor: ");
          Double novoValor = scanner.nextDouble();
          produtoService.alterarCadastroDeProduto(codigo, novaDescricao, novoValor);
          retornarAoMenu(GERENTE);
          break;
        case 7:
          System.out.println("Opção 7 selecionada: Alterar preço de produto");
          break;
        case 8:
          System.out.println("Opção 8 selecionada: Inserir produto no estoque");
          System.out.println("=".repeat(70));
          produtoService.visualizarProdutos();
          print("Digite o código do produto: ");
          codigo = scanner.next();
          print("Digite a quantidade do produto no estoque: ");
          Integer quantidade = scanner.nextInt();
          if (produtoService.produtoExiste(codigo, BD_ESTOQUE)) produtoService.alterarQuantidadeProduto(codigo, quantidade, ADICIONAR);
          else produtoService.cadastrarProdutoNoEstoque(codigo, quantidade);
          // também ver funcionalidade de alterar o valor de um produto.
          retornarAoMenu(GERENTE);
          break;
        case 9:
          System.out.println("Opção 9 selecionada: Ver estoque");
          break;
        case 10:
          System.out.println("Opção 10 selecionada: Excluir produto do estoque");
          break;
        case 11:
          System.out.println("Saindo do sistema. Até breve!");
          lojinhaController.iniciar();
          break;
        default:
          System.out.println("Opção inválida. Tente novamente.");
      }
    }
    scanner.close();
  }

  public void retornarAoMenu(String userType){
    print("Retornar ao menu? S/N: ");
    String resposta = scanner.next();
    scanner.nextLine();
    String lower = resposta.toLowerCase();
    switch (lower) {
      case "s":
        if (Objects.equals(userType, CLIENTE)) {
          menuCliente();
        } else {
          exibirMenuGerente();
        }
        break;
      case "n":
        menuInicial();
        scanner.close();
      default:
        System.out.println("Saindo do sistema. Até breve!");
    }
  }
}
