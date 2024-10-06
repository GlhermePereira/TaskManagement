package fatecpg.br.edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
  private static final String URL = "jdbc:postgresql://localhost:5432/gerenciador_tarefas";
  private static final String USER = "postgres";
  private static final String PASSWORD = "";

  public static Connection connect() {
    try {
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
}
