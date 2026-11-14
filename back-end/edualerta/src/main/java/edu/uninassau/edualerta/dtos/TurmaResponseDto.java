package edu.uninassau.edualerta.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TurmaResponseDto {

  private Long id;

  private String nome;

  private Integer anoLetivo;

  private boolean ativo;
}
