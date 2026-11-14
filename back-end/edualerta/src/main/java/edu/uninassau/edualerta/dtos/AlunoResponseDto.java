package edu.uninassau.edualerta.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlunoResponseDto {

  private Long id;

  private String nome;

  private LocalDate dataNascimento;

  private boolean ativo;

  private LocalDateTime criadoEm;
}
