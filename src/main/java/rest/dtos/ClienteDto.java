package src.main.java.rest.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDto {
  private String cpf;
  private String nome;
  private String telefone;
  public static String cpfFormatter(String cpf) {
    String apenasNumeros = cpf.replaceAll("\\D", "");

    return apenasNumeros.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
  }

  public static String telefoneFormatter(String telefone) {
    String apenasNumeros = telefone.replaceAll("\\D", "");

    return apenasNumeros.replaceFirst("(\\d{2})(\\d{7})(\\d{4})", "($1)$2-$3");
  }
}
