package src.usuarios;

public class Usuario {
  private String nome;
  private String telefone;
  private String cpf;
  private String senha;


  public Usuario() {}

  public Usuario(String nome, String telefone, String cpf, String senha) {
    this.nome = nome;
    this.telefone = telefone;
    this.cpf = cpf;
    this.senha = senha;
  }

  public String getNome() {
    return nome;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getCpf() {
    return cpf;
  }

  public String getSenha() { return senha; }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setSenha(String senha) { this.senha = senha; }
}
