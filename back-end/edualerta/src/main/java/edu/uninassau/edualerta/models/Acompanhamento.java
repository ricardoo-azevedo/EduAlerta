
package edu.uninassau.edualerta.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"alerta", "usuario"})
@Entity
@Table(name = "tb_acompanhamento", schema = "app")
public class Acompanhamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alerta_id", nullable = false)
  private Alerta alerta;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "data_hora", nullable = false, updatable = false)
  private LocalDateTime dataHora;

  @Column(name = "tipo_intervencao", nullable = false, length = 50)
  private String tipoIntervencao;

  @Column(nullable = false, length = 1000)
  private String descricao;

  @Column(nullable = false, length = 1000)
  private String resultado;

  public Acompanhamento(
      Alerta alerta,
      Usuario usuario,
      String tipoIntervencao,
      String descricao,
      String resultado) {

    this.alerta = alerta;
    this.usuario = usuario;
    this.tipoIntervencao = tipoIntervencao;
    this.descricao = descricao;
    this.resultado = resultado;
  }

  @PrePersist
  protected void prePersist() {
    if (dataHora == null) {
      dataHora = LocalDateTime.now();
    }
  }
}

