package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatriculaCadastroDto {

  @NotNull(message = "O ID do aluno deve ser informado")
  private Long alunoId;

  @NotNull(message = "O ID da turma deve ser informado")
  private Long turmaId;

  @NotNull(message = "A data de início deve ser informada")
  private LocalDate dataInicio;
}
