package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.AlertaResponseDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AlertaServiceInterface {

  List<AlertaResponseDto> listar(Long alunoId, Long periodoLetivoId, Jwt jwt);

  void atualizarAlertaDoAluno(Long alunoId, Long periodoLetivoId);
}
