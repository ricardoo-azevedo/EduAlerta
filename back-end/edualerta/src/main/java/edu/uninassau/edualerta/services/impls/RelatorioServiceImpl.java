
package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.RelatorioAlunoResponseDto;
import edu.uninassau.edualerta.dtos.RelatorioAlunoResponseDto.*;
import edu.uninassau.edualerta.models.*;
import edu.uninassau.edualerta.repositorys.*;
import edu.uninassau.edualerta.services.interfaces.RelatorioServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelatorioServiceImpl implements RelatorioServiceInterface {

  private final UsuarioContext usuarioContext;
  private final AlunoRepository alunoRepository;
  private final MatriculaRepository matriculaRepository;
  private final FechamentoSemanalRepository fechamentoRepository;
  private final FechamentoDiaRepository fechamentoDiaRepository;
  private final FrequenciaSemanalRepository frequenciaRepository;
  private final AlunoResponsavelRepository alunoResponsavelRepository;
  private final AlertaRepository alertaRepository;
  private final NotificacaoRepository notificacaoRepository;
  private final AcompanhamentoRepository acompanhamentoRepository;
  private final EncaminhamentoRepository encaminhamentoRepository;

  @Override
  public RelatorioAlunoResponseDto gerarRelatorioAluno(Long alunoId, Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuario(jwt);

    if (usuario.getPerfil() != Perfil.ADM &&
        usuario.getPerfil() != Perfil.SECRETARIA) {

      throw new AccessDeniedException(
          "Seu perfil não possui permissão para gerar relatórios");
    }

    if (alunoId == null || alunoId <= 0) {
      throw new IllegalArgumentException(
          "O identificador do aluno deve ser maior que zero");
    }

    Aluno aluno = alunoRepository.findById(alunoId).orElseThrow(
        () -> new EntityNotFoundException("Aluno não encontrado"));

    List<Matricula> matriculas =
        matriculaRepository.findByAlunoIdOrderByDataInicioDescIdDesc(alunoId);

    List<MatriculaRelatorio> matriculasDto = new ArrayList<>();

    long totalAulas = 0;
    long totalFaltas = 0;
    long totalJustificadas = 0;

    int totalFechamentos = 0;

    for (Matricula matricula : matriculas) {

      MatriculaConsolidada consolidada = consolidarMatricula(matricula);

      matriculasDto.add(consolidada.getDto());

      totalAulas += consolidada.getAulas();
      totalFaltas += consolidada.getFaltas();
      totalJustificadas += consolidada.getJustificadas();

      totalFechamentos += consolidada.getDto().getFechamentos().size();
    }

    // A consulta retorna as matrículas da mais recente
    // para a mais antiga. Invertemos para identificar
    // as mudanças na ordem cronológica.
    List<Matricula> cronologicas = new ArrayList<>(matriculas);

    Collections.reverse(cronologicas);

    List<MudancaTurma> mudancas = identificarMudancas(cronologicas);

    List<ResponsavelRelatorio> responsaveis =
        alunoResponsavelRepository
            .findByAlunoIdOrderByResponsavelPrincipalDescResponsavelNomeAsc(
                alunoId)
            .stream()
            .map(
                ar
                -> new ResponsavelRelatorio(ar.getResponsavel().getNome(),
                                            ar.getResponsavel().getParentesco(),
                                            ar.isResponsavelPrincipal(),
                                            ar.getResponsavel().isAtivo()))
            .toList();

    List<Alerta> alertasEntidade =
        alertaRepository.listarComFiltros(alunoId, null, null, null);

    List<AlertaRelatorio> alertas =
        alertasEntidade.stream().map(this::consolidarAlerta).toList();

    int totalIntervencoes =
        alertas.stream().mapToInt(a -> a.getAcompanhamentos().size()).sum();

    int totalEncaminhamentos =
        alertas.stream().mapToInt(a -> a.getEncaminhamentos().size()).sum();

    int enviadas =
        alertas.stream()
            .flatMap(a -> a.getNotificacoes().stream())
            .mapToInt(n -> n.getStatus() == StatusNotificacao.ENVIADA ? 1 : 0)
            .sum();

    int falhas =
        alertas.stream()
            .flatMap(a -> a.getNotificacoes().stream())
            .mapToInt(n -> n.getStatus() == StatusNotificacao.FALHA ? 1 : 0)
            .sum();

    Resumo resumo =
        new Resumo(matriculas.size(), mudancas.size(), totalFechamentos,
                   totalAulas, totalFaltas, totalJustificadas,
                   percentual(totalFaltas, totalAulas), alertas.size(),
                   totalIntervencoes, totalEncaminhamentos, enviadas, falhas);

    return new RelatorioAlunoResponseDto(
        new AlunoResumo(aluno.getNome(), aluno.isAtivo()), LocalDateTime.now(),
        resumo, matriculasDto, mudancas, responsaveis, alertas);
  }

  private MatriculaConsolidada consolidarMatricula(Matricula matricula) {

    List<FechamentoSemanal> fechamentos =
        fechamentoRepository
            .findByTurmaIdAndStatusOrderByDataInicioSemanaAscIdAsc(
                matricula.getTurma().getId(), StatusFechamento.FECHADO)
            .stream()
            .filter(
                f -> !f.getDataFimSemana().isBefore(matricula.getDataInicio()))
            .filter(
                f
                -> matricula.getDataFim() == null ||
                       !f.getDataInicioSemana().isAfter(matricula.getDataFim()))
            .toList();

    List<FechamentoRelatorio> itens = new ArrayList<>();

    Set<String> periodos = new LinkedHashSet<>();

    long aulasTotal = 0;
    long faltasTotal = 0;
    long justificadasTotal = 0;

    for (FechamentoSemanal fechamento : fechamentos) {

      long aulas =
          fechamentoDiaRepository
              .findByFechamentoSemanalIdOrderByDataAsc(fechamento.getId())
              .stream()
              .filter(d -> !d.getData().isBefore(matricula.getDataInicio()))
              .filter(d
                      -> matricula.getDataFim() == null ||
                             !d.getData().isAfter(matricula.getDataFim()))
              .mapToLong(FechamentoDia::getQuantidadeAulas)
              .sum();

      FrequenciaSemanal frequencia =
          frequenciaRepository
              .findByFechamentoSemanalIdAndMatriculaId(fechamento.getId(),
                                                       matricula.getId())
              .orElse(null);

      long faltas = frequencia == null ? 0 : frequencia.getFaltas();

      long justificadas =
          frequencia == null ? 0 : frequencia.getFaltasJustificadas();

      periodos.add(fechamento.getPeriodoLetivo().getNome());

      aulasTotal += aulas;
      faltasTotal += faltas;
      justificadasTotal += justificadas;

      itens.add(new FechamentoRelatorio(
          fechamento.getPeriodoLetivo().getNome(),
          fechamento.getDataInicioSemana(), fechamento.getDataFimSemana(),
          aulas, faltas, justificadas, percentual(faltas, aulas)));
    }

    MatriculaRelatorio dto = new MatriculaRelatorio(
        matricula.getTurma().getNome(), matricula.getTurma().getAnoLetivo(),
        matricula.getDataInicio(), matricula.getDataFim(),
        matricula.getStatus(), List.copyOf(periodos), itens, aulasTotal,
        faltasTotal, justificadasTotal, percentual(faltasTotal, aulasTotal));

    return new MatriculaConsolidada(dto, aulasTotal, faltasTotal,
                                    justificadasTotal);
  }

  private List<MudancaTurma> identificarMudancas(List<Matricula> matriculas) {

    List<MudancaTurma> mudancas = new ArrayList<>();

    for (int i = 1; i < matriculas.size(); i++) {

      Matricula anterior = matriculas.get(i - 1);
      Matricula atual = matriculas.get(i);

      if (!Objects.equals(anterior.getTurma().getId(),
                          atual.getTurma().getId())) {

        mudancas.add(new MudancaTurma(
            anterior.getTurma().getNome(), atual.getTurma().getNome(),
            anterior.getDataFim(), atual.getDataInicio(),
            "Mudança de turma identificada pelo encerramento "
                + "de uma matrícula e início de outra. "
                + "O sistema não registra um motivo específico "
                + "para a mudança."));
      }
    }

    return mudancas;
  }

  private AlertaRelatorio consolidarAlerta(Alerta alerta) {

    List<NotificacaoRelatorio> notificacoes =
        notificacaoRepository
            .findByAlertaIdOrderByCriadoEmDescIdDesc(alerta.getId())
            .stream()
            .map(n
                 -> new NotificacaoRelatorio(
                     n.getResponsavel().getNome(), n.getStatus(),
                     n.getCriadoEm(), n.getEnviadoEm(),
                     n.getStatus() == StatusNotificacao.ENVIADA
                         ? "O serviço de e-mail registrou o envio "
                               + "como concluído. Isso não comprova "
                               + "leitura ou resposta."
                         : "O sistema registrou falha no envio. "
                               + "Não há confirmação de contato concluído."))
            .toList();

    List<AcompanhamentoRelatorio> acompanhamentos =
        acompanhamentoRepository
            .findByAlertaIdOrderByDataHoraDescIdDesc(alerta.getId())
            .stream()
            .map(a
                 -> new AcompanhamentoRelatorio(
                     a.getDataHora(), a.getTipoIntervencao(), a.getDescricao(),
                     a.getResultado(), a.getUsuario().getNome()))
            .toList();

    List<EncaminhamentoRelatorio> encaminhamentos =
        encaminhamentoRepository
            .findByAlertaIdOrderByDataEncaminhamentoDescIdDesc(alerta.getId())
            .stream()
            .map(e
                 -> new EncaminhamentoRelatorio(
                     e.getServicoDestino(), e.getFinalidade(),
                     e.getObservacao(), e.getStatus(),
                     e.getDataEncaminhamento()))
            .toList();

    return new AlertaRelatorio(alerta.getPeriodoLetivo().getNome(),
                               alerta.getPercentualFaltas(), alerta.getNivel(),
                               alerta.getSituacao(), alerta.getMotivo(),
                               alerta.getAtualizadoEm(), notificacoes,
                               acompanhamentos, encaminhamentos);
  }

  private BigDecimal percentual(long faltas, long aulas) {

    if (aulas <= 0) {
      return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    return BigDecimal.valueOf(faltas)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(aulas), 2, RoundingMode.HALF_UP);
  }

  @Getter
  @AllArgsConstructor
  private static class MatriculaConsolidada {

    private MatriculaRelatorio dto;
    private long aulas;
    private long faltas;
    private long justificadas;
  }
}
