package edu.uninassau.edualerta.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "alerta")
@Entity
@Table(name = "tb_encaminhamento", schema = "app")
public class Encaminhamento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alerta_id", nullable = false)
  private Alerta alerta;
  @Column(name = "servico_destino", nullable = false, length = 150)
  private String servicoDestino;
  @Column(nullable = false, length = 150)
  private String finalidade;
  @Column(nullable = false, length = 1000)
  private String observacao;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private StatusEncaminhamento status;
  @Column(name = "data_encaminhamento", nullable = false)
  private LocalDate dataEncaminhamento;
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @PrePersist
  void prePersist() {
    if (criadoEm == null)
      criadoEm = LocalDateTime.now();
    if (dataEncaminhamento == null)
      dataEncaminhamento = LocalDate.now();
    if (status == null)
      status = StatusEncaminhamento.PENDENTE;
  }

  public Encaminhamento(Alerta alerta, String destino, String finalidade,
      String observacao, LocalDate data,
      StatusEncaminhamento status) {
    this.alerta = alerta;
    this.servicoDestino = destino;
    this.finalidade = finalidade;
    this.observacao = observacao;
    this.dataEncaminhamento = data;
    this.status = status == null ? StatusEncaminhamento.PENDENTE : status;
  }
}
