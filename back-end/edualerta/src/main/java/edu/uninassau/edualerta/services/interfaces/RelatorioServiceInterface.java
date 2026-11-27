
package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.RelatorioAlunoResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface RelatorioServiceInterface {

  RelatorioAlunoResponseDto gerarRelatorioAluno(Long alunoId, Jwt jwt);
}
