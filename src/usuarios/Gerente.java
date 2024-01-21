package src.usuarios;

public class Gerente extends Usuario {
  private boolean administrador;

  public Gerente(String nome, String telefone, String cpf, String senha, boolean administrador) {
    super(nome, telefone, cpf, senha);
    this.administrador = administrador;
  }

  public boolean isAdministrador() {
    return administrador;
  }
}
