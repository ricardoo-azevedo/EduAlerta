package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.TurmaCadastroDto;
import edu.uninassau.edualerta.dtos.TurmaResponseDto;
import edu.uninassau.edualerta.dtos.TurmaStatusDto;
import edu.uninassau.edualerta.dtos.TurmaUpdateDto;
import edu.uninassau.edualerta.services.interfaces.TurmaServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {

  private final TurmaServiceInterface turmaService;

  @PostMapping
  public ResponseEntity<TurmaResponseDto>
  cadastrar(@RequestBody @Valid TurmaCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    TurmaResponseDto resposta = turmaService.cadastrar(dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<List<TurmaResponseDto>>
  listar(@AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(turmaService.listar(jwt));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TurmaResponseDto>
  buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(turmaService.buscarPorId(id, jwt));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TurmaResponseDto>
  atualizar(@PathVariable Long id, @RequestBody @Valid TurmaUpdateDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(turmaService.atualizar(id, dto, jwt));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<TurmaResponseDto>
  alterarStatus(@PathVariable Long id, @RequestBody @Valid TurmaStatusDto dto,
                @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(turmaService.alterarStatus(id, dto, jwt));
  }
}
