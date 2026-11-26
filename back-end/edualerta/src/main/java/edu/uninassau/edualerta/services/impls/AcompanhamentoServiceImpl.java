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
import java.time.LocalDateTime;
import java.util.List;
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

    Alerta alerta = buscarAlerta(alertaId);

    LocalDateTime agora = LocalDateTime.now();

    Acompanhamento acompanhamento = new Acompanhamento(
        alerta, usuario, dto.getTipoIntervencao().trim(),
        dto.getDescricao().trim(), dto.getResultado().trim());

    acompanhamento.setDataHora(agora);

    Acompanhamento salvo =
        acompanhamentoRepository.saveAndFlush(acompanhamento);

    alerta.setSituacao(SituacaoAlerta.RESOLVIDO);
    alerta.setResolvidoManualmente(true);
    alerta.setAtualizadoEm(agora);

    return converterParaDto(salvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AcompanhamentoResponseDto> listarPorAlerta(Long alertaId,
                                                         Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuario(jwt);
    validarPermissao(usuario);

    buscarAlerta(alertaId);

    return acompanhamentoRepository
        .findByAlertaIdOrderByDataHoraDescIdDesc(alertaId)
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  private Alerta buscarAlerta(Long alertaId) {
    return alertaRepository.findById(alertaId).orElseThrow(
        () -> new EntityNotFoundException("Alerta não encontrado"));
  }

  private void validarPermissao(Usuario usuario) {
    if (usuario.getPerfil() != Perfil.ADM &&
        usuario.getPerfil() != Perfil.SECRETARIA) {

      throw new AccessDeniedException("Seu perfil não possui permissão para "
                                      + "consultar ou registrar intervenções");
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
