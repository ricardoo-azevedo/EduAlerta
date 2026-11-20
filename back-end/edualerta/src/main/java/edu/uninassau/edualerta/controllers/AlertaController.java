package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.AlertaResponseDto;
import edu.uninassau.edualerta.services.interfaces.AlertaServiceInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alertas")
@RequiredArgsConstructor
public class AlertaController {

  private final AlertaServiceInterface alertaService;

  @GetMapping
  public ResponseEntity<List<AlertaResponseDto>>
  listar(@RequestParam(required = false) Long alunoId,
         @RequestParam(required = false) Long periodoLetivoId,
         @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(
        alertaService.listar(alunoId, periodoLetivoId, jwt));
  }
}
