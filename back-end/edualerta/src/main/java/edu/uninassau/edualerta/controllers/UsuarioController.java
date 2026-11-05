package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.UsuarioCadastroDto;
import edu.uninassau.edualerta.dtos.UsuarioResponseDto;
import edu.uninassau.edualerta.dtos.UsuarioSenhaUpdateDto;
import edu.uninassau.edualerta.dtos.UsuarioUpdateDto;
import edu.uninassau.edualerta.services.interfaces.UsuarioServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

  private final UsuarioServiceInterface usuarioService;

  @PostMapping
  public ResponseEntity<UsuarioResponseDto>
  cadastrar(@RequestBody @Valid UsuarioCadastroDto dto,
            @AuthenticationPrincipal Jwt jwt) {

    UsuarioResponseDto resposta = usuarioService.cadastrar(dto, jwt);

    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping("/me")
  public ResponseEntity<UsuarioResponseDto>
  me(@AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(usuarioService.me(jwt));
  }

  @PutMapping("/me")
  public ResponseEntity<UsuarioResponseDto>
  atualizar(@AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid UsuarioUpdateDto dto) {

    return ResponseEntity.ok(usuarioService.atualizarUsuario(jwt, dto));
  }

  @PutMapping("/me/senha")
  public ResponseEntity<Void>
  atualizarSenha(@AuthenticationPrincipal Jwt jwt,
                 @RequestBody @Valid UsuarioSenhaUpdateDto dto) {

    usuarioService.atualizarSenha(jwt, dto);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> deletar(@AuthenticationPrincipal Jwt jwt) {

    usuarioService.deletarConta(jwt);

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<UsuarioResponseDto>>
  listar(@AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(usuarioService.listar(jwt));
  }

  @GetMapping("/buscar")
  public ResponseEntity<List<UsuarioResponseDto>>
  buscarPorNome(@RequestParam String nome, @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(usuarioService.buscarPorNome(nome, jwt));
  }
}
