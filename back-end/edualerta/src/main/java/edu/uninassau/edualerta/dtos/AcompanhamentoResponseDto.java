
package edu.uninassau.edualerta.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcompanhamentoResponseDto {

  private Long id;
  private Long alertaId;
  private Long usuarioId;
  private String usuarioNome;
  private LocalDateTime dataHora;
  private String tipoIntervencao;
  private String descricao;
  private String resultado;
}
