package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.TurmaCadastroDto;
import edu.uninassau.edualerta.dtos.TurmaResponseDto;
import edu.uninassau.edualerta.dtos.TurmaStatusDto;
import edu.uninassau.edualerta.dtos.TurmaUpdateDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface TurmaServiceInterface {

  TurmaResponseDto cadastrar(TurmaCadastroDto dto, Jwt jwt);

  List<TurmaResponseDto> listar(Jwt jwt);

  TurmaResponseDto buscarPorId(Long id, Jwt jwt);

  TurmaResponseDto atualizar(Long id, TurmaUpdateDto dto, Jwt jwt);

  TurmaResponseDto alterarStatus(Long id, TurmaStatusDto dto, Jwt jwt);
}
