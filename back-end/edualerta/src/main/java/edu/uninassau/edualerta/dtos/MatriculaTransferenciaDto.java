package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatriculaTransferenciaDto {

  @NotNull(message = "O ID da turma de destino deve ser informado")
  private Long turmaId;

  @NotNull(message = "A data da transferência deve ser informada")
  private LocalDate dataTransferencia;
}
