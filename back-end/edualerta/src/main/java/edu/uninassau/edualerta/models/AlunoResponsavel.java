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
@ToString(exclude = { "aluno", "responsavel" })
@Entity
@Table(name = "tb_aluno_responsavel", schema = "app", uniqueConstraints = @UniqueConstraint(name = "uk_aluno_responsavel", columnNames = {
    "aluno_id", "responsavel_id" }))
public class AlunoResponsavel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "aluno_id", nullable = false)
  private Aluno aluno;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "responsavel_id", nullable = false)
  private Responsavel responsavel;
  @Column(name = "responsavel_principal", nullable = false)
  private boolean responsavelPrincipal;
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @PrePersist
  void prePersist() {
    if (criadoEm == null)
      criadoEm = LocalDateTime.now();
  }

  public AlunoResponsavel(Aluno aluno, Responsavel responsavel,
      boolean principal) {
    this.aluno = aluno;
    this.responsavel = responsavel;
    this.responsavelPrincipal = principal;
  }
}
