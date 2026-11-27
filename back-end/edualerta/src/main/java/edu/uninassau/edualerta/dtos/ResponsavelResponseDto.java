package edu.uninassau.edualerta.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponsavelResponseDto {
  private Long id;
  private String nome;
  private String email;
  private String telefone;
  private String parentesco;
  private boolean ativo;
}
