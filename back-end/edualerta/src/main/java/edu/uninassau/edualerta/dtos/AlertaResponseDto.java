package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.NivelAlerta;
import edu.uninassau.edualerta.models.SituacaoAlerta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertaResponseDto {

  private Long id;
  private Long alunoId;
  private String alunoNome;
  private Long periodoLetivoId;
  private String periodoLetivoNome;
  private BigDecimal percentualFaltas;
  private NivelAlerta nivel;
  private SituacaoAlerta situacao;
  private String motivo;
  private LocalDateTime atualizadoEm;
}
