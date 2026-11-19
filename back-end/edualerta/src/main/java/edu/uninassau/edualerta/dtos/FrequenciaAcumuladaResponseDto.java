package edu.uninassau.edualerta.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FrequenciaAcumuladaResponseDto {

  private Long matriculaId;
  private Long alunoId;
  private String alunoNome;
  private Long faltasNaoJustificadas;
  private Long aulasRealizadas;
  private BigDecimal percentualFaltas;
}
