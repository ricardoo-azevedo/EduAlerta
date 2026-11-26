package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.AlertaResponseDto;
import edu.uninassau.edualerta.models.NivelAlerta;
import edu.uninassau.edualerta.models.SituacaoAlerta;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AlertaServiceInterface {

  List<AlertaResponseDto> listar(Long alunoId, Long periodoLetivoId,
                                 NivelAlerta nivel, SituacaoAlerta situacao,
                                 Jwt jwt);

  AlertaResponseDto resolver(Long alertaId, Jwt jwt);

  void atualizarAlertaDoAluno(Long alunoId, Long periodoLetivoId);
}
