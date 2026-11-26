package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.StatusMatricula;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatriculaResponseDto {

  private Long id;
  private Long alunoId;
  private String alunoNome;
  private Long turmaId;
  private String turmaNome;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private StatusMatricula status;
}
