package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.NotificacaoResponseDto;
import edu.uninassau.edualerta.services.interfaces.NotificacaoServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoReenvioController {
  private final NotificacaoServiceInterface service;

  @PostMapping("/{id}/reenviar")
  public ResponseEntity<NotificacaoResponseDto>
  reenviar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(service.reenviar(id, jwt));
  }
}
