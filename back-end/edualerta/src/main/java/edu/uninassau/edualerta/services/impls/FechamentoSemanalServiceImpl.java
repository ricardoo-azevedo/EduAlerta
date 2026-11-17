
package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.FechamentoDiaCadastroDto;
import edu.uninassau.edualerta.dtos.FechamentoDiaResponseDto;
import edu.uninassau.edualerta.dtos.FechamentoSemanalCadastroDto;
import edu.uninassau.edualerta.dtos.FechamentoSemanalResponseDto;
import edu.uninassau.edualerta.models.FechamentoDia;
import edu.uninassau.edualerta.models.FechamentoSemanal;
import edu.uninassau.edualerta.models.FrequenciaSemanal;
import edu.uninassau.edualerta.models.Matricula;
import edu.uninassau.edualerta.models.PeriodoLetivo;
import edu.uninassau.edualerta.models.StatusFechamento;
import edu.uninassau.edualerta.models.Turma;
import edu.uninassau.edualerta.models.Usuario;
import edu.uninassau.edualerta.repositorys.FechamentoDiaRepository;
import edu.uninassau.edualerta.repositorys.FechamentoSemanalRepository;
import edu.uninassau.edualerta.repositorys.FrequenciaSemanalRepository;
import edu.uninassau.edualerta.repositorys.MatriculaRepository;
import edu.uninassau.edualerta.repositorys.PeriodoLetivoRepository;
import edu.uninassau.edualerta.repositorys.TurmaRepository;
import edu.uninassau.edualerta.services.interfaces.FechamentoSemanalServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FechamentoSemanalServiceImpl
    implements FechamentoSemanalServiceInterface {

  private final FechamentoSemanalRepository fechamentoSemanalRepository;
  private final FechamentoDiaRepository fechamentoDiaRepository;
  private final FrequenciaSemanalRepository frequenciaSemanalRepository;
  private final MatriculaRepository matriculaRepository;
  private final TurmaRepository turmaRepository;
  private final PeriodoLetivoRepository periodoLetivoRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public FechamentoSemanalResponseDto
  cadastrar(FechamentoSemanalCadastroDto dto, Jwt jwt) {

    Usuario usuario = usuarioContext.getUsuario(jwt);

    Turma turma =
        turmaRepository.findById(dto.getTurmaId())
            .orElseThrow(
                () -> new EntityNotFoundException("Turma não encontrada"));

    PeriodoLetivo periodo =
        periodoLetivoRepository.findById(dto.getPeriodoLetivoId())
            .orElseThrow(()
                             -> new EntityNotFoundException(
                                 "Período letivo não encontrado"));

    if (!turma.isAtivo()) {
      throw new IllegalArgumentException(
          "Não é possível criar um fechamento para uma turma inativa");
    }

    if (!periodo.isAtivo()) {
      throw new IllegalArgumentException(
          "Não é possível criar um fechamento para um período letivo inativo");
    }

    LocalDate dataInicio = dto.getDataInicioSemana();

    validarInicioDaSemana(dataInicio);

    LocalDate dataFim = dataInicio.plusDays(6);

    validarIntersecaoComPeriodo(dataInicio, dataFim, periodo);

    boolean jaExiste =
        fechamentoSemanalRepository
            .existsByTurmaIdAndPeriodoLetivoIdAndDataInicioSemana(
                turma.getId(), periodo.getId(), dataInicio);

    if (jaExiste) {
      throw new IllegalArgumentException(
          "Já existe um fechamento para essa turma, período e semana");
    }

    FechamentoSemanal fechamento =
        new FechamentoSemanal(turma, periodo, usuario, dataInicio, dataFim);

    FechamentoSemanal salvo = fechamentoSemanalRepository.save(fechamento);

    return converterParaDto(salvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FechamentoSemanalResponseDto>
  listar(Long turmaId, Long periodoLetivoId, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return fechamentoSemanalRepository
        .listarComFiltros(turmaId, periodoLetivoId)
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public FechamentoSemanalResponseDto buscarPorId(Long id, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return converterParaDto(buscarFechamento(id));
  }

  @Override
  public FechamentoSemanalResponseDto
  adicionarDia(Long fechamentoId, FechamentoDiaCadastroDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    validarDiaNoFechamento(fechamento, dto.getData());

    if (dto.getQuantidadeAulas() == null || dto.getQuantidadeAulas() < 0) {
      throw new IllegalArgumentException(
          "A quantidade de aulas não pode ser negativa");
    }

    boolean diaJaExiste =
        fechamentoDiaRepository.existsByFechamentoSemanalIdAndData(
            fechamentoId, dto.getData());

    if (diaJaExiste) {
      throw new IllegalArgumentException(
          "Já existe um registro para essa data");
    }

    FechamentoDia dia =
        new FechamentoDia(fechamento, dto.getData(), dto.getQuantidadeAulas());

    fechamentoDiaRepository.save(dia);

    return converterParaDto(fechamento);
  }

  @Override
  public FechamentoSemanalResponseDto
  atualizarDia(Long fechamentoId, Long diaId, FechamentoDiaCadastroDto dto,
               Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    FechamentoDia dia =
        fechamentoDiaRepository
            .findByIdAndFechamentoSemanalId(diaId, fechamentoId)
            .orElseThrow(()
                             -> new EntityNotFoundException(
                                 "Dia não encontrado nesse fechamento"));

    validarDiaNoFechamento(fechamento, dto.getData());

    if (dto.getQuantidadeAulas() == null || dto.getQuantidadeAulas() < 0) {
      throw new IllegalArgumentException(
          "A quantidade de aulas não pode ser negativa");
    }

    boolean dataJaUtilizada =
        fechamentoDiaRepository.existsByFechamentoSemanalIdAndData(
            fechamentoId, dto.getData());

    if (dataJaUtilizada && !dia.getData().equals(dto.getData())) {
      throw new IllegalArgumentException(
          "Já existe outro registro para essa data");
    }

    dia.setData(dto.getData());
    dia.setQuantidadeAulas(dto.getQuantidadeAulas());

    return converterParaDto(fechamento);
  }

  @Override
  public void removerDia(Long fechamentoId, Long diaId, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    FechamentoDia dia =
        fechamentoDiaRepository
            .findByIdAndFechamentoSemanalId(diaId, fechamentoId)
            .orElseThrow(()
                             -> new EntityNotFoundException(
                                 "Dia não encontrado nesse fechamento"));

    fechamentoDiaRepository.delete(dia);
  }

  @Override
  public FechamentoSemanalResponseDto fechar(Long fechamentoId, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    List<FechamentoDia> dias =
        fechamentoDiaRepository.findByFechamentoSemanalIdOrderByDataAsc(
            fechamentoId);

    if (dias.isEmpty()) {
      throw new IllegalArgumentException(
          "Não é possível fechar uma semana sem registrar nenhum dia");
    }

    List<Matricula> matriculas =
        matriculaRepository.findByTurmaAndPeriodoSobreposto(
            fechamento.getTurma().getId(), fechamento.getDataInicioSemana(),
            fechamento.getDataFimSemana());

    for (Matricula matricula : matriculas) {

      FrequenciaSemanal frequencia =
          frequenciaSemanalRepository
              .findByFechamentoSemanalIdAndMatriculaId(fechamentoId,
                                                       matricula.getId())
              .orElseThrow(
                  ()
                      -> new IllegalArgumentException(
                          "A matrícula " + matricula.getId() +
                          " ainda não possui registro de frequência"));

      validarFrequenciaDoFechamento(fechamento, matricula, frequencia, dias);
    }

    fechamento.setStatus(StatusFechamento.FECHADO);

    return converterParaDto(fechamento);
  }

  private void validarFrequenciaDoFechamento(FechamentoSemanal fechamento,
                                             Matricula matricula,
                                             FrequenciaSemanal frequencia,
                                             List<FechamentoDia> dias) {

    LocalDate inicio = fechamento.getDataInicioSemana();

    if (matricula.getDataInicio().isAfter(inicio)) {
      inicio = matricula.getDataInicio();
    }

    LocalDate fim = fechamento.getDataFimSemana();

    if (matricula.getDataFim() != null &&
        matricula.getDataFim().isBefore(fim)) {
      fim = matricula.getDataFim();
    }

    final LocalDate dataInicioContagem = inicio;
    final LocalDate dataFimContagem = fim;

    long aulasRealizadas =
        dias.stream()
            .filter(dia
                    -> !dia.getData().isBefore(dataInicioContagem) &&
                           !dia.getData().isAfter(dataFimContagem))
            .mapToLong(FechamentoDia::getQuantidadeAulas)
            .sum();

    long totalFaltas =
        (long)frequencia.getFaltas() + frequencia.getFaltasJustificadas();

    if (totalFaltas > aulasRealizadas) {
      throw new IllegalArgumentException(
          "A frequência da matrícula " + matricula.getId() +
          " possui mais faltas do que aulas realizadas");
    }
  }

  private FechamentoSemanal buscarFechamento(Long id) {

    return fechamentoSemanalRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Fechamento semanal não encontrado"));
  }

  private void validarInicioDaSemana(LocalDate dataInicio) {

    if (dataInicio.getDayOfWeek() != DayOfWeek.MONDAY) {
      throw new IllegalArgumentException(
          "A data inicial do fechamento deve ser uma segunda-feira");
    }
  }

  private void validarIntersecaoComPeriodo(LocalDate dataInicio,
                                           LocalDate dataFim,
                                           PeriodoLetivo periodo) {

    boolean semanaAntesDoPeriodo = dataFim.isBefore(periodo.getDataInicio());

    boolean semanaDepoisDoPeriodo = dataInicio.isAfter(periodo.getDataFim());

    if (semanaAntesDoPeriodo || semanaDepoisDoPeriodo) {
      throw new IllegalArgumentException(
          "A semana informada não pertence ao intervalo do período letivo");
    }
  }

  private void validarDiaNoFechamento(FechamentoSemanal fechamento,
                                      LocalDate data) {

    if (data == null) {
      throw new IllegalArgumentException("A data do dia é obrigatória");
    }

    if (data.isBefore(fechamento.getDataInicioSemana()) ||
        data.isAfter(fechamento.getDataFimSemana())) {

      throw new IllegalArgumentException(
          "A data informada não pertence à semana do fechamento");
    }

    PeriodoLetivo periodo = fechamento.getPeriodoLetivo();

    if (data.isBefore(periodo.getDataInicio()) ||
        data.isAfter(periodo.getDataFim())) {

      throw new IllegalArgumentException(
          "A data informada está fora do período letivo");
    }
  }

  private void validarRascunho(FechamentoSemanal fechamento) {

    if (fechamento.getStatus() == StatusFechamento.FECHADO) {
      throw new IllegalArgumentException(
          "Esse fechamento já foi finalizado e não pode ser alterado");
    }
  }

  private FechamentoSemanalResponseDto
  converterParaDto(FechamentoSemanal fechamento) {

    List<FechamentoDiaResponseDto> dias =
        fechamentoDiaRepository
            .findByFechamentoSemanalIdOrderByDataAsc(fechamento.getId())
            .stream()
            .map(this::converterDiaParaDto)
            .toList();

    return new FechamentoSemanalResponseDto(
        fechamento.getId(), fechamento.getTurma().getId(),
        fechamento.getTurma().getNome(), fechamento.getPeriodoLetivo().getId(),
        fechamento.getPeriodoLetivo().getNome(),
        fechamento.getUsuario().getId(), fechamento.getUsuario().getNome(),
        fechamento.getDataInicioSemana(), fechamento.getDataFimSemana(),
        fechamento.getStatus(), dias);
  }

  private FechamentoDiaResponseDto converterDiaParaDto(FechamentoDia dia) {

    return new FechamentoDiaResponseDto(dia.getId(), dia.getData(),
                                        dia.getQuantidadeAulas());
  }
}
