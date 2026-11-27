package edu.uninassau.edualerta.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tb_responsavel", schema = "app")
public class Responsavel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 150)
  private String nome;
  @Column(nullable = false, length = 254)
  private String email;
  @Column(length = 20)
  private String telefone;
  @Column(length = 50)
  private String parentesco;
  @Column(nullable = false)
  private boolean ativo = true;

  public Responsavel(String nome, String email, String telefone,
      String parentesco) {
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
    this.parentesco = parentesco;
    this.ativo = true;
  }
}
