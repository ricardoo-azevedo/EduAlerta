package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioSenhaUpdateDto {

  @NotBlank(message = "Nova senha não pode ser vazia")
  @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
  private String novaSenha;
}
