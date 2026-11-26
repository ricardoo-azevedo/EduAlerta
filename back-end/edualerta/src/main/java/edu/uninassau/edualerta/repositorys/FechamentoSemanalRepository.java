package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.FechamentoSemanal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FechamentoSemanalRepository
    extends JpaRepository<FechamentoSemanal, Long> {

  boolean existsByTurmaIdAndPeriodoLetivoIdAndDataInicioSemana(
      Long turmaId, Long periodoLetivoId, java.time.LocalDate dataInicioSemana);

  @Query("""
      SELECT f
      FROM FechamentoSemanal f
      WHERE (:turmaId IS NULL OR f.turma.id = :turmaId)
        AND (:periodoLetivoId IS NULL OR f.periodoLetivo.id = :periodoLetivoId)
      ORDER BY f.dataInicioSemana DESC, f.id DESC
      """)
  List<FechamentoSemanal> listarComFiltros(
      @Param("turmaId") Long turmaId,
      @Param("periodoLetivoId") Long periodoLetivoId);
}
