package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.*;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface ResponsavelServiceInterface {
  ResponsavelResponseDto cadastrar(ResponsavelCadastroDto dto, Jwt jwt);

  List<ResponsavelResponseDto> listar(Jwt jwt);

  ResponsavelResponseDto atualizar(Long id, ResponsavelCadastroDto dto,
      Jwt jwt);

  void alterarStatus(Long id, boolean ativo, Jwt jwt);

  List<AlunoResponsavelResponseDto> listarPorAluno(Long alunoId, Jwt jwt);

  AlunoResponsavelResponseDto associar(Long alunoId,
      AssociacaoResponsavelDto dto, Jwt jwt);

  void desassociar(Long alunoId, Long responsavelId, Jwt jwt);
}
