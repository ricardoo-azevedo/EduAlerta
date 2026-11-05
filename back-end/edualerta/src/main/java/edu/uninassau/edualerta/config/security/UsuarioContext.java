package edu.uninassau.edualerta.config.security;

import edu.uninassau.edualerta.models.Usuario;
import edu.uninassau.edualerta.repositorys.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioContext {

  private final UsuarioRepository usuarioRepository;

  public Usuario getUsuario(Jwt jwt) {

    Usuario usuario = buscarUsuario(jwt);

    if (usuario.isAlterarSenhaObrigatorio()) {
      throw new AccessDeniedException(
          "Você precisa alterar sua senha antes de acessar o sistema");
    }

    return usuario;
  }

  public Usuario getUsuarioParaTrocarSenha(Jwt jwt) {

    return buscarUsuario(jwt);
  }

  private Usuario buscarUsuario(Jwt jwt) {

    if (jwt == null || jwt.getSubject() == null) {
      throw new AccessDeniedException("Usuário não autenticado");
    }

    UUID authUserId;

    try {
      authUserId = UUID.fromString(jwt.getSubject());
    } catch (IllegalArgumentException e) {
      throw new AccessDeniedException("Identificador de usuário inválido");
    }

    Usuario usuario =
        usuarioRepository.findByAuthUserId(authUserId)
            .orElseThrow(()
                             -> new EntityNotFoundException(
                                 "Usuário não encontrado no EduAlerta"));

    if (!usuario.isAtivo()) {
      throw new AccessDeniedException("Sua conta está desativada");
    }

    return usuario;
  }
}
