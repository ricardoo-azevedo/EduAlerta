package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.PeriodoLetivoCadastroDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoResponseDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoStatusDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoUpdateDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface PeriodoLetivoServiceInterface {

  PeriodoLetivoResponseDto cadastrar(PeriodoLetivoCadastroDto dto, Jwt jwt);

  List<PeriodoLetivoResponseDto> listar(Jwt jwt);

  PeriodoLetivoResponseDto buscarPorId(Long id, Jwt jwt);

  PeriodoLetivoResponseDto atualizar(Long id, PeriodoLetivoUpdateDto dto,
                                     Jwt jwt);

  PeriodoLetivoResponseDto alterarStatus(Long id, PeriodoLetivoStatusDto dto,
                                         Jwt jwt);
}
