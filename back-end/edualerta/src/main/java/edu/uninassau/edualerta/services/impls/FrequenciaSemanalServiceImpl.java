
package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalCadastroDto;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalResponseDto;
import edu.uninassau.edualerta.dtos.FrequenciaSemanalUpdateDto;
import edu.uninassau.edualerta.models.FechamentoDia;
import edu.uninassau.edualerta.models.FechamentoSemanal;
import edu.uninassau.edualerta.models.FrequenciaSemanal;
import edu.uninassau.edualerta.models.Matricula;
import edu.uninassau.edualerta.models.StatusFechamento;
import edu.uninassau.edualerta.repositorys.FechamentoDiaRepository;
import edu.uninassau.edualerta.repositorys.FechamentoSemanalRepository;
import edu.uninassau.edualerta.repositorys.FrequenciaSemanalRepository;
import edu.uninassau.edualerta.repositorys.MatriculaRepository;
import edu.uninassau.edualerta.services.interfaces.FrequenciaSemanalServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FrequenciaSemanalServiceImpl
    implements FrequenciaSemanalServiceInterface {

  private final FrequenciaSemanalRepository frequenciaRepository;
  private final FechamentoSemanalRepository fechamentoRepository;
  private final FechamentoDiaRepository fechamentoDiaRepository;
  private final MatriculaRepository matriculaRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public FrequenciaSemanalResponseDto
  cadastrar(Long fechamentoId, FrequenciaSemanalCadastroDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    Matricula matricula = buscarMatricula(dto.getMatriculaId());

    validarMatriculaNoFechamento(matricula, fechamento);

    boolean existe =
        frequenciaRepository.existsByFechamentoSemanalIdAndMatriculaId(
            fechamentoId, matricula.getId());

    if (existe) {
      throw new IllegalArgumentException(
          "Essa matrícula já possui frequência nesse fechamento");
    }

    validarQuantidadeDeFaltas(fechamento, matricula, dto.getFaltas(),
                              dto.getFaltasJustificadas());

    FrequenciaSemanal frequencia = new FrequenciaSemanal(
        fechamento, matricula, dto.getFaltas(), dto.getFaltasJustificadas());

    return converterParaDto(frequenciaRepository.save(frequencia));
  }

  @Override
  @Transactional(readOnly = true)
  public List<FrequenciaSemanalResponseDto> listar(Long fechamentoId, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    buscarFechamento(fechamentoId);

    return frequenciaRepository
        .findByFechamentoSemanalIdOrderByMatriculaIdAsc(fechamentoId)
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  public FrequenciaSemanalResponseDto
  atualizar(Long fechamentoId, Long frequenciaId,
            FrequenciaSemanalUpdateDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    FrequenciaSemanal frequencia = buscarFrequencia(frequenciaId, fechamentoId);

    validarQuantidadeDeFaltas(fechamento, frequencia.getMatricula(),
                              dto.getFaltas(), dto.getFaltasJustificadas());

    frequencia.setFaltas(dto.getFaltas());
    frequencia.setFaltasJustificadas(dto.getFaltasJustificadas());

    return converterParaDto(frequencia);
  }

  @Override
  public void remover(Long fechamentoId, Long frequenciaId, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    FechamentoSemanal fechamento = buscarFechamento(fechamentoId);

    validarRascunho(fechamento);

    FrequenciaSemanal frequencia = buscarFrequencia(frequenciaId, fechamentoId);

    frequenciaRepository.delete(frequencia);
  }

  private FechamentoSemanal buscarFechamento(Long id) {

    return fechamentoRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Fechamento semanal não encontrado"));
  }

  private Matricula buscarMatricula(Long id) {

    return matriculaRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Matrícula não encontrada"));
  }

  private FrequenciaSemanal buscarFrequencia(Long frequenciaId,
                                             Long fechamentoId) {

    return frequenciaRepository
        .findByIdAndFechamentoSemanalId(frequenciaId, fechamentoId)
        .orElseThrow(()
                         -> new EntityNotFoundException(
                             "Frequência não encontrada nesse fechamento"));
  }

  private void validarRascunho(FechamentoSemanal fechamento) {

    if (fechamento.getStatus() == StatusFechamento.FECHADO) {
      throw new IllegalArgumentException(
          "Esse fechamento já foi finalizado e não pode ser alterado");
    }
  }

  private void validarMatriculaNoFechamento(Matricula matricula,
                                            FechamentoSemanal fechamento) {

    if (!matricula.getTurma().getId().equals(fechamento.getTurma().getId())) {
      throw new IllegalArgumentException(
          "A matrícula não pertence à turma desse fechamento");
    }

    LocalDate inicio = fechamento.getDataInicioSemana();
    LocalDate fim = fechamento.getDataFimSemana();

    boolean anterior = matricula.getDataInicio().isAfter(fim);

    boolean posterior = matricula.getDataFim() != null &&
                        matricula.getDataFim().isBefore(inicio);

    if (anterior || posterior) {
      throw new IllegalArgumentException(
          "A matrícula não possui vínculo com a turma durante essa semana");
    }
  }

  private void validarQuantidadeDeFaltas(FechamentoSemanal fechamento,
                                         Matricula matricula, Integer faltas,
                                         Integer faltasJustificadas) {

    if (faltas == null || faltasJustificadas == null || faltas < 0 ||
        faltasJustificadas < 0) {

      throw new IllegalArgumentException(
          "As quantidades de faltas devem ser iguais ou superiores a zero");
    }

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

    List<FechamentoDia> dias =
        fechamentoDiaRepository.findByFechamentoSemanalIdOrderByDataAsc(
            fechamento.getId());

    int aulasRealizadas =
        dias.stream()
            .filter(dia
                    -> !dia.getData().isBefore(dataInicioContagem) &&
                           !dia.getData().isAfter(dataFimContagem))
            .mapToInt(FechamentoDia::getQuantidadeAulas)
            .sum();

    long totalFaltas = (long)faltas + faltasJustificadas;

    if (totalFaltas > aulasRealizadas) {
      throw new IllegalArgumentException(
          "A soma das faltas não pode ultrapassar as aulas realizadas "
          + "durante o vínculo da matrícula nessa semana (" + aulasRealizadas +
          ")");
    }
  }

  private FrequenciaSemanalResponseDto
  converterParaDto(FrequenciaSemanal frequencia) {

    Matricula matricula = frequencia.getMatricula();

    return new FrequenciaSemanalResponseDto(
        frequencia.getId(), matricula.getId(), matricula.getAluno().getId(),
        matricula.getAluno().getNome(), frequencia.getFaltas(),
        frequencia.getFaltasJustificadas());
  }
}
