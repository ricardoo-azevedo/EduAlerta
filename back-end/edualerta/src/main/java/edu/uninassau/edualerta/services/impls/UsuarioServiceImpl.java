package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.UsuarioCadastroDto;
import edu.uninassau.edualerta.dtos.UsuarioResponseDto;
import edu.uninassau.edualerta.dtos.UsuarioSenhaUpdateDto;
import edu.uninassau.edualerta.dtos.UsuarioUpdateDto;
import edu.uninassau.edualerta.models.Perfil;
import edu.uninassau.edualerta.models.Usuario;
import edu.uninassau.edualerta.repositorys.UsuarioRepository;
import edu.uninassau.edualerta.services.interfaces.UsuarioServiceInterface;
import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioServiceInterface {

  private final UsuarioRepository usuarioRepository;
  private final UsuarioContext usuarioContext;
  private final SupabaseAuthService supabaseAuthService;
  private final EmailService emailService;

  private static final SecureRandom RANDOM = new SecureRandom();

  private static final String CARACTERES_SENHA =
      "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%*-_";

  @Override
  public UsuarioResponseDto cadastrar(UsuarioCadastroDto dto, Jwt jwt) {

    Usuario secretariaLogada = usuarioContext.getUsuario(jwt);

    if (secretariaLogada.getPerfil() != Perfil.SECRETARIA) {
      throw new AccessDeniedException(
          "Somente uma secretaria pode cadastrar outra secretaria");
    }

    String nome = dto.getNome().trim();
    String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);

    if (usuarioRepository.existsByEmailIgnoreCase(email)) {
      throw new IllegalArgumentException(
          "Já existe um usuário cadastrado com esse e-mail");
    }

    // Valida as integrações antes de criar a conta externa.
    supabaseAuthService.validarConfiguracaoCriacaoUsuario();
    emailService.validarConfiguracao();

    String senhaTemporaria = gerarSenhaTemporaria();

    UUID authUserId =
        supabaseAuthService.criarUsuario(nome, email, senhaTemporaria);

    try {

      Usuario novoUsuario =
          new Usuario(authUserId, nome, email, Perfil.SECRETARIA, true);

      novoUsuario.setAlterarSenhaObrigatorio(true);

      Usuario usuarioSalvo = usuarioRepository.saveAndFlush(novoUsuario);

      emailService.enviarSenhaTemporaria(nome, email, senhaTemporaria);

      return converterParaDto(usuarioSalvo);

    } catch (RuntimeException e) {

      try {
        supabaseAuthService.excluirUsuario(authUserId);
      } catch (RuntimeException erroExclusao) {
        e.addSuppressed(erroExclusao);
      }

      throw e;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UsuarioResponseDto me(Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuarioParaTrocarSenha(jwt);

    return converterParaDto(usuario);
  }

  @Override
  public UsuarioResponseDto atualizarUsuario(Jwt jwt, UsuarioUpdateDto dto) {

    Usuario usuario = usuarioContext.getUsuario(jwt);

    usuario.setNome(dto.getNome().trim());

    return converterParaDto(usuario);
  }

  @Override
  public void atualizarSenha(Jwt jwt, UsuarioSenhaUpdateDto dto) {

    Usuario usuario = usuarioContext.getUsuarioParaTrocarSenha(jwt);

    if (!usuario.isAlterarSenhaObrigatorio()) {
      throw new IllegalArgumentException(
          "Sua senha já foi alterada anteriormente");
    }

    supabaseAuthService.atualizarSenha(jwt.getTokenValue(), dto.getNovaSenha());

    usuario.setAlterarSenhaObrigatorio(false);
  }

  @Override
  public void deletarConta(Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuario(jwt);

    usuario.setAtivo(false);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UsuarioResponseDto> listar(Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return usuarioRepository.findAll()
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UsuarioResponseDto> buscarPorNome(String nome, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    if (nome == null || nome.isBlank()) {
      throw new IllegalArgumentException(
          "O nome para pesquisa não pode ser vazio");
    }

    return usuarioRepository.findByNomeContainingIgnoreCase(nome.trim())
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  private String gerarSenhaTemporaria() {

    StringBuilder senha = new StringBuilder();

    for (int i = 0; i < 18; i++) {
      int indice = RANDOM.nextInt(CARACTERES_SENHA.length());
      senha.append(CARACTERES_SENHA.charAt(indice));
    }

    return senha.toString();
  }

  private UsuarioResponseDto converterParaDto(Usuario usuario) {

    return new UsuarioResponseDto(usuario.getId(), usuario.getNome(),
                                  usuario.getEmail(), usuario.getPerfil(),
                                  usuario.isAtivo(),
                                  usuario.isAlterarSenhaObrigatorio());
  }
}
