package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.AlertaResponseDto;
import edu.uninassau.edualerta.models.NivelAlerta;
import edu.uninassau.edualerta.models.SituacaoAlerta;
import edu.uninassau.edualerta.services.interfaces.AlertaServiceInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
         @RequestParam(required = false) NivelAlerta nivel,
         @RequestParam(required = false) SituacaoAlerta situacao,
         @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(
        alertaService.listar(alunoId, periodoLetivoId, nivel, situacao, jwt));
  }

  @PutMapping("/{id}/resolver")
  public ResponseEntity<AlertaResponseDto>
  resolver(@PathVariable("id") Long alertaId,
           @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(alertaService.resolver(alertaId, jwt));
  }
}
