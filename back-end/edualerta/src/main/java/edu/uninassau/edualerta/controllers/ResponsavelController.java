package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.*;
import edu.uninassau.edualerta.services.interfaces.ResponsavelServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/responsaveis")
@RequiredArgsConstructor
public class ResponsavelController {
  private final ResponsavelServiceInterface service;

  @PostMapping
  public ResponseEntity<ResponsavelResponseDto>
  cadastrar(@RequestBody @Valid ResponsavelCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.cadastrar(dto, jwt));
  }

  @GetMapping
  public ResponseEntity<List<ResponsavelResponseDto>>
  listar(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(service.listar(jwt));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponsavelResponseDto>
  atualizar(@PathVariable Long id,
            @RequestBody @Valid ResponsavelCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(service.atualizar(id, dto, jwt));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<Void> status(@PathVariable Long id,
                                     @RequestParam boolean ativo,
                                     @AuthenticationPrincipal Jwt jwt) {
    service.alterarStatus(id, ativo, jwt);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/alunos/{alunoId}")
  public ResponseEntity<List<AlunoResponsavelResponseDto>>
  listarPorAluno(@PathVariable Long alunoId, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(service.listarPorAluno(alunoId, jwt));
  }

  @PostMapping("/alunos/{alunoId}")
  public ResponseEntity<AlunoResponsavelResponseDto>
  associar(@PathVariable Long alunoId,
           @RequestBody @Valid AssociacaoResponsavelDto dto,
           @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.associar(alunoId, dto, jwt));
  }

  @DeleteMapping("/alunos/{alunoId}/{responsavelId}")
  public ResponseEntity<Void> desassociar(@PathVariable Long alunoId,
                                          @PathVariable Long responsavelId,
                                          @AuthenticationPrincipal Jwt jwt) {
    service.desassociar(alunoId, responsavelId, jwt);
    return ResponseEntity.noContent().build();
  }
}
