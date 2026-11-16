package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.Min;
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
public class FechamentoDiaCadastroDto {

  @NotNull(message = "A data é obrigatória") private LocalDate data;

  @NotNull(message = "A quantidade de aulas é obrigatória")
  @Min(value = 0, message = "A quantidade de aulas não pode ser negativa")
  private Integer quantidadeAulas;
}
