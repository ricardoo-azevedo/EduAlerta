package edu.uninassau.edualerta.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tb_periodo_letivo", schema = "app")
public class PeriodoLetivo {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Column(nullable = false, length = 100) private String nome;

  @Column(name = "ano_letivo", nullable = false) private Integer anoLetivo;

  @Column(name = "numero_semestre", nullable = false)
  private Integer numeroSemestre;

  @Column(name = "data_inicio", nullable = false) private LocalDate dataInicio;

  @Column(name = "data_fim", nullable = false) private LocalDate dataFim;

  @Column(nullable = false) private boolean ativo;

  public PeriodoLetivo(String nome, Integer anoLetivo, Integer numeroSemestre,
                       LocalDate dataInicio, LocalDate dataFim) {

    this.nome = nome;
    this.anoLetivo = anoLetivo;
    this.numeroSemestre = numeroSemestre;
    this.dataInicio = dataInicio;
    this.dataFim = dataFim;
    this.ativo = true;
  }
}
