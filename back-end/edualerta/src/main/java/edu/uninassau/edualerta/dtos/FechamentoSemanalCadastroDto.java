package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoSemanalCadastroDto {

  @NotNull(message = "A turma é obrigatória") private Long turmaId;

  @NotNull(message = "O período letivo é obrigatório")
  private Long periodoLetivoId;

  @NotNull(message = "A data inicial da semana é obrigatória")
  private LocalDate dataInicioSemana;
}
