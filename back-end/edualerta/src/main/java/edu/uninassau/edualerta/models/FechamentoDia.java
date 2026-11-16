package edu.uninassau.edualerta.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@ToString(exclude = "fechamentoSemanal")
@Entity
@Table(name = "tb_fechamento_dia", schema = "app",
       uniqueConstraints =
       {
         @UniqueConstraint(name = "uk_fechamento_dia_data",
                           columnNames = {"fechamento_semanal_id", "data"})
       })
public class FechamentoDia {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fechamento_semanal_id", nullable = false)
  private FechamentoSemanal fechamentoSemanal;

  @Column(nullable = false) private LocalDate data;

  @Column(name = "quantidade_aulas", nullable = false)
  private Integer quantidadeAulas;

  public FechamentoDia(FechamentoSemanal fechamentoSemanal, LocalDate data,
                       Integer quantidadeAulas) {
    this.fechamentoSemanal = fechamentoSemanal;
    this.data = data;
    this.quantidadeAulas = quantidadeAulas;
  }
}
