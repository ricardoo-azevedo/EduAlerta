package edu.uninassau.edualerta.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlunoResponsavelResponseDto {
  private Long associacaoId;
  private Long alunoId;
  private Long responsavelId;
  private String responsavelNome;
  private String email;
  private String telefone;
  private String parentesco;
  private boolean responsavelPrincipal;
}
