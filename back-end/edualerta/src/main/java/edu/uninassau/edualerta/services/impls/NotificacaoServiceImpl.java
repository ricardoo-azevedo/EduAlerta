package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.NotificacaoResponseDto;
import edu.uninassau.edualerta.models.*;
import edu.uninassau.edualerta.repositorys.*;
import edu.uninassau.edualerta.services.interfaces.NotificacaoServiceInterface;
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
public class NotificacaoServiceImpl implements NotificacaoServiceInterface {
  private final NotificacaoRepository notificacaoRepository;
  private final AlertaRepository alertaRepository;
  private final AlunoResponsavelRepository associacaoRepository;
  private final UsuarioContext usuarioContext;
  private final EmailService emailService;

  public List<NotificacaoResponseDto> enviarParaResponsaveis(Long alertaId,
      Jwt jwt) {
    validarAcesso(jwt);
    Alerta alerta = buscarAlerta(alertaId);
    List<AlunoResponsavel> associados = associacaoRepository
        .findByAlunoIdOrderByResponsavelPrincipalDescResponsavelNomeAsc(
            alerta.getAluno().getId())
        .stream()
        .filter(a -> a.getResponsavel().isAtivo())
        .toList();
    if (associados.isEmpty())
      throw new IllegalArgumentException(
          "O aluno não possui responsáveis ativos associados");
    return associados.stream()
        .map(a -> enviar(alerta, a.getResponsavel()))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<NotificacaoResponseDto> listarPorAlerta(Long alertaId, Jwt jwt) {
    validarAcesso(jwt);
    buscarAlerta(alertaId);
    return notificacaoRepository
        .findByAlertaIdOrderByCriadoEmDescIdDesc(alertaId)
        .stream()
        .map(this::converter)
        .toList();
  }

  public NotificacaoResponseDto reenviar(Long id, Jwt jwt) {
    validarAcesso(jwt);
    Notificacao n = notificacaoRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Notificação não encontrada"));
    if (n.getStatus() == StatusNotificacao.ENVIADA)
      throw new IllegalArgumentException("Essa notificação já foi enviada");
    try {
      emailService.enviarNotificacaoAlerta(n.getResponsavel().getNome(),
          n.getEmailDestino(), n.getAssunto(),
          n.getMensagem());
      n.setStatus(StatusNotificacao.ENVIADA);
      n.setErroEnvio(null);
      n.setEnviadoEm(LocalDateTime.now());
    } catch (RuntimeException e) {
      n.setStatus(StatusNotificacao.FALHA);
      n.setErroEnvio(mensagemErro(e));
    }
    return converter(n);
  }

  private NotificacaoResponseDto enviar(Alerta alerta, Responsavel r) {
    Notificacao anterior = notificacaoRepository
        .findByAlertaIdOrderByCriadoEmDescIdDesc(alerta.getId())
        .stream()
        .filter(n -> n.getResponsavel().getId().equals(r.getId()))
        .findFirst()
        .orElse(null);
    if (anterior != null && anterior.getStatus() == StatusNotificacao.ENVIADA)
      return converter(anterior);
    String assunto = "EduAlerta - Alerta de frequência de " + alerta.getAluno().getNome();
    String texto = "Olá, " + r.getNome() +
        (". A instituição identificou uma situação que exige atenção na "
            + "frequência de ")
        +
        alerta.getAluno().getNome() + ". Nível: " + alerta.getNivel() +
        ". Percentual de faltas não justificadas: " +
        alerta.getPercentualFaltas() +
        "%. Período letivo: " + alerta.getPeriodoLetivo().getNome() +
        ". Motivo: " + alerta.getMotivo() +
        ". Entre em contato com a instituição para obter mais informações.";
    Notificacao n = anterior == null ? new Notificacao(alerta, r, r.getEmail(),
        assunto, texto)
        : anterior;
    n.setEmailDestino(r.getEmail());
    n.setAssunto(assunto);
    n.setMensagem(texto);
    if (anterior == null)
      notificacaoRepository.saveAndFlush(n);
    try {
      emailService.enviarNotificacaoAlerta(r.getNome(), r.getEmail(), assunto,
          texto);
      n.setStatus(StatusNotificacao.ENVIADA);
      n.setEnviadoEm(LocalDateTime.now());
      n.setErroEnvio(null);
    } catch (RuntimeException e) {
      n.setStatus(StatusNotificacao.FALHA);
      n.setErroEnvio(mensagemErro(e));
    }
    return converter(n);
  }

  private String mensagemErro(Exception e) {
    String m = e.getMessage();
    if (m == null || m.isBlank())
      m = "Falha desconhecida no envio";
    return m.length() > 1000 ? m.substring(0, 1000) : m;
  }

  private Alerta buscarAlerta(Long id) {
    return alertaRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Alerta não encontrado"));
  }

  private void validarAcesso(Jwt jwt) {
    Usuario u = usuarioContext.getUsuario(jwt);
    if (u.getPerfil() != Perfil.ADM && u.getPerfil() != Perfil.SECRETARIA)
      throw new AccessDeniedException(
          "Seu perfil não pode gerenciar notificações");
  }

  private NotificacaoResponseDto converter(Notificacao n) {
    return new NotificacaoResponseDto(
        n.getId(), n.getAlerta().getId(), n.getResponsavel().getId(),
        n.getResponsavel().getNome(), n.getEmailDestino(), n.getAssunto(),
        n.getStatus(), n.getErroEnvio(), n.getCriadoEm(), n.getEnviadoEm());
  }
}
