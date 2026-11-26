
package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.FrequenciaSemanalCadastroDto;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalResponseDto;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalUpdateDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface FrequenciaSemanalServiceInterface {

  FrequenciaSemanalResponseDto
  cadastrar(Long fechamentoId, FrequenciaSemanalCadastroDto dto, Jwt jwt);

  List<FrequenciaSemanalResponseDto> listar(Long fechamentoId, Jwt jwt);

  FrequenciaSemanalResponseDto atualizar(Long fechamentoId, Long frequenciaId,
                                         FrequenciaSemanalUpdateDto dto,
                                         Jwt jwt);

  void remover(Long fechamentoId, Long frequenciaId, Jwt jwt);
}
