package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PeriodoLetivoUpdateDto {

  @NotBlank(message = "Nome não pode ser vazio")
  @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
  private String nome;

  @NotNull(message = "Ano letivo deve ser informado")
  @Positive(message = "Ano letivo deve ser positivo")
  private Integer anoLetivo;

  @NotNull(message = "Número do semestre deve ser informado")
  @Min(value = 1, message = "Semestre deve ser 1 ou 2")
  @Max(value = 2, message = "Semestre deve ser 1 ou 2")
  private Integer numeroSemestre;

  @NotNull(message = "Data de início deve ser informada")
  private LocalDate dataInicio;

  @NotNull(message = "Data de término deve ser informada")
  private LocalDate dataFim;
}
