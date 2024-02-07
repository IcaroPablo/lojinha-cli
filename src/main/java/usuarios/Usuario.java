package src.main.java.usuarios;

public class Usuario {
  private String cpf;
  private String nome;
  private String telefone;
  private Boolean administrador;

  public Usuario(String cpf, String nome, String telefone, Boolean administrador) {
    this.cpf = cpf;
    this.nome = nome;
    this.telefone = telefone;
    this.administrador = administrador;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public Boolean getAdministrador() {
    return administrador;
  }

  public void setAdministrador(Boolean administrador) {
    this.administrador = administrador;
  }
}
