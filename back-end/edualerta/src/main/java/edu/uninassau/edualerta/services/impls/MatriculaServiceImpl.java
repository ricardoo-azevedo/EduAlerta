package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.FrequenciaAcumuladaResponseDto;
import edu.uninassau.edualerta.dtos.MatriculaCadastroDto;
import edu.uninassau.edualerta.dtos.MatriculaEncerramentoDto;
import edu.uninassau.edualerta.dtos.MatriculaResponseDto;
import edu.uninassau.edualerta.dtos.MatriculaTransferenciaDto;
import edu.uninassau.edualerta.models.Aluno;
import edu.uninassau.edualerta.models.Matricula;
import edu.uninassau.edualerta.models.StatusFechamento;
import edu.uninassau.edualerta.models.StatusMatricula;
import edu.uninassau.edualerta.models.Turma;
import edu.uninassau.edualerta.repositorys.AlunoRepository;
import edu.uninassau.edualerta.repositorys.FechamentoDiaRepository;
import edu.uninassau.edualerta.repositorys.FrequenciaSemanalRepository;
import edu.uninassau.edualerta.repositorys.MatriculaRepository;
import edu.uninassau.edualerta.repositorys.TurmaRepository;
import edu.uninassau.edualerta.services.interfaces.MatriculaServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatriculaServiceImpl implements MatriculaServiceInterface {

  private final MatriculaRepository matriculaRepository;
  private final FrequenciaSemanalRepository frequenciaSemanalRepository;
  private final FechamentoDiaRepository fechamentoDiaRepository;
  private final AlunoRepository alunoRepository;
  private final TurmaRepository turmaRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public MatriculaResponseDto cadastrar(MatriculaCadastroDto dto, Jwt jwt) {
    usuarioContext.getUsuario(jwt);

    Aluno aluno = buscarAlunoParaOperacao(dto.getAlunoId());
    Turma turma = buscarTurma(dto.getTurmaId());

    if (!aluno.isAtivo()) {
      throw new IllegalArgumentException(
          "Não é possível matricular um aluno inativo");
    }

    if (!turma.isAtivo()) {
      throw new IllegalArgumentException(
          "Não é possível matricular um aluno em uma turma inativa");
    }

    validarSobreposicao(aluno.getId(), dto.getDataInicio(), null, null);

    Matricula matricula = new Matricula(aluno, turma, dto.getDataInicio());

    return converterParaDto(matriculaRepository.save(matricula));
  }

  @Override
  @Transactional(readOnly = true)
  public List<MatriculaResponseDto> listar(Jwt jwt) {
    usuarioContext.getUsuario(jwt);

    return matriculaRepository.findAllByOrderByIdAsc()
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public MatriculaResponseDto buscarPorId(Long id, Jwt jwt) {
    usuarioContext.getUsuario(jwt);

    return converterParaDto(buscarMatricula(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<MatriculaResponseDto> listarPorAluno(Long alunoId, Jwt jwt) {
    usuarioContext.getUsuario(jwt);

    if (!alunoRepository.existsById(alunoId)) {
      throw new EntityNotFoundException("Aluno não encontrado");
    }

    return matriculaRepository.findByAlunoIdOrderByDataInicioDescIdDesc(alunoId)
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public FrequenciaAcumuladaResponseDto calcularFrequenciaAcumulada(Long id,
                                                                    Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Matricula matricula = buscarMatricula(id);

    Long faltasNaoJustificadas =
        frequenciaSemanalRepository.somarFaltasNaoJustificadasFechadas(
            matricula.getId(), matricula.getTurma().getId(),
            matricula.getDataInicio(), matricula.getDataFim(),
            StatusFechamento.FECHADO);

    Long aulasRealizadas =
        fechamentoDiaRepository.somarAulasRealizadasEmFechamentosFechados(
            matricula.getTurma().getId(), matricula.getDataInicio(),
            matricula.getDataFim(), StatusFechamento.FECHADO);

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

    return new FrequenciaAcumuladaResponseDto(
        matricula.getId(), matricula.getAluno().getId(),
        matricula.getAluno().getNome(), faltasNaoJustificadas, aulasRealizadas,
        percentual);
  }

  @Override
  public MatriculaResponseDto encerrar(Long id, MatriculaEncerramentoDto dto,
                                       Jwt jwt) {
    usuarioContext.getUsuario(jwt);

    Matricula matriculaInicial = buscarMatricula(id);

    Aluno aluno = buscarAlunoParaOperacao(matriculaInicial.getAluno().getId());

    Matricula matricula = buscarMatricula(id);

    if (matricula.getStatus() != StatusMatricula.ATIVA) {
      throw new IllegalArgumentException(
          "Somente matrículas ativas podem ser encerradas");
    }

    LocalDate dataFim = dto.getDataFim();

    if (dataFim.isBefore(matricula.getDataInicio())) {
      throw new IllegalArgumentException(
          "A data de encerramento não pode ser anterior "
          + "à data de início da matrícula");
    }

    matricula.setDataFim(dataFim);
    matricula.setStatus(StatusMatricula.ENCERRADA);

    return converterParaDto(matricula);
  }

  @Override
  public MatriculaResponseDto transferir(Long id, MatriculaTransferenciaDto dto,
                                         Jwt jwt) {
    usuarioContext.getUsuario(jwt);

    Matricula matriculaInicial = buscarMatricula(id);

    Aluno aluno = buscarAlunoParaOperacao(matriculaInicial.getAluno().getId());

    Matricula matriculaAtual = buscarMatricula(id);

    if (matriculaAtual.getStatus() != StatusMatricula.ATIVA) {
      throw new IllegalArgumentException(
          "Somente matrículas ativas podem ser transferidas");
    }

    LocalDate dataTransferencia = dto.getDataTransferencia();

    if (!dataTransferencia.isAfter(matriculaAtual.getDataInicio())) {
      throw new IllegalArgumentException(
          "A data da transferência deve ser posterior "
          + "à data de início da matrícula atual");
    }

    Turma turmaDestino = buscarTurma(dto.getTurmaId());

    if (!turmaDestino.isAtivo()) {
      throw new IllegalArgumentException("A turma de destino está inativa");
    }

    if (turmaDestino.getId().equals(matriculaAtual.getTurma().getId())) {
      throw new IllegalArgumentException(
          "O aluno já está matriculado nessa turma");
    }

    LocalDate dataFimAnterior = dataTransferencia.minusDays(1);

    validarSobreposicao(aluno.getId(), dataTransferencia, null,
                        matriculaAtual.getId());

    matriculaAtual.setDataFim(dataFimAnterior);
    matriculaAtual.setStatus(StatusMatricula.ENCERRADA);

    matriculaRepository.saveAndFlush(matriculaAtual);

    Matricula novaMatricula =
        new Matricula(aluno, turmaDestino, dataTransferencia);

    return converterParaDto(matriculaRepository.save(novaMatricula));
  }

  private void validarSobreposicao(Long alunoId, LocalDate dataInicio,
                                   LocalDate dataFim, Long matriculaId) {
    boolean existe = matriculaRepository.existeSobreposicao(
        alunoId, dataInicio, dataFim, matriculaId);

    if (existe) {
      throw new IllegalArgumentException(
          "O aluno já possui uma matrícula nesse período");
    }
  }

  private Aluno buscarAlunoParaOperacao(Long id) {
    return alunoRepository.findByIdForUpdate(id).orElseThrow(
        () -> new EntityNotFoundException("Aluno não encontrado"));
  }

  private Turma buscarTurma(Long id) {
    return turmaRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Turma não encontrada"));
  }

  private Matricula buscarMatricula(Long id) {
    return matriculaRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Matrícula não encontrada"));
  }

  private MatriculaResponseDto converterParaDto(Matricula matricula) {
    return new MatriculaResponseDto(
        matricula.getId(), matricula.getAluno().getId(),
        matricula.getAluno().getNome(), matricula.getTurma().getId(),
        matricula.getTurma().getNome(), matricula.getDataInicio(),
        matricula.getDataFim(), matricula.getStatus());
  }
}
