package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.*;
import edu.uninassau.edualerta.models.StatusEncaminhamento;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface EncaminhamentoServiceInterface {
  EncaminhamentoResponseDto cadastrar(Long alertaId,
      EncaminhamentoCadastroDto dto, Jwt jwt);

  List<EncaminhamentoResponseDto> listarPorAlerta(Long alertaId, Jwt jwt);

  EncaminhamentoResponseDto atualizarStatus(Long alertaId, Long id, StatusEncaminhamento status, Jwt jwt);
}
