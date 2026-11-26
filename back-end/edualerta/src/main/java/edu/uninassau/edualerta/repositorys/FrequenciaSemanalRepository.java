package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.FrequenciaSemanal;
import edu.uninassau.edualerta.models.StatusFechamento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequenciaSemanalRepository
    extends JpaRepository<FrequenciaSemanal, Long> {

  List<FrequenciaSemanal>
  findByFechamentoSemanalIdOrderByMatriculaIdAsc(Long fechamentoSemanalId);

  Optional<FrequenciaSemanal>
  findByIdAndFechamentoSemanalId(Long id, Long fechamentoSemanalId);

  Optional<FrequenciaSemanal>
  findByFechamentoSemanalIdAndMatriculaId(Long fechamentoSemanalId,
                                          Long matriculaId);

  boolean existsByFechamentoSemanalIdAndMatriculaId(Long fechamentoSemanalId,
                                                    Long matriculaId);

  @Query("""
      SELECT COALESCE(SUM(f.faltas), 0L)
      FROM FrequenciaSemanal f
      JOIN f.fechamentoSemanal fechamento
      WHERE f.matricula.id = :matriculaId
        AND fechamento.turma.id = :turmaId
        AND fechamento.status = :status
        AND fechamento.dataInicioSemana <=
            COALESCE(:dataFim, fechamento.dataInicioSemana)
        AND fechamento.dataFimSemana >= :dataInicio
      """)
  Long somarFaltasNaoJustificadasFechadas(
      @Param("matriculaId") Long matriculaId,
      @Param("turmaId") Long turmaId,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("status") StatusFechamento status);

  @Query("""
      SELECT COALESCE(SUM(f.faltas), 0L)
      FROM FrequenciaSemanal f
      JOIN f.matricula m
      JOIN f.fechamentoSemanal fechamento
      WHERE m.aluno.id = :alunoId
        AND fechamento.periodoLetivo.id = :periodoLetivoId
        AND fechamento.status = :status
        AND fechamento.turma.id = m.turma.id
        AND fechamento.dataInicioSemana <=
            COALESCE(m.dataFim, fechamento.dataInicioSemana)
        AND fechamento.dataFimSemana >= m.dataInicio
      """)
  Long somarFaltasNaoJustificadasDoAlunoNoPeriodo(
      @Param("alunoId") Long alunoId,
      @Param("periodoLetivoId") Long periodoLetivoId,
      @Param("status") StatusFechamento status);
}
