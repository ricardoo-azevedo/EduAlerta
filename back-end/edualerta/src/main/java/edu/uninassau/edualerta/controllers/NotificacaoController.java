package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.NotificacaoResponseDto;
import edu.uninassau.edualerta.services.interfaces.NotificacaoServiceInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alertas/{alertaId}/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
  private final NotificacaoServiceInterface service;

  @PostMapping
  public ResponseEntity<List<NotificacaoResponseDto>>
  enviar(@PathVariable Long alertaId, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.enviarParaResponsaveis(alertaId, jwt));
  }

  @GetMapping
  public ResponseEntity<List<NotificacaoResponseDto>>
  listar(@PathVariable Long alertaId, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(service.listarPorAlerta(alertaId, jwt));
  }
}
