package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioCadastroDto {

  @NotBlank(message = "Nome não pode ser vazio")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  private String nome;

  @NotBlank(message = "E-mail não pode ser vazio")
  @Email(message = "E-mail inválido")
  @Size(max = 254, message = "E-mail deve ter no máximo 254 caracteres")
  private String email;
}
