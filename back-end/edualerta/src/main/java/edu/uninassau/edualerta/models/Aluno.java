
package edu.uninassau.edualerta.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tb_aluno", schema = "app")
public class Aluno {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Column(nullable = false, length = 150) private String nome;

  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

  @Column(nullable = false) private boolean ativo;

  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  public Aluno(String nome, LocalDate dataNascimento) {
    this.nome = nome;
    this.dataNascimento = dataNascimento;
    this.ativo = true;
  }

  @PrePersist
  protected void prePersist() {
    if (criadoEm == null) {
      criadoEm = LocalDateTime.now();
    }
  }
}
