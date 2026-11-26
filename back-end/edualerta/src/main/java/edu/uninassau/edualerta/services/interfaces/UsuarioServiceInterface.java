package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.UsuarioCadastroDto;
import edu.uninassau.edualerta.dtos.UsuarioResponseDto;
import edu.uninassau.edualerta.dtos.UsuarioSenhaUpdateDto;
import edu.uninassau.edualerta.dtos.UsuarioStatusUpdateDto;
import edu.uninassau.edualerta.dtos.UsuarioUpdateDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UsuarioServiceInterface {

  UsuarioResponseDto cadastrar(UsuarioCadastroDto dto, Jwt jwt);

  UsuarioResponseDto me(Jwt jwt);

  UsuarioResponseDto atualizarUsuario(Jwt jwt, UsuarioUpdateDto dto);

  void atualizarSenha(Jwt jwt, UsuarioSenhaUpdateDto dto);

  void deletarConta(Jwt jwt);

  List<UsuarioResponseDto> listar(Jwt jwt);

  List<UsuarioResponseDto> buscarPorNome(String nome, Jwt jwt);

  UsuarioResponseDto alterarNomeSecretaria(Long id, UsuarioUpdateDto dto,
      Jwt jwt);

  UsuarioResponseDto alterarStatusSecretaria(Long id, UsuarioStatusUpdateDto dto, Jwt jwt);
}
