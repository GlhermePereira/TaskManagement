package fatecpg.br.edu;

public class Tarefa {
  // atributos
  private String titulo;
  private String descricao;
  private int idCategoria;
  private Boolean status;
  private String criadoEm;
  private String atualizadoEm;

  // metodo setters
  public void setStatus(boolean status) {
    this.status = status;
  }

  public void setCriadoEm(String criadoEm) {
    this.criadoEm = criadoEm;
  }

  public void setAtualizadoEm(String atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public void setIdCategoria(int id) {
    this.idCategoria = id;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  // metodos geters
  public String getTitulo() {
    return this.titulo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getCriadoEm() {
    return this.criadoEm;
  }

  public String getAtualizadoEm() {
    return this.atualizadoEm;
  }

  public int getIdCategoria() {
    return this.idCategoria;
  }

  public boolean getStatus() {
    return this.status;
  }
}
