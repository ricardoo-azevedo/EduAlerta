package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlunoCadastroDto {

  @NotBlank(message = "Nome não pode ser vazio")
  @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
  private String nome;

  @NotNull(message = "Data de nascimento deve ser informada")
  private LocalDate dataNascimento;
}
