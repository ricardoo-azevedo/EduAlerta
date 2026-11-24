
package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.AcompanhamentoCadastroDto;
import edu.uninassau.edualerta.dtos.AcompanhamentoResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AcompanhamentoServiceInterface {

  AcompanhamentoResponseDto cadastrar(Long alertaId,
                                      AcompanhamentoCadastroDto dto, Jwt jwt);
}
