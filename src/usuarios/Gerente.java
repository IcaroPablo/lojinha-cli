package src.usuarios;

public class Gerente extends Usuario {

  public Gerente(String cpf, String nome, String telefone) {
    super(cpf, nome, telefone, true);
  }
  @Override
  public boolean equals(Object obj) {
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return null;
  }

}
