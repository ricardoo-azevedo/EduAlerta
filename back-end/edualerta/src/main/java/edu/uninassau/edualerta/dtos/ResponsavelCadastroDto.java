package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsavelCadastroDto {
  @NotBlank @Size(max = 150) private String nome;
  @NotBlank @Email @Size(max = 254) private String email;
  @Size(max = 20) private String telefone;
  @Size(max = 50) private String parentesco;
}
