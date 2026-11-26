package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.StatusFechamento;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoSemanalResponseDto {

  private Long id;

  private Long turmaId;

  private String turmaNome;

  private Long periodoLetivoId;

  private String periodoLetivoNome;

  private Long usuarioId;

  private String usuarioNome;

  private LocalDate dataInicioSemana;

  private LocalDate dataFimSemana;

  private StatusFechamento status;

  private List<FechamentoDiaResponseDto> dias;
}
