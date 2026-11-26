package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioUpdateDto {

  @NotBlank(message = "Nome não pode ser vazio")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  private String nome;
}
