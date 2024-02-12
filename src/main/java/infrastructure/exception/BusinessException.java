package src.main.java.infrastructure.exception;

import java.util.Map;

public class BusinessException extends LojinhaGenericException {

  public BusinessException(String reason) {
    super("Operação não pode ser realizada", reason, Map.of(REASON, reason));
  }

  public BusinessException(String title, String reason) { super(title, reason, Map.of(REASON, reason)); }
}
