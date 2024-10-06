package fatecpg.br.edu;

import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class TarefaDAO {
  private Connection connection;

  public TarefaDAO(Connection connection) {
    this.connection = connection;
  }

  public void AdiconarTarefa(Tarefa task) throws SQLException {
    String sql = "INSERT INTO tarefas (nm_titulo, ds_descricao, id_categoria, ic_status, dt_criado_em, dt_atualizado_em) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, task.getTitulo());
      stmt.setString(2, task.getDescricao());
      stmt.setInt(3, task.getIdCategoria());
      stmt.setBoolean(4, task.getStatus());

      stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // dt_criado_em
      stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // dt_atualizado_em
      stmt.executeUpdate();
    }
  }

  public void atualizarTarefa(Scanner scanner) {
    System.out.print("Digite o ID da tarefa para atualizar: ");
    int id = scanner.nextInt();
    scanner.nextLine(); // Consumir a nova linha

    try (var connection = DB.connect()) {
      // Consultar a tarefa existente
      String selectSQL = "SELECT nm_titulo, ds_descricao, ic_status,dt_atualizado_em FROM tarefas WHERE id = ?";
      try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
        selectStmt.setInt(1, id);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
          // Mostrar os dados atuais
          String titulo = rs.getString("nm_titulo");
          String descricao = rs.getString("ds_descricao");
          boolean status = rs.getBoolean("ic_status");

          System.out.println("Dados atuais da tarefa:");
          System.out.println("Título: " + titulo);
          System.out.println("Descrição: " + descricao);
          System.out.println("Concluída: " + (status ? "Sim" : "Não"));

          // Receber novos dados do usuário
          System.out.print("Digite o novo título (deixe em branco para não alterar): ");
          String novoTitulo = scanner.nextLine();
          if (novoTitulo.isEmpty()) {
            novoTitulo = titulo; // Manter o título atual se não houver alteração
          }

          System.out.print("Digite a nova descrição (deixe em branco para não alterar): ");
          String novaDescricao = scanner.nextLine();
          if (novaDescricao.isEmpty()) {
            novaDescricao = descricao; // Manter a descrição atual se não houver alteração
          }

          System.out.print("A tarefa está concluída? (s/n): ");
          String concluida = scanner.nextLine();
          boolean novoStatus = concluida.equalsIgnoreCase("s");

          // Atualizar a tarefa no banco de dados
          String updateSQL = "UPDATE tarefas SET nm_titulo = ?, ds_descricao = ?, ic_status = ?, dt_atualizado_em = ? WHERE id = ?";
          try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
            updateStmt.setString(1, novoTitulo);
            updateStmt.setString(2, novaDescricao);
            updateStmt.setBoolean(3, novoStatus);
            updateStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            updateStmt.setInt(5, id);

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
              System.out.println("Tarefa atualizada com sucesso!");
            } else {
              System.out.println("Nenhuma tarefa encontrada com o ID fornecido.");
            }
          }
        } else {
          System.out.println("Nenhuma tarefa encontrada com o ID fornecido.");
        }
      }
    } catch (SQLException e) {
      System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
    }
  }

  public void exluirTarefa(Scanner scanner) {
    System.out.println("Digite o ID da tarefa que vai ser excluida: ");
    int id = scanner.nextInt();
    try (var connection = DB.connect()) {
      String deleteSQL = "DELETE FROM tarefas WHERE id = ?";
      try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
        deleteStmt.setInt(1, id);
        int rowsAffected = deleteStmt.executeUpdate();

        if (rowsAffected > 0) {
          System.out.print("Tarefa excluida com sucesso.");
        } else {
          System.out.print("Nenhuma tarefa econtrada!");
        }
      }
    } catch (SQLException e) {
      System.out.println("Erro ao excluir a tarefa: " + e.getMessage());
    }
  }

  public void concluirTarefa(Scanner scanner) {
    System.out.println("Digite o ID da tarefa a ser concluida: ");
    int id = scanner.nextInt();
    scanner.nextLine();
    try (var connection = DB.connect()) {
      String updateSQL = "UPDATE tarefas set ic_status = ?,dt_criado_em = ? WHERE id = ?";
      try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
        updateStmt.setBoolean(1, true);
        updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        updateStmt.setInt(3, id);

        int rowsAffected = updateStmt.executeUpdate();
        if (rowsAffected > 0) {
          System.out.println("Tarefa atualizada com sucesso!");
        } else {
          System.out.println("Nenhuma tarefa encontrada");
        }
      } catch (SQLException e) {
        System.out.println("Erro " + e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println("Erro " + e.getMessage());
    }
  }

  public void listarTodasTarefas() {

    try (var connection = DB.connect()) {
      // Consultar a tarefa existente
      String selectSQL = "SELECT id,nm_titulo, ds_descricao, ic_status,dt_atualizado_em FROM tarefas ORDER BY dt_atualizado_em";
      try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {

        ResultSet rs = selectStmt.executeQuery();

        while (rs.next()) {

          String titulo = rs.getString("nm_titulo");
          String descricao = rs.getString("ds_descricao");
          String status = rs.getString("ic_status");
          String id = rs.getString("id");
          // Date atualizadoEm = rs.getDate("dt_atualizado_em");
          SimpleDateFormat dateFormatter = new SimpleDateFormat(rs.getString("dt_atualizado_em"));

          if (status.equals("t"))
            System.out.println("Status: Concluido.");
          else
            System.out.println("Status: Em Andamento.");
          // Print the data
          System.out.println("Id: " + id + ".");
          System.out.println("Título: " + titulo + ".");
          System.out.println("Descrição: " + descricao + ".");
          System.out.println("Atualizado em: " + dateFormatter + ".");
          System.out.println("------------");
        }
      }
    } catch (SQLException e) {
      System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
    }
    // Manter a tela aberta
    System.out.println("Pressione Enter para sair...");
    new java.util.Scanner(System.in).nextLine();
  }

  public void listarTarefasPorCategoria() {
    try (var connection = DB.connect()) {
      String selectSQL = "SELECT t.nm_titulo, " +
          "       t.id,          " +
          "       t.ds_descricao, " +
          "       CASE " +
          "           WHEN t.ic_status THEN 'Concluído' " +
          "           ELSE 'Em Andamento' " +
          "       END AS status, " +
          "       t.dt_atualizado_em, " +
          "       t.dt_criado_em, " +
          "       c.nm_nome AS nome_categoria " +
          "FROM tarefas t " +
          "JOIN categorias c ON t.id_categoria = c.id " +
          "ORDER BY t.id_categoria";

      try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
        ResultSet rs = selectStmt.executeQuery();

        // Criar um formatador de data
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        while (rs.next()) {
          // Obter dados do ResultSet
          String id = rs.getString("id");
          String titulo = rs.getString("nm_titulo");
          String descricao = rs.getString("ds_descricao");
          String status = rs.getString("status");
          Timestamp atualizadoEm = rs.getTimestamp("dt_atualizado_em");
          Timestamp criadoEm = rs.getTimestamp("dt_criado_em");
          String nomeCategoria = rs.getString("nome_categoria");

          // Formatar datas
          String dataAtualizadoFormatada = (atualizadoEm != null) ? dateFormatter.format(atualizadoEm)
              : "Data não disponível";
          String dataCriadoFormatada = (criadoEm != null) ? dateFormatter.format(criadoEm) : "Data não disponível";

          // Exibir os dados
          System.out.println("Id: " + id + ".");
          System.out.println("Título: " + titulo + ".");
          System.out.println("Descrição: " + descricao + ".");
          System.out.println("Status: " + status + ".");
          System.out.println("Atualizado em: " + dataAtualizadoFormatada + ".");
          System.out.println("Criado em: " + dataCriadoFormatada + ".");
          System.out.println("Categoria: " + nomeCategoria + ".");
          System.out.println("------------");
        }
      }
    } catch (SQLException e) {
      System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
    }

    // Manter a tela aberta
    System.out.println("Pressione Enter para sair...");
    new java.util.Scanner(System.in).nextLine();
  }

  public void listarTarefasPorStatus() {
    try (var connection = DB.connect()) {
      String selectSQL = "SELECT t.nm_titulo, t.id, " +
          "       t.ds_descricao, " +
          "       CASE " +
          "           WHEN t.ic_status THEN 'Concluído' " +
          "           ELSE 'Em Andamento' " +
          "       END AS status, " +
          "       t.dt_atualizado_em, " +
          "       t.dt_criado_em, " +
          "       c.nm_nome AS nome_categoria " +
          "FROM tarefas t " +
          "JOIN categorias c ON t.id_categoria = c.id " +
          "ORDER BY t.ic_status";

      try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
        ResultSet rs = selectStmt.executeQuery();

        // Criar um formatador de data
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        while (rs.next()) {
          // Obter dados do ResultSet
          String id = rs.getString("id");
          String titulo = rs.getString("nm_titulo");
          String descricao = rs.getString("ds_descricao");
          String status = rs.getString("status");
          Timestamp atualizadoEm = rs.getTimestamp("dt_atualizado_em");
          Timestamp criadoEm = rs.getTimestamp("dt_criado_em");
          String nomeCategoria = rs.getString("nome_categoria");

          // Formatar datas
          String dataAtualizadoFormatada = (atualizadoEm != null) ? dateFormatter.format(atualizadoEm)
              : "Data não disponível";
          String dataCriadoFormatada = (criadoEm != null) ? dateFormatter.format(criadoEm) : "Data não disponível";

          // Exibir os dados
          System.out.println("Id: " + id + ".");
          System.out.println("Título: " + titulo + ".");
          System.out.println("Descrição: " + descricao + ".");
          System.out.println("Status: " + status + ".");
          System.out.println("Atualizado em: " + dataAtualizadoFormatada + ".");
          System.out.println("Criado em: " + dataCriadoFormatada + ".");
          System.out.println("Categoria: " + nomeCategoria + ".");
          System.out.println("------------");
        }
      }
    } catch (SQLException e) {
      System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
    }

    // Manter a tela aberta
    System.out.println("Pressione Enter para sair...");
    new java.util.Scanner(System.in).nextLine();
  }
}
