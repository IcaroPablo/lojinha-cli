package src.main.java.view;

import src.main.java.entities.Cliente;
import src.main.java.entities.Gerente;
import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.infrastructure.utils.Valid;
import src.main.java.rest.dtos.Carrinho;
import src.main.java.service.ComprasService;
import src.main.java.service.ProdutoService;
import src.main.java.service.UsuarioService;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import static src.main.java.constants.Constantes.ADICIONAR;
import static src.main.java.constants.Constantes.BD_GERENTES;
import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.BD_USUARIOS;
import static src.main.java.constants.Constantes.CARTAO;
import static src.main.java.constants.Constantes.CLIENTE;
import static src.main.java.constants.Constantes.DINHEIRO;
import static src.main.java.constants.Constantes.GERENTE;
import static src.main.java.constants.Constantes.LOGIN_GERENTES;
import static src.main.java.constants.Constantes.LOGIN_USUARIOS;
import static src.main.java.constants.Constantes.SAINDO;
import static src.main.java.constants.Constantes.SUBSTITUIR;
import static src.main.java.constants.Constantes.SUBTRAIR;
import static src.main.java.infrastructure.utils.Present.print;
import static src.main.java.infrastructure.utils.Present.printf;
import static src.main.java.infrastructure.utils.Present.println;

public class Menus {
  private final UsuarioService usuarioService;
  private final ProdutoService produtoService;
  private final ComprasService comprasService;
  private final Valid valid;

  public Menus() {
    this.comprasService = null;
    this.valid = null;
    this.usuarioService = null;
    this.produtoService = null;
  }
  public Menus(UsuarioService usuarioService, ProdutoService produtoService, ComprasService comprasService, Valid valid) {
    this.usuarioService = usuarioService;
    this.produtoService = produtoService;
    this.comprasService = comprasService;
    this.valid = valid;
  }
  InputStream inputStream = System.in;
  Scanner scanner = new Scanner(inputStream).useLocale(Locale.US);

