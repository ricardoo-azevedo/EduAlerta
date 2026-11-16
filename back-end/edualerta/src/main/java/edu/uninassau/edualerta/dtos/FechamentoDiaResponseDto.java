package edu.uninassau.edualerta.dtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoDiaResponseDto {

  private Long id;

  private LocalDate data;

  private Integer quantidadeAulas;
}
