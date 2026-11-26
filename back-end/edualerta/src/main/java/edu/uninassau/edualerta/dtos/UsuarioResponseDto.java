package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioResponseDto {

  private Long id;

  private String nome;

  private String email;

  private Perfil perfil;

  private boolean ativo;

  private boolean alterarSenhaObrigatorio;
}
