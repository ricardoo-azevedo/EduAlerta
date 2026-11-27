package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.*;
import edu.uninassau.edualerta.models.StatusEncaminhamento;
import edu.uninassau.edualerta.services.interfaces.EncaminhamentoServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alertas/{alertaId}/encaminhamentos")
@RequiredArgsConstructor
public class EncaminhamentoController {
  private final EncaminhamentoServiceInterface service;

  @PostMapping
  public ResponseEntity<EncaminhamentoResponseDto>
  cadastrar(@PathVariable Long alertaId,
            @RequestBody @Valid EncaminhamentoCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.cadastrar(alertaId, dto, jwt));
  }

  @GetMapping
  public ResponseEntity<List<EncaminhamentoResponseDto>>
  listar(@PathVariable Long alertaId, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(service.listarPorAlerta(alertaId, jwt));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<EncaminhamentoResponseDto>
  status(@PathVariable Long alertaId, @PathVariable Long id,
         @RequestParam StatusEncaminhamento status,
         @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(
        service.atualizarStatus(alertaId, id, status, jwt));
  }
}
