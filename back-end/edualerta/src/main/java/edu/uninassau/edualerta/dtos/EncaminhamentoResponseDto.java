package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.StatusEncaminhamento;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EncaminhamentoResponseDto {
  private Long id;
  private Long alertaId;
  private Long alunoId;
  private String alunoNome;
  private String servicoDestino;
  private String finalidade;
  private String observacao;
  private StatusEncaminhamento status;
  private LocalDate dataEncaminhamento;
  private LocalDateTime criadoEm;
}
