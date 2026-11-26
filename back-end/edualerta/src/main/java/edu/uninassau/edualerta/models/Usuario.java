package edu.uninassau.edualerta.models;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "authUserId")
@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
public class Usuario {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Column(name = "auth_user_id", nullable = false, unique = true)
  private UUID authUserId;

  @Column(nullable = false, length = 150) private String nome;

  @Column(nullable = false, unique = true, length = 254) private String email;

  @Enumerated(EnumType.STRING) @Column(nullable = false) private Perfil perfil;

  @Column(nullable = false) private boolean ativo;

  @Column(name = "alterar_senha_obrigatorio", nullable = false)
  private boolean alterarSenhaObrigatorio;

  public Usuario(UUID authUserId, String nome, String email, Perfil perfil,
                 boolean ativo) {

    this.authUserId = authUserId;
    this.nome = nome;
    this.email = email;
    this.perfil = perfil;
    this.ativo = ativo;
    this.alterarSenhaObrigatorio = false;
  }
}
