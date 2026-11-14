package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.PeriodoLetivoCadastroDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoResponseDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoStatusDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoUpdateDto;
import edu.uninassau.edualerta.services.interfaces.PeriodoLetivoServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodos-letivos")
@RequiredArgsConstructor
public class PeriodoLetivoController {

  private final PeriodoLetivoServiceInterface periodoLetivoService;

  @PostMapping
  public ResponseEntity<PeriodoLetivoResponseDto>
  cadastrar(@RequestBody @Valid PeriodoLetivoCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    PeriodoLetivoResponseDto resposta =
        periodoLetivoService.cadastrar(dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<List<PeriodoLetivoResponseDto>>
  listar(@AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(periodoLetivoService.listar(jwt));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PeriodoLetivoResponseDto>
  buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(periodoLetivoService.buscarPorId(id, jwt));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PeriodoLetivoResponseDto>
  atualizar(@PathVariable Long id,
            @RequestBody @Valid PeriodoLetivoUpdateDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(periodoLetivoService.atualizar(id, dto, jwt));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<PeriodoLetivoResponseDto>
  alterarStatus(@PathVariable Long id,
                @RequestBody @Valid PeriodoLetivoStatusDto dto,
                @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(periodoLetivoService.alterarStatus(id, dto, jwt));
  }
}
