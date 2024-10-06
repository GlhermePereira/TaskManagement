package fatecpg.br.edu;

import java.util.Scanner;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    try (var connection = DB.connect()) {
      TarefaDAO TarefaDAO = new TarefaDAO(connection);
      // CategoryDAO categoryDAO = new CategoryDAO(connection);
      Scanner scanner = new Scanner(System.in);

      while (true) {
        System.out.println("1. Adicionar Tarefa.");
        System.out.println("2. Atualizar Tarefa.");
        System.out.println("3. Excluir Tarefa.");
        System.out.println("4 Marcar Tarefa como concluida.");
        System.out.println("5 Listar todas as tarefas.");
        System.out.println("6. Listar Tarefas por Categoria.");
        System.out.println("7. Listar Tarefas por Status.");
        System.out.println("8. Sair.");
        System.out.print("Escolha uma opção: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
          case 1:
            // Adicionar tarefa
            Tarefa task = new Tarefa();

            System.out.print("Título: ");
            task.setTitulo(scanner.nextLine());

            System.out.print("Descrição: ");
            task.setDescricao(scanner.nextLine());

            System.out.print("ID da Categoria: ");
            task.setIdCategoria(scanner.nextInt());
            scanner.nextLine(); // Consumir a nova linha restante após nextInt()

            System.out.print("A tarefa está concluída? (s/n): ");
            String concluida = scanner.nextLine();

            boolean novoStatus = concluida.equalsIgnoreCase("s");
            task.setStatus(novoStatus);
            task.setCriadoEm(java.time.LocalDateTime.now().toString());
            task.setAtualizadoEm(java.time.LocalDateTime.now().toString());
            TarefaDAO.AdiconarTarefa(task);

            System.out.println("Tarefa adicionada.");
            break;

          case 2:
            TarefaDAO.atualizarTarefa(scanner);
            break;

          case 3:
            TarefaDAO.exluirTarefa(scanner);
            break;

          case 4:
            TarefaDAO.concluirTarefa(scanner);
            break;

          case 5:
            // 5 Listar todas as tarefas.
            TarefaDAO.listarTodasTarefas();
            break;
          case 6:
            // 6. Listar Tarefas por Categoria.
            TarefaDAO.listarTarefasPorCategoria();
            break;
          case 7:
            // 7 listar por status
            TarefaDAO.listarTarefasPorStatus();
            break;

          case 8:
            // Sair
            System.out.println("Saindo...");
            return;

          default:
            System.out.println("Opção inválida.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
