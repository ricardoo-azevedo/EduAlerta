package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.FrequenciaAcumuladaResponseDto;
import edu.uninassau.edualerta.dtos.MatriculaCadastroDto;
import edu.uninassau.edualerta.dtos.MatriculaEncerramentoDto;
import edu.uninassau.edualerta.dtos.MatriculaResponseDto;
import edu.uninassau.edualerta.dtos.MatriculaTransferenciaDto;
import edu.uninassau.edualerta.services.interfaces.MatriculaServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

  private final MatriculaServiceInterface matriculaService;

  @PostMapping
  public ResponseEntity<MatriculaResponseDto>
  cadastrar(@RequestBody @Valid MatriculaCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {
    MatriculaResponseDto resposta = matriculaService.cadastrar(dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<List<MatriculaResponseDto>>
  listar(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(matriculaService.listar(jwt));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MatriculaResponseDto>
  buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(matriculaService.buscarPorId(id, jwt));
  }

  @GetMapping("/aluno/{alunoId}")
  public ResponseEntity<List<MatriculaResponseDto>>
  listarPorAluno(@PathVariable Long alunoId, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(matriculaService.listarPorAluno(alunoId, jwt));
  }

  @GetMapping("/{id}/frequencia-acumulada")
  public ResponseEntity<FrequenciaAcumuladaResponseDto>
  calcularFrequenciaAcumulada(@PathVariable Long id,
                              @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(
        matriculaService.calcularFrequenciaAcumulada(id, jwt));
  }

  @PutMapping("/{id}/encerramento")
  public ResponseEntity<MatriculaResponseDto>
  encerrar(@PathVariable Long id,
           @RequestBody @Valid MatriculaEncerramentoDto dto,
           @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(matriculaService.encerrar(id, dto, jwt));
  }

  @PostMapping("/{id}/transferencia")
  public ResponseEntity<MatriculaResponseDto>
  transferir(@PathVariable Long id,
             @RequestBody @Valid MatriculaTransferenciaDto dto,
             @AuthenticationPrincipal Jwt jwt) {
    MatriculaResponseDto resposta = matriculaService.transferir(id, dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }
}
