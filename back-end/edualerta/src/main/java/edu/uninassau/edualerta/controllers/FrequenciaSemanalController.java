
package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.FrequenciaSemanalCadastroDto;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalResponseDto;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalUpdateDto;
import edu.uninassau.edualerta.services.interfaces.FrequenciaSemanalServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fechamentos-semanais/{fechamentoId}/frequencias")
@RequiredArgsConstructor
public class FrequenciaSemanalController {

  private final FrequenciaSemanalServiceInterface frequenciaService;

  @PostMapping
  public ResponseEntity<FrequenciaSemanalResponseDto>
  cadastrar(@PathVariable Long fechamentoId,
            @RequestBody @Valid FrequenciaSemanalCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    FrequenciaSemanalResponseDto resposta =
        frequenciaService.cadastrar(fechamentoId, dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<List<FrequenciaSemanalResponseDto>>
  listar(@PathVariable Long fechamentoId, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(frequenciaService.listar(fechamentoId, jwt));
  }

  @PutMapping("/{frequenciaId}")
  public ResponseEntity<FrequenciaSemanalResponseDto>
  atualizar(@PathVariable Long fechamentoId, @PathVariable Long frequenciaId,
            @RequestBody @Valid FrequenciaSemanalUpdateDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(
        frequenciaService.atualizar(fechamentoId, frequenciaId, dto, jwt));
  }

  @DeleteMapping("/{frequenciaId}")
  public ResponseEntity<Void> remover(@PathVariable Long fechamentoId,
                                      @PathVariable Long frequenciaId,
                                      @AuthenticationPrincipal Jwt jwt) {

    frequenciaService.remover(fechamentoId, frequenciaId, jwt);

    return ResponseEntity.noContent().build();
  }
}
