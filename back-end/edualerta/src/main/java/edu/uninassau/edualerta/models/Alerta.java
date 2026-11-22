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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"aluno", "periodoLetivo"})
@Entity
@Table(name = "tb_alerta", schema = "app",
       uniqueConstraints =
           @UniqueConstraint(name = "uk_alerta_aluno_periodo",
                             columnNames = {"aluno_id", "periodo_letivo_id"}))
public class Alerta {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "aluno_id", nullable = false)
  private Aluno aluno;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "periodo_letivo_id", nullable = false)
  private PeriodoLetivo periodoLetivo;

  @Column(name = "percentual_faltas", nullable = false, precision = 5,
          scale = 2)
  private BigDecimal percentualFaltas;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private NivelAlerta nivel;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SituacaoAlerta situacao;

  @Column(name = "resolvido_manualmente", nullable = false)
  private boolean resolvidoManualmente;

  @Column(nullable = false, length = 500) private String motivo;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Alerta(Aluno aluno, PeriodoLetivo periodoLetivo,
                BigDecimal percentualFaltas, NivelAlerta nivel, String motivo) {

    this.aluno = aluno;
    this.periodoLetivo = periodoLetivo;
    this.percentualFaltas = percentualFaltas;
    this.nivel = nivel;
    this.situacao = SituacaoAlerta.ATIVO;
    this.resolvidoManualmente = false;
    this.motivo = motivo;
    this.atualizadoEm = LocalDateTime.now();
  }
}