  public void menuInicial() throws BusinessException, FileNotFoundException {
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

  public void loginUsuario() throws BusinessException, FileNotFoundException {
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

  public void fazerCadastro() throws BusinessException, FileNotFoundException {
    print("Opção selecionada: FAZER CADASTRO \n " +
        "Você deseja fazer o cadastro de cliente ou administrador? \n " +
        "Digite 1 para Cliente ou 2 para Administrador: ");
    int tipoCadastro = scanner.nextInt();
    scanner.nextLine();

    if (tipoCadastro == 1) {
      print("Digite o CPF: ");
      String cpf = scanner.next();
      if (usuarioService.usuarioExiste(cpf, BD_USUARIOS)) {
        print("Já existe um usuário com este CPF. \nRedirecionando ao menu principal.");
        menuInicial();
        return;
      }
      print("Digite o nome: ");
      String nome = scanner.nextLine();
      scanner.nextLine();
      print("Digite o telefone: ");
      String telefone = scanner.next();
//      if(!valid.isValidTelefone(telefone)) throw new BusinessException("O número do celular é inválido.");
      print("Digite a senha de acesso: ");
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
      print("Digite a senha de acesso: ");
      String senha = scanner.next();
      Gerente gerente = new Gerente(cpf, nome, telefone);
      usuarioService.salvarUsuario(cpf, nome, telefone, GERENTE, BD_GERENTES);
      usuarioService.salvarDadosAcessoUsuario(gerente.getCpf(), senha, GERENTE, LOGIN_GERENTES);
      println("Cadastro realizado com sucesso!");
    }

    print("Redirecionando para o Menu Inicial \n Faça login, por favor.");
    menuInicial();
  }

  public void loginAdministrador() throws BusinessException, FileNotFoundException {
    print("Opção 3 selecionada: FAZER LOGIN DE GERENTE \n Digite o CPF: ");
    String cpf = scanner.next();
    scanner.nextLine();
    if (usuarioService.usuarioExiste(cpf, BD_GERENTES)) {
      print("Digite a SENHA: ");
      String senha = scanner.nextLine();
      if (usuarioService.login(cpf, senha, LOGIN_GERENTES)) {
        usuarioService.bemVindoUsuario(cpf, BD_GERENTES);
        menuGerente();
      } else {
        println("Não foi possível fazer login. Por favor tente novamente.");
        return;
      }
    } else {
      printf("O usuário com o CPF %s não existe. Retornando ao menu inicial. \n", cpf);
      menuInicial();
    }
  }

  public void menuCliente() throws BusinessException, FileNotFoundException {
    print("Selecione uma opção: \n " +
            "1. Nova Compra \n " +
            "2. Visualizar Cadastro \n " +
            "3. Alterar Cadastro \n " +
            "4. Remover Cadastro \n " +
            "5. Ver carrinho \n " +
            "6. Limpar carrinho \n " +
            "7. Sair \n " +
            "DIGITE SUA OPÇÃO: ");
    int option = scanner.nextInt();
    while (option != 7) {
      switch (option) {
        case 1 -> novaCompra();
        case 2 -> visualizarCadastro();
        case 3 -> alterarCadastro();
        case 4 -> removerCadastro();
//        case 5 -> verCarrinho();
//        case 6 -> limparCarrinho();
        default -> print("Escolha uma opção válida");
      }
    }
    print(SAINDO);
    menuInicial();
  }

  private void removerCadastro() throws BusinessException, FileNotFoundException {
    print("Opção 4 selecionada: Remover Cadastro \n Tem certeza que deseja deletar o cadastro? S/N: ");
    String resposta = scanner.next();
    if (resposta.equalsIgnoreCase("s")) {
      print("Digite o CPF para confirmar: ");
      String cpf = scanner.next();
      assert usuarioService != null;
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

  private void alterarCadastro() throws BusinessException, FileNotFoundException {
    print("Opção 3 selecionada: Alterar Cadastro \n Digite o CPF novamente: ");
    String cpf = scanner.next();
    scanner.nextLine();
    assert usuarioService != null;
    usuarioService.verCadastro(cpf, BD_USUARIOS);
    println("NOVO NOME: ");
    String novoNome = scanner.nextLine();
    println("NOVO TELEFONE: ");
    String novoTelefone = scanner.nextLine();
    usuarioService.alterarCadastro(cpf, novoNome, novoTelefone);
    retornarAoMenu(CLIENTE);
  }

  private void visualizarCadastro() throws BusinessException, FileNotFoundException {
    print("Opção 2 selecionada: Visualizar Cadastro \n " +
        "Digite o CPF novamente: ");
    String cpf = scanner.next();
    assert usuarioService != null;
    usuarioService.verCadastro(cpf, BD_USUARIOS);
    retornarAoMenu(CLIENTE);
  }

  private void novaCompra() throws FileNotFoundException {
    print("Opção 3 selecionada: Nova compra \n " +
        "Digite o CPF novamente: ");
    String cpf = scanner.next();
    scanner.nextLine();
    if(comprasService.carrinhoSalvo(cpf)) {
      List<Carrinho> carrinho = comprasService.verCarrinho(cpf);
      comprasService.imprimirCarrinho(carrinho);
      salvarOuFinalizarCompra(cpf, carrinho);
    } else {
      print("=".repeat(70) + "\n");
      produtoService.visualizarProdutos();
      List<Carrinho> carrinho = new ArrayList<>();
      do {
        print("Digite o código do produto ou 0 para encerrar a compra: ");
        String codigo = scanner.next();
        if(codigo.equals("0")) break;
        if(produtoService.produtoExiste(codigo, BD_PRODUTOS)) {
          print("Digite a quantidade que deseja do produto: ");
          int quantidade = scanner.nextInt();
          if(produtoService.isAvailableQuantity(codigo, quantidade)) {
            String descricao = produtoService.getProduto(codigo).getDescricao();
            Double valor = produtoService.getProduto(codigo).getValor();

            carrinho.add(new Carrinho(codigo, descricao, valor, quantidade));
          } else {
            printf("Não há estoque disponível para o produto %s ", produtoService.getProduto(codigo).getDescricao());
          }
        } else printf("Não existe um produto com este código: %s", codigo);
        print("Deseja incluir um novo produto? S/N: ");
      } while (scanner.next().equalsIgnoreCase("s"));

      comprasService.imprimirCarrinho(carrinho);
      if (!carrinho.isEmpty()) salvarOuFinalizarCompra(cpf, carrinho);
      else retornarAoMenu(GERENTE);
    }
  }

  private void salvarOuFinalizarCompra(String cpf, List<Carrinho> carrinho) throws FileNotFoundException {
    print("Deseja salvar o carrinho (S) ou finalizar a compra (F)? ");
    String resposta = scanner.next();
    assert comprasService != null;
    if (resposta.equalsIgnoreCase("S")) {
      comprasService.salvarCarrinho(cpf, carrinho);
    } else if (resposta.equalsIgnoreCase("F")) {
      finalizarCompra(cpf, carrinho);
    } else {
      print("Opção inválida.");
    }
  }

  private void finalizarCompra(String cpf, List<Carrinho> carrinho) throws FileNotFoundException {
    print("Antes de finalizar, vejamos os itens e se é preciso alterar algum.\n");
    comprasService.imprimirCarrinho(carrinho);
    print("Deseja alterar ou remover algum item do carrinho? S/N: ");
    String alterar = scanner.next();
    do {
      if ("s".equalsIgnoreCase(alterar)) {
        print("Digite o código do produto a ser alterado: ");
        String codigo = scanner.next();
        print("Digite a quantidade: (Caso queira remover algum item, digite a quantidade 0)");
        int quantidade = scanner.nextInt();
        comprasService.alterarItemCarrinho(carrinho, codigo, quantidade);
        comprasService.imprimirCarrinho(carrinho);
      }
    } while (!"n".equalsIgnoreCase(alterar));
    print("Finalizemos sua compra.\nPara pagamentos à vista temos desconto de 10%\nNo cartão não há desconto.");
    print("Qual a forma de pagamento? Dinheiro (D) ou Cartão de Crédito (C)? ");
    String resposta = scanner.next();
    resposta.toLowerCase();
    switch (resposta) {
      case "d" -> comprasService.emitirNotaFiscal(carrinho, cpf, DINHEIRO);
      case "c" -> comprasService.emitirNotaFiscal(carrinho, cpf, CARTAO);
      default -> print("");
    }
    for (Carrinho item : carrinho) {
      produtoService.alterarQuantidadeProduto(item.getCodigo(), item.getQuantidade(), SUBTRAIR);
    }
    comprasService.limparCarrinho(cpf);
    print("Compra finalizada com sucesso.");
    retornarAoMenu(GERENTE);
  }

  public void menuGerente() throws BusinessException, FileNotFoundException {
    print("Selecione uma opção: \n " +
        " 1. Cadastrar Cliente \n " +
        " 2. Ver cadastros de clientes \n " +
        " 3. Alterar cadastro de cliente \n " +
        " 4. Excluir Cadastro de Cliente \n " +
        " 5. Cadastrar Produto \n " +
        " 6. Visualizar cadastro de Produto \n " +
        " 7. Alterar Cadastro de Produto \n " +
        " 8. Alterar Preço de Produto \n " +
        " 9. Inserir Produto no Estoque \n " +
        " 10. Excluir Cadastro de Produto \n " +
        " 11. Sair" + "\n " +
        "=".repeat(50));
    print("\n OPÇÃO: ");
    int option = scanner.nextInt();
    while (option != 11) {
      switch (option) {
        case 1 -> fazerCadastro();
        case 2 -> visualizarCadastros();
        case 3 -> alterarCadastroClientes();
        case 4 -> excluirCadastroClientes();
        case 5 -> cadastrarProduto();
        case 6 -> visualizarCadastroProdutos();
        case 7 -> alterarCadastroProduto();
        case 8 -> alterarPrecoProduto();
        case 9 -> inserirProdutoEstoque();
        case 10 -> excluirProduto();
        default -> print("Escolha uma opção válida");
      }
    }
    print(SAINDO);
    menuInicial();
  }

  public void visualizarCadastros() throws BusinessException, FileNotFoundException {
    print("Opção 2 selecionada: Ver cadastros de clientes " + "\n " + "=".repeat(70));
    assert usuarioService != null;
    usuarioService.recuperarCadastros();
    retornarAoMenu(GERENTE);
  }

  public void alterarCadastroClientes() throws BusinessException, FileNotFoundException {
    print("Opção 3 selecionada: Alterar cadastro de cliente \n " +
        "Listando primeiro os cadastros dos clientes para escolha. \n " +
        "=".repeat(70));
    assert usuarioService != null;
    usuarioService.recuperarCadastros();
    print("Digite o CPF do CLIENTE que deseja alterar: ");
    String cpf = scanner.next();
    scanner.nextLine();
    print("DIGITE O NOVO NOME: ");
    String novoNome = scanner.nextLine();
    print("DIGITE O NOVO TELEFONE: ");
    String novoTelefone = scanner.nextLine();
    usuarioService.alterarCadastro(cpf, novoNome, novoTelefone);
    retornarAoMenu(GERENTE);
  }

  public void excluirCadastroClientes() throws BusinessException, FileNotFoundException {
    print("Opção 4 selecionada: Excluir Cadastro de Cliente \n " +
        "=".repeat(70));
    assert usuarioService != null;
    usuarioService.recuperarCadastros();
    print("Digite o CPF do cliente que deseja excluir: ");
    String cpf = scanner.next();
    scanner.nextLine();
    usuarioService.excluirCadastro(cpf);
    print("Voltando ao menu. \n ");
    menuGerente();
  }

  public void cadastrarProduto() throws BusinessException, FileNotFoundException {
    print("Opção 5 selecionada: Cadastrar Produto \n Digite o código do produto: ");
    String codigo = scanner.next();
    scanner.nextLine();
    if (produtoService.produtoExiste(codigo, BD_PRODUTOS)) {
      print("Já existe um produto com este código \n Redirecionando ao menu principal");
      menuGerente();
      return;
    }
    print("Digite a descrição do produto: ");
    String descricaoProduto = scanner.nextLine();
    print("Digite o valor do produto: ");
    Double valor = scanner.nextDouble();
    scanner.nextLine();
    print("Digite a quantidade do produto: ");
    int quantidade = scanner.nextInt();
    produtoService.cadastrarProduto(codigo, descricaoProduto, valor, quantidade);
    retornarAoMenu(GERENTE);
  }
  public void visualizarCadastroProdutos() throws BusinessException, FileNotFoundException {
    print("Opção 6 selecionada: visualizar cadastro de produto \n" +
        "=".repeat(70) + "\n");
    assert produtoService != null;
    produtoService.visualizarProdutos();
    retornarAoMenu(GERENTE);
  }
  public void alterarCadastroProduto() throws BusinessException, FileNotFoundException {
    print("Opção 7 selecionada: Alterar cadastro de produto \n " +
        "=".repeat(70) + "\n");
    assert produtoService != null;
    produtoService.visualizarProdutos();
    print("Digite o código do produto: ");
    String codigo = scanner.next();
    print("Digite a nova descrição: ");
    String novaDescricao = scanner.next();
    print("Digite o novo valor: ");
    Double novoValor = scanner.nextDouble();
    print("Deseja reassinalar uma quantidade para o produto? S/N: ");
    String response = scanner.next();
    if ("s".equalsIgnoreCase(response)) {
      print("Digite a nova quantidade: ");
      Integer quantidade = scanner.nextInt();
      produtoService.alterarCadastroProduto(codigo, novaDescricao, novoValor, null);
      produtoService.alterarQuantidadeProduto(codigo, quantidade, SUBSTITUIR);
    } else if ("n".equalsIgnoreCase(response)) {
      produtoService.alterarCadastroProduto(codigo, novaDescricao, novoValor, null);
    }
    retornarAoMenu(GERENTE);
  }
  public void alterarPrecoProduto() throws BusinessException, FileNotFoundException {
    print("Opção 8 selecionada: Alterar preço de produto \n" +
        "=".repeat(70) + "\n");
    assert produtoService != null;
    produtoService.visualizarProdutos();
    print("Digite o código do produto: ");
    String codigo = scanner.next();
    print("Digite o novo valor: ");
    Double novoValor = scanner.nextDouble();
    produtoService.alterarPrecoProduto(codigo, novoValor);
    retornarAoMenu(GERENTE);
  }
  public void inserirProdutoEstoque() throws BusinessException, FileNotFoundException {
    print("Opção 9 selecionada: Inserir produto no estoque \n" +
        "=".repeat(70) + "\n");
    assert produtoService != null;
    produtoService.visualizarProdutos();
    print("Digite o código do produto: ");
    String codigo = scanner.next();
    print("Digite a quantidade do produto no estoque: ");
    Integer quantidade = scanner.nextInt();
    if (produtoService.produtoExiste(codigo, BD_PRODUTOS)) produtoService.alterarQuantidadeProduto(codigo, quantidade, ADICIONAR);
    retornarAoMenu(GERENTE);
  }

  public void excluirProduto() throws FileNotFoundException {
    print("Opção 10 selecionada: Excluir cadastro de produto \n" +
        "=".repeat(70) + "\n");
    assert produtoService != null;
    produtoService.visualizarProdutos();
    print("Digite o código do produto: ");
    String codigo = scanner.next();
    if (produtoService.produtoExiste(codigo, BD_PRODUTOS))
      produtoService.excluirProduto(codigo);
    retornarAoMenu(GERENTE);
  }

  public void retornarAoMenu(String userType) throws BusinessException, FileNotFoundException {
    print("Retornar ao menu? S/N: ");
    String resposta = scanner.next();
    scanner.nextLine();
    String lower = resposta.toLowerCase();
    switch (lower) {
      case "s":
        if (Objects.equals(userType, CLIENTE)) {
          menuCliente();
        } else {
          menuGerente();
        }
        break;
      case "n":
        menuInicial();
        scanner.close();
        break;
      default:
        System.out.println("Saindo do sistema. Até breve!");
    }
  }
}
