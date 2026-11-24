
package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.AcompanhamentoCadastroDto;
import edu.uninassau.edualerta.dtos.AcompanhamentoResponseDto;
import edu.uninassau.edualerta.models.Acompanhamento;
import edu.uninassau.edualerta.models.Alerta;
import edu.uninassau.edualerta.models.Perfil;
import edu.uninassau.edualerta.models.SituacaoAlerta;
import edu.uninassau.edualerta.models.Usuario;
import edu.uninassau.edualerta.repositorys.AcompanhamentoRepository;
import edu.uninassau.edualerta.repositorys.AlertaRepository;
import edu.uninassau.edualerta.services.interfaces.AcompanhamentoServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcompanhamentoServiceImpl
    implements AcompanhamentoServiceInterface {

  private final AcompanhamentoRepository acompanhamentoRepository;
  private final AlertaRepository alertaRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public AcompanhamentoResponseDto
  cadastrar(Long alertaId, AcompanhamentoCadastroDto dto, Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuario(jwt);

    validarPermissao(usuario);

    Alerta alerta = alertaRepository.findById(alertaId).orElseThrow(
        () -> new EntityNotFoundException("Alerta não encontrado"));

    if (alerta.getSituacao() == SituacaoAlerta.RESOLVIDO) {
      throw new IllegalArgumentException("Esse alerta já está resolvido");
    }

    Acompanhamento acompanhamento = new Acompanhamento(
        alerta, usuario, dto.getTipoIntervencao().trim(),
        dto.getDescricao().trim(), dto.getResultado().trim());

    acompanhamentoRepository.save(acompanhamento);

    alerta.setSituacao(SituacaoAlerta.RESOLVIDO);
    alerta.setResolvidoManualmente(true);
    alerta.setAtualizadoEm(acompanhamento.getDataHora());

    return converterParaDto(acompanhamento);
  }

  private void validarPermissao(Usuario usuario) {

    if (usuario.getPerfil() != Perfil.ADM &&
        usuario.getPerfil() != Perfil.SECRETARIA) {

      throw new AccessDeniedException(
          "Seu perfil não possui permissão para registrar intervenções");
    }
  }

  private AcompanhamentoResponseDto
  converterParaDto(Acompanhamento acompanhamento) {

    return new AcompanhamentoResponseDto(
        acompanhamento.getId(), acompanhamento.getAlerta().getId(),
        acompanhamento.getUsuario().getId(),
        acompanhamento.getUsuario().getNome(), acompanhamento.getDataHora(),
        acompanhamento.getTipoIntervencao(), acompanhamento.getDescricao(),
        acompanhamento.getResultado());
  }
}
