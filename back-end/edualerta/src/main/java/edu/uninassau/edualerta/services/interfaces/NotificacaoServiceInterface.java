package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.NotificacaoResponseDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface NotificacaoServiceInterface {
  List<NotificacaoResponseDto> enviarParaResponsaveis(Long alertaId, Jwt jwt);

  List<NotificacaoResponseDto> listarPorAlerta(Long alertaId, Jwt jwt);

  NotificacaoResponseDto reenviar(Long notificacaoId, Jwt jwt);
}
