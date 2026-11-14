package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurmaCadastroDto {

  @NotBlank(message = "Nome não pode ser vazio")
  @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
  private String nome;

  @NotNull(message = "Ano letivo deve ser informado")
  @Positive(message = "Ano letivo deve ser positivo")
  private Integer anoLetivo;
}
