package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.AlunoCadastroDto;
import edu.uninassau.edualerta.dtos.AlunoResponseDto;
import edu.uninassau.edualerta.dtos.AlunoStatusDto;
import edu.uninassau.edualerta.dtos.AlunoUpdateDto;
import edu.uninassau.edualerta.services.interfaces.AlunoServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

  private final AlunoServiceInterface alunoService;

  @PostMapping
  public ResponseEntity<AlunoResponseDto> cadastrar(@RequestBody @Valid AlunoCadastroDto dto,
      @AuthenticationPrincipal Jwt jwt) {

    AlunoResponseDto resposta = alunoService.cadastrar(dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<List<AlunoResponseDto>> listar(@AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(alunoService.listar(jwt));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AlunoResponseDto> buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(alunoService.buscarPorId(id, jwt));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AlunoResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid AlunoUpdateDto dto,
      @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(alunoService.atualizar(id, dto, jwt));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<AlunoResponseDto> alterarStatus(@PathVariable Long id, @RequestBody @Valid AlunoStatusDto dto,
      @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(alunoService.alterarStatus(id, dto, jwt));
  }
}
