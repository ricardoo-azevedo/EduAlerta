package edu.uninassau.edualerta.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "alerta", "responsavel" })
@Entity
@Table(name = "tb_notificacao", schema = "app", uniqueConstraints = @UniqueConstraint(name = "uk_notificacao_alerta_responsavel", columnNames = {
    "alerta_id", "responsavel_id" }))
public class Notificacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alerta_id", nullable = false)
  private Alerta alerta;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "responsavel_id", nullable = false)
  private Responsavel responsavel;
  @Column(name = "email_destino", nullable = false, length = 254)
  private String emailDestino;
  @Column(nullable = false, length = 254)
  private String assunto;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String mensagem;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private StatusNotificacao status;
  @Column(name = "erro_envio", length = 1000)
  private String erroEnvio;
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;
  @Column(name = "enviado_em")
  private LocalDateTime enviadoEm;

  @PrePersist
  void prePersist() {
    if (criadoEm == null)
      criadoEm = LocalDateTime.now();
  }

  public Notificacao(Alerta alerta, Responsavel responsavel, String email,
      String assunto, String mensagem) {
    this.alerta = alerta;
    this.responsavel = responsavel;
    this.emailDestino = email;
    this.assunto = assunto;
    this.mensagem = mensagem;
    this.status = StatusNotificacao.FALHA;
  }
}
