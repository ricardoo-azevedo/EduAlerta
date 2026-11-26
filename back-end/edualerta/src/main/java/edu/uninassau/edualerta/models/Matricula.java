package edu.uninassau.edualerta.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_matricula", schema = "app")
public class Matricula {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "aluno_id", nullable = false)
  @ToString.Exclude
  private Aluno aluno;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "turma_id", nullable = false)
  @ToString.Exclude
  private Turma turma;

  @Column(name = "data_inicio", nullable = false)
  private LocalDate dataInicio;

  @Column(name = "data_fim")
  private LocalDate dataFim;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private StatusMatricula status;

  public Matricula(Aluno aluno, Turma turma, LocalDate dataInicio) {
    this.aluno = aluno;
    this.turma = turma;
    this.dataInicio = dataInicio;
    this.dataFim = null;
    this.status = StatusMatricula.ATIVA;
  }
}
