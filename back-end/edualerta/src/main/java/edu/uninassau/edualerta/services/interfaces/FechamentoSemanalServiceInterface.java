package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.FechamentoDiaCadastroDto;
import edu.uninassau.edualerta.dtos.FechamentoSemanalCadastroDto;
import edu.uninassau.edualerta.dtos.FechamentoSemanalResponseDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface FechamentoSemanalServiceInterface {

  FechamentoSemanalResponseDto cadastrar(FechamentoSemanalCadastroDto dto,
                                         Jwt jwt);

  List<FechamentoSemanalResponseDto> listar(Long turmaId, Long periodoLetivoId,
                                            Jwt jwt);

  FechamentoSemanalResponseDto buscarPorId(Long id, Jwt jwt);

  FechamentoSemanalResponseDto
  adicionarDia(Long fechamentoId, FechamentoDiaCadastroDto dto, Jwt jwt);

  FechamentoSemanalResponseDto atualizarDia(Long fechamentoId, Long diaId,
                                            FechamentoDiaCadastroDto dto,
                                            Jwt jwt);

  void removerDia(Long fechamentoId, Long diaId, Jwt jwt);

  FechamentoSemanalResponseDto fechar(Long fechamentoId, Jwt jwt);
}
