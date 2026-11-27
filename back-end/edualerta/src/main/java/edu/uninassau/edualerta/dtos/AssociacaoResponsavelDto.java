package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociacaoResponsavelDto {
  @NotNull private Long responsavelId;
  private boolean responsavelPrincipal;
}
