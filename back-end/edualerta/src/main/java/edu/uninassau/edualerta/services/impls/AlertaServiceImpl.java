package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.AlertaResponseDto;
import edu.uninassau.edualerta.models.Alerta;
import edu.uninassau.edualerta.models.Aluno;
import edu.uninassau.edualerta.models.NivelAlerta;
import edu.uninassau.edualerta.models.Perfil;
import edu.uninassau.edualerta.models.PeriodoLetivo;
import edu.uninassau.edualerta.models.SituacaoAlerta;
import edu.uninassau.edualerta.models.StatusFechamento;
import edu.uninassau.edualerta.models.Usuario;
import edu.uninassau.edualerta.repositorys.AlertaRepository;
import edu.uninassau.edualerta.repositorys.AlunoRepository;
import edu.uninassau.edualerta.repositorys.FechamentoDiaRepository;
import edu.uninassau.edualerta.repositorys.FrequenciaSemanalRepository;
import edu.uninassau.edualerta.repositorys.PeriodoLetivoRepository;
import edu.uninassau.edualerta.services.interfaces.AlertaServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertaServiceImpl implements AlertaServiceInterface {

  private static final BigDecimal LIMITE_PREVENTIVO = new BigDecimal("20.00");

  private static final BigDecimal LIMITE_CRITICO = new BigDecimal("25.00");

  private final AlertaRepository alertaRepository;
  private final AlunoRepository alunoRepository;
  private final PeriodoLetivoRepository periodoLetivoRepository;
  private final FrequenciaSemanalRepository frequenciaSemanalRepository;
  private final FechamentoDiaRepository fechamentoDiaRepository;
  private final UsuarioContext usuarioContext;

  @Override
  @Transactional(readOnly = true)
  public List<AlertaResponseDto> listar(Long alunoId, Long periodoLetivoId,
                                        NivelAlerta nivel,
                                        SituacaoAlerta situacao, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    validarFiltros(alunoId, periodoLetivoId);

    return alertaRepository
        .listarComFiltros(alunoId, periodoLetivoId, nivel, situacao)
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  public AlertaResponseDto resolver(Long alertaId, Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuario(jwt);

    validarPermissaoGerenciar(usuario);

    Alerta alerta = alertaRepository.findById(alertaId).orElseThrow(
        () -> new EntityNotFoundException("Alerta não encontrado"));

    if (alerta.getSituacao() == SituacaoAlerta.RESOLVIDO) {
      throw new IllegalArgumentException("Esse alerta já está resolvido");
    }

    alerta.setSituacao(SituacaoAlerta.RESOLVIDO);
    alerta.setResolvidoManualmente(true);
    alerta.setAtualizadoEm(LocalDateTime.now());

    return converterParaDto(alerta);
  }

  @Override
  public void atualizarAlertaDoAluno(Long alunoId, Long periodoLetivoId) {

    Aluno aluno = alunoRepository.findByIdForUpdate(alunoId).orElseThrow(
        () -> new EntityNotFoundException("Aluno não encontrado"));

    PeriodoLetivo periodo =
        periodoLetivoRepository.findById(periodoLetivoId)
            .orElseThrow(()
                             -> new EntityNotFoundException(
                                 "Período letivo não encontrado"));

    Long faltasNaoJustificadas =
        frequenciaSemanalRepository.somarFaltasNaoJustificadasDoAlunoNoPeriodo(
            alunoId, periodoLetivoId, StatusFechamento.FECHADO);

    Long aulasRealizadas = fechamentoDiaRepository.somarAulasDoAlunoNoPeriodo(
        alunoId, periodoLetivoId, StatusFechamento.FECHADO);

    if (faltasNaoJustificadas == null) {
      faltasNaoJustificadas = 0L;
    }

    if (aulasRealizadas == null) {
      aulasRealizadas = 0L;
    }

    BigDecimal percentual = BigDecimal.ZERO;

    if (aulasRealizadas > 0) {

      percentual = BigDecimal.valueOf(faltasNaoJustificadas)
                       .multiply(BigDecimal.valueOf(100))
                       .divide(BigDecimal.valueOf(aulasRealizadas), 2,
                               RoundingMode.HALF_UP);
    }

    Alerta alerta =
        alertaRepository
            .findByAlunoIdAndPeriodoLetivoId(alunoId, periodoLetivoId)
            .orElse(null);

    if (percentual.compareTo(LIMITE_PREVENTIVO) < 0) {

      if (alerta != null) {

        alerta.setPercentualFaltas(percentual);
        alerta.setSituacao(SituacaoAlerta.RESOLVIDO);

        alerta.setResolvidoManualmente(false);

        alerta.setMotivo(criarMotivo(percentual, faltasNaoJustificadas,
                                     aulasRealizadas, periodo));

        alerta.setAtualizadoEm(LocalDateTime.now());

        alertaRepository.save(alerta);
      }

      return;
    }

    NivelAlerta nivel = percentual.compareTo(LIMITE_CRITICO) >= 0
                            ? NivelAlerta.CRITICO
                            : NivelAlerta.PREVENTIVO;

    String motivo = criarMotivo(percentual, faltasNaoJustificadas,
                                aulasRealizadas, periodo);

    if (alerta == null) {

      alerta = new Alerta(aluno, periodo, percentual, nivel, motivo);

    } else if (!alerta.isResolvidoManualmente()) {

      alerta.setSituacao(SituacaoAlerta.ATIVO);
    }

    alerta.setPercentualFaltas(percentual);
    alerta.setNivel(nivel);
    alerta.setMotivo(motivo);
    alerta.setAtualizadoEm(LocalDateTime.now());

    alertaRepository.save(alerta);
  }

  private void validarFiltros(Long alunoId, Long periodoLetivoId) {

    if (alunoId != null && alunoId <= 0) {

      throw new IllegalArgumentException(
          "O identificador do aluno deve ser maior que zero");
    }

    if (periodoLetivoId != null && periodoLetivoId <= 0) {

      throw new IllegalArgumentException(
          "O identificador do período letivo deve ser maior que zero");
    }
  }

  private void validarPermissaoGerenciar(Usuario usuario) {

    if (usuario.getPerfil() != Perfil.ADM &&
        usuario.getPerfil() != Perfil.SECRETARIA) {

      throw new AccessDeniedException(
          "Seu perfil não possui permissão para gerenciar alertas");
    }
  }

  private String criarMotivo(BigDecimal percentual, Long faltasNaoJustificadas,
                             Long aulasRealizadas, PeriodoLetivo periodo) {

    return String.format(Locale.forLanguageTag("pt-BR"),
                         "O aluno apresenta %s%% de faltas não justificadas "
                             + "(%d faltas em %d aulas realizadas) "
                             + "no período letivo %s.",
                         percentual.toPlainString(), faltasNaoJustificadas,
                         aulasRealizadas, periodo.getNome());
  }

  private AlertaResponseDto converterParaDto(Alerta alerta) {

    return new AlertaResponseDto(
        alerta.getId(), alerta.getAluno().getId(), alerta.getAluno().getNome(),
        alerta.getPeriodoLetivo().getId(), alerta.getPeriodoLetivo().getNome(),
        alerta.getPercentualFaltas(), alerta.getNivel(), alerta.getSituacao(),
        alerta.getMotivo(), alerta.getAtualizadoEm());
  }
}
