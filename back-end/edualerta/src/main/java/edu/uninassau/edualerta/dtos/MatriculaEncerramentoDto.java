package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatriculaEncerramentoDto {

  @NotNull(message = "A data de encerramento deve ser informada")
  private LocalDate dataFim;
}
