
package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FrequenciaSemanalUpdateDto {

  @NotNull(message = "A quantidade de faltas é obrigatória")
  @Min(value = 0, message = "Faltas não podem ser negativas")
  private Integer faltas;

  @NotNull(message = "A quantidade de faltas justificadas é obrigatória")
  @Min(value = 0, message = "Faltas justificadas não podem ser negativas")
  private Integer faltasJustificadas;
}
