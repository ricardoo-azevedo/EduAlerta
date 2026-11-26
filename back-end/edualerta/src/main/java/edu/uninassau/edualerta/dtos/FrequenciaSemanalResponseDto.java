
package edu.uninassau.edualerta.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FrequenciaSemanalResponseDto {

  private Long id;

  private Long matriculaId;

  private Long alunoId;

  private String alunoNome;

  private Integer faltas;

  private Integer faltasJustificadas;
}
