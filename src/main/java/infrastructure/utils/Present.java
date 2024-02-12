package src.main.java.infrastructure.utils;

public class Present {
  public static void print(String string) {
    System.out.print(string);
  }

  public static void println(String string) {
    System.out.println(string);
  }

  public static void printf(String string, Object... args) {
    System.out.printf(string, args);
  }
}
