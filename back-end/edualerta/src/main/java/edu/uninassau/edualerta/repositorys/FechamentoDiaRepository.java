package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.FechamentoDia;
import edu.uninassau.edualerta.models.StatusFechamento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FechamentoDiaRepository
    extends JpaRepository<FechamentoDia, Long> {

  List<FechamentoDia>
  findByFechamentoSemanalIdOrderByDataAsc(Long fechamentoSemanalId);

  boolean existsByFechamentoSemanalIdAndData(Long fechamentoSemanalId,
                                             LocalDate data);

  Optional<FechamentoDia>
  findByIdAndFechamentoSemanalId(Long id, Long fechamentoSemanalId);

  @Query("""
      SELECT COALESCE(SUM(d.quantidadeAulas), 0L)
      FROM FechamentoDia d
      JOIN d.fechamentoSemanal fechamento
      WHERE fechamento.turma.id = :turmaId
        AND fechamento.status = :status
        AND d.data >= :dataInicio
        AND d.data <= COALESCE(:dataFim, d.data)
      """)
  Long somarAulasRealizadasEmFechamentosFechados(
      @Param("turmaId") Long turmaId,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("status") StatusFechamento status);
}
