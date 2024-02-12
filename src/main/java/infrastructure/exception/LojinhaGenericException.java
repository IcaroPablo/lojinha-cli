package src.main.java.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LojinhaGenericException extends RuntimeException {

  public static final String NAME = "name";
  public static final String REASON = "reason";
  private final String title;
  private final String detail;
  private final Map<String, String> invalidParams;

  public LojinhaGenericException(
      String title, String detail, Map<String, String> invalidParams) {
    super(generateDetailedTitle(title, invalidParams));
    this.title = title;
    this.detail = detail;
    this.invalidParams = invalidParams;
  }

  private static String generateDetailedTitle(String title, Map<String, String> invalidParams) {
    StringBuilder builder = new StringBuilder(title);

    if (invalidParams != null && invalidParams.containsKey(REASON)) {
      builder.append(" - ").append(invalidParams.get(REASON));
    }

    return builder.toString();
  }
}
