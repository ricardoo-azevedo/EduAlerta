package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.*;
import edu.uninassau.edualerta.models.*;
import edu.uninassau.edualerta.repositorys.*;
import edu.uninassau.edualerta.services.interfaces.EncaminhamentoServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EncaminhamentoServiceImpl
    implements EncaminhamentoServiceInterface {
  private final EncaminhamentoRepository repository;
  private final AlertaRepository alertaRepository;
  private final UsuarioContext usuarioContext;

  public EncaminhamentoResponseDto cadastrar(Long alertaId, EncaminhamentoCadastroDto dto, Jwt jwt) {
    validarAcesso(jwt);
    Alerta a = alertaRepository.findById(alertaId).orElseThrow(
        () -> new EntityNotFoundException("Alerta não encontrado"));
    Encaminhamento e = new Encaminhamento(
        a, dto.getServicoDestino().trim(), dto.getFinalidade().trim(),
        dto.getObservacao().trim(),
        dto.getDataEncaminhamento() == null ? LocalDate.now()
            : dto.getDataEncaminhamento(),
        dto.getStatus());
    return converter(repository.save(e));
  }

  @Transactional(readOnly = true)
  public List<EncaminhamentoResponseDto> listarPorAlerta(Long alertaId,
      Jwt jwt) {
    validarAcesso(jwt);
    if (!alertaRepository.existsById(alertaId))
      throw new EntityNotFoundException("Alerta não encontrado");
    return repository
        .findByAlertaIdOrderByDataEncaminhamentoDescIdDesc(alertaId)
        .stream()
        .map(this::converter)
        .toList();
  }

  public EncaminhamentoResponseDto atualizarStatus(Long alertaId, Long id,
      StatusEncaminhamento status,
      Jwt jwt) {
    validarAcesso(jwt);
    if (status == null)
      throw new IllegalArgumentException("A situação é obrigatória");
    Encaminhamento e = repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Encaminhamento não encontrado"));
    if (!e.getAlerta().getId().equals(alertaId))
      throw new EntityNotFoundException(
          "Encaminhamento não encontrado para este alerta");
    e.setStatus(status);
    return converter(e);
  }

  private void validarAcesso(Jwt jwt) {
    Usuario u = usuarioContext.getUsuario(jwt);
    if (u.getPerfil() != Perfil.ADM && u.getPerfil() != Perfil.SECRETARIA)
      throw new AccessDeniedException(
          "Seu perfil não pode gerenciar encaminhamentos");
  }

  private EncaminhamentoResponseDto converter(Encaminhamento e) {
    return new EncaminhamentoResponseDto(
        e.getId(), e.getAlerta().getId(), e.getAlerta().getAluno().getId(),
        e.getAlerta().getAluno().getNome(), e.getServicoDestino(),
        e.getFinalidade(), e.getObservacao(), e.getStatus(),
        e.getDataEncaminhamento(), e.getCriadoEm());
  }
}
