package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Alerta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

  Optional<Alerta> findByAlunoIdAndPeriodoLetivoId(Long alunoId,
                                                   Long periodoLetivoId);

  @Query("""
      SELECT a
      FROM Alerta a
      WHERE (:alunoId IS NULL OR a.aluno.id = :alunoId)
        AND (:periodoLetivoId IS NULL OR
             a.periodoLetivo.id = :periodoLetivoId)
      ORDER BY a.periodoLetivo.id DESC, a.nivel DESC, a.aluno.nome ASC
      """)
  List<Alerta> listarComFiltros(
      @Param("alunoId") Long alunoId,
      @Param("periodoLetivoId") Long periodoLetivoId);

  void deleteByAlunoIdAndPeriodoLetivoId(
      Long alunoId,
      Long periodoLetivoId);
}
