
package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.RelatorioAlunoResponseDto;
import edu.uninassau.edualerta.services.interfaces.RelatorioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

  private final RelatorioServiceInterface relatorioService;

  @GetMapping("/alunos/{alunoId}")
  public ResponseEntity<RelatorioAlunoResponseDto>
  gerarRelatorioAluno(@PathVariable Long alunoId,
                      @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(
        relatorioService.gerarRelatorioAluno(alunoId, jwt));
  }
}
