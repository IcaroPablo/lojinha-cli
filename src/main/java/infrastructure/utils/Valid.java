package src.main.java.infrastructure.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Valid {

  public boolean isValidCpf(String cpf) {
      cpf = cpf.replaceAll("\\D", "");

      if (cpf.length() != 11) {
        return false;
      }

    return !cpf.matches("(\\d)\\1{10}");
  }

  public boolean isValidTelefone(String celular) {
    String regex = "^[1-9]{2}9[0-9]{8}$";

    Pattern pattern = Pattern.compile(regex);

    Matcher matcher = pattern.matcher(celular);

    return matcher.matches();
  }
}
