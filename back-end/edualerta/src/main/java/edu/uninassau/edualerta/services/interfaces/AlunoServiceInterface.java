package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.AlunoCadastroDto;
import edu.uninassau.edualerta.dtos.AlunoResponseDto;
import edu.uninassau.edualerta.dtos.AlunoStatusDto;
import edu.uninassau.edualerta.dtos.AlunoUpdateDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AlunoServiceInterface {

  AlunoResponseDto cadastrar(AlunoCadastroDto dto, Jwt jwt);

  List<AlunoResponseDto> listar(Jwt jwt);

  AlunoResponseDto buscarPorId(Long id, Jwt jwt);

  AlunoResponseDto atualizar(Long id, AlunoUpdateDto dto, Jwt jwt);

  AlunoResponseDto alterarStatus(Long id, AlunoStatusDto dto, Jwt jwt);
}
