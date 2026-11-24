
package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.AcompanhamentoCadastroDto;
import edu.uninassau.edualerta.dtos.AcompanhamentoResponseDto;
import edu.uninassau.edualerta.services.interfaces.AcompanhamentoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alertas/{alertaId}/acompanhamentos")
@RequiredArgsConstructor
public class AcompanhamentoController {

  private final AcompanhamentoServiceInterface acompanhamentoService;

  @PostMapping
  public ResponseEntity<AcompanhamentoResponseDto>
  cadastrar(@PathVariable Long alertaId,
            @RequestBody @Valid AcompanhamentoCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    AcompanhamentoResponseDto resposta =
        acompanhamentoService.cadastrar(alertaId, dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }
}
