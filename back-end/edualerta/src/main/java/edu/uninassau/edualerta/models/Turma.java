package edu.uninassau.edualerta.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tb_turma", schema = "app")
public class Turma {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Column(nullable = false, length = 255) private String nome;

  @Column(name = "ano_letivo", nullable = false) private Integer anoLetivo;

  @Column(nullable = false) private boolean ativo;

  public Turma(String nome, Integer anoLetivo) {
    this.nome = nome;
    this.anoLetivo = anoLetivo;
    this.ativo = true;
  }
}
