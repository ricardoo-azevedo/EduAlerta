
package edu.uninassau.edualerta.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"fechamentoSemanal", "matricula"})
@Entity
@Table(name = "tb_frequencia_semanal", schema = "app",
       uniqueConstraints =
       {
         @UniqueConstraint(
             name = "uk_frequencia_fechamento_matricula",
             columnNames = {"fechamento_semanal_id", "matricula_id"})
       })
public class FrequenciaSemanal {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "fechamento_semanal_id", nullable = false)
  private FechamentoSemanal fechamentoSemanal;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "matricula_id", nullable = false)
  private Matricula matricula;

  @Column(nullable = false) private Integer faltas;

  @Column(name = "faltas_justificadas", nullable = false)
  private Integer faltasJustificadas;

  public FrequenciaSemanal(FechamentoSemanal fechamentoSemanal,
                           Matricula matricula, Integer faltas,
                           Integer faltasJustificadas) {
    this.fechamentoSemanal = fechamentoSemanal;
    this.matricula = matricula;
    this.faltas = faltas;
    this.faltasJustificadas = faltasJustificadas;
  }
}
