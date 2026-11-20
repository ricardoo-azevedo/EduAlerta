package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.AlertaResponseDto;
import edu.uninassau.edualerta.models.Alerta;
import edu.uninassau.edualerta.models.Aluno;
import edu.uninassau.edualerta.models.NivelAlerta;
import edu.uninassau.edualerta.models.PeriodoLetivo;
import edu.uninassau.edualerta.models.StatusFechamento;
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
                                        Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return alertaRepository.listarComFiltros(alunoId, periodoLetivoId)
        .stream()
        .map(this::converterParaDto)
        .toList();
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

    if (percentual.compareTo(LIMITE_PREVENTIVO) < 0) {
      alertaRepository.deleteByAlunoIdAndPeriodoLetivoId(alunoId,
                                                         periodoLetivoId);

      return;
    }

    NivelAlerta nivel = percentual.compareTo(LIMITE_CRITICO) >= 0
                            ? NivelAlerta.CRITICO
                            : NivelAlerta.PREVENTIVO;

    String motivo =
        String.format(Locale.forLanguageTag("pt-BR"),
                      "O aluno apresenta %s%% de faltas não justificadas "
                          + "(%d faltas em %d aulas realizadas) "
                          + "no período letivo %s.",
                      percentual.toPlainString(), faltasNaoJustificadas,
                      aulasRealizadas, periodo.getNome());

    Alerta alerta =
        alertaRepository
            .findByAlunoIdAndPeriodoLetivoId(alunoId, periodoLetivoId)
            .orElse(null);

    if (alerta == null) {
      alerta = new Alerta(aluno, periodo, percentual, nivel, motivo);
    }

    alerta.setPercentualFaltas(percentual);
    alerta.setNivel(nivel);
    alerta.setMotivo(motivo);
    alerta.setAtualizadoEm(LocalDateTime.now());

    alertaRepository.save(alerta);
  }

  private AlertaResponseDto converterParaDto(Alerta alerta) {

    return new AlertaResponseDto(
        alerta.getId(), alerta.getAluno().getId(), alerta.getAluno().getNome(),
        alerta.getPeriodoLetivo().getId(), alerta.getPeriodoLetivo().getNome(),
        alerta.getPercentualFaltas(), alerta.getNivel(), alerta.getMotivo(),
        alerta.getAtualizadoEm());
  }
}
