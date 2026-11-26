
package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcompanhamentoCadastroDto {

  @NotBlank(message = "O tipo de intervenção é obrigatório")
  @Size(max = 50,
        message = "O tipo de intervenção deve ter no máximo 50 caracteres")
  private String tipoIntervencao;

  @NotBlank(message = "A descrição da intervenção é obrigatória")
  @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
  private String descricao;

  @NotBlank(message = "O resultado da intervenção é obrigatório")
  @Size(max = 1000, message = "O resultado deve ter no máximo 1000 caracteres")
  private String resultado;
}
