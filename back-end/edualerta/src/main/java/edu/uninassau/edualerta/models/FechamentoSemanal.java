package edu.uninassau.edualerta.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"turma", "periodoLetivo", "usuario"})
@Entity
@Table(name = "tb_fechamento_semanal", schema = "app",
       uniqueConstraints =
       {
         @UniqueConstraint(name = "uk_fechamento_turma_periodo_semana",
                           columnNames = {"turma_id", "periodo_letivo_id",
                                          "data_inicio_semana"})
       })
public class FechamentoSemanal {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "turma_id", nullable = false)
  private Turma turma;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "periodo_letivo_id", nullable = false)
  private PeriodoLetivo periodoLetivo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "data_inicio_semana", nullable = false)
  private LocalDate dataInicioSemana;

  @Column(name = "data_fim_semana", nullable = false)
  private LocalDate dataFimSemana;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private StatusFechamento status;

  public FechamentoSemanal(Turma turma, PeriodoLetivo periodoLetivo,
                           Usuario usuario, LocalDate dataInicioSemana,
                           LocalDate dataFimSemana) {
    this.turma = turma;
    this.periodoLetivo = periodoLetivo;
    this.usuario = usuario;
    this.dataInicioSemana = dataInicioSemana;
    this.dataFimSemana = dataFimSemana;
    this.status = StatusFechamento.RASCUNHO;
  }
}
