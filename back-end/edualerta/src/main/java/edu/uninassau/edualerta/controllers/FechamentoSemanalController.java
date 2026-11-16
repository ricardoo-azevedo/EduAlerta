package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.FechamentoDiaCadastroDto;
import edu.uninassau.edualerta.dtos.FechamentoSemanalCadastroDto;
import edu.uninassau.edualerta.dtos.FechamentoSemanalResponseDto;
import edu.uninassau.edualerta.services.interfaces.FechamentoSemanalServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fechamentos-semanais")
@RequiredArgsConstructor
public class FechamentoSemanalController {

  private final FechamentoSemanalServiceInterface fechamentoService;

  @PostMapping
  public ResponseEntity<FechamentoSemanalResponseDto>
  cadastrar(@RequestBody @Valid FechamentoSemanalCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    FechamentoSemanalResponseDto resposta =
        fechamentoService.cadastrar(dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<List<FechamentoSemanalResponseDto>>
  listar(@RequestParam(required = false) Long turmaId,
         @RequestParam(required = false) Long periodoLetivoId,
         @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(
        fechamentoService.listar(turmaId, periodoLetivoId, jwt));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FechamentoSemanalResponseDto>
  buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(fechamentoService.buscarPorId(id, jwt));
  }

  @PostMapping("/{id}/dias")
  public ResponseEntity<FechamentoSemanalResponseDto>
  adicionarDia(@PathVariable Long id,
               @RequestBody @Valid FechamentoDiaCadastroDto dto,
               @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(fechamentoService.adicionarDia(id, dto, jwt));
  }

  @PutMapping("/{id}/dias/{diaId}")
  public ResponseEntity<FechamentoSemanalResponseDto>
  atualizarDia(@PathVariable Long id, @PathVariable Long diaId,
               @RequestBody @Valid FechamentoDiaCadastroDto dto,
               @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(
        fechamentoService.atualizarDia(id, diaId, dto, jwt));
  }

  @DeleteMapping("/{id}/dias/{diaId}")
  public ResponseEntity<Void> removerDia(@PathVariable Long id,
                                         @PathVariable Long diaId,
                                         @AuthenticationPrincipal Jwt jwt) {

    fechamentoService.removerDia(id, diaId, jwt);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/fechar")
  public ResponseEntity<FechamentoSemanalResponseDto>
  fechar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(fechamentoService.fechar(id, jwt));
  }
}
