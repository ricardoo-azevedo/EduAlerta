package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.StatusEncaminhamento;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncaminhamentoCadastroDto {
  @NotBlank @Size(max = 150) private String servicoDestino;
  @NotBlank @Size(max = 150) private String finalidade;
  @NotBlank @Size(max = 1000) private String observacao;
  private LocalDate dataEncaminhamento;
  private StatusEncaminhamento status;
}
