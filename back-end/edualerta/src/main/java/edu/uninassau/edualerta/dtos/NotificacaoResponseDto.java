package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.StatusNotificacao;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificacaoResponseDto {
  private Long id;
  private Long alertaId;
  private Long responsavelId;
  private String responsavelNome;
  private String emailDestino;
  private String assunto;
  private StatusNotificacao status;
  private String erroEnvio;
  private LocalDateTime criadoEm;
  private LocalDateTime enviadoEm;
}
