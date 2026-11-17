package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Matricula;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

  List<Matricula> findAllByOrderByIdAsc();

  List<Matricula> findByAlunoIdOrderByDataInicioDescIdDesc(Long alunoId);

  @Query("""
      SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
      FROM Matricula m
      WHERE m.aluno.id = :alunoId
        AND (:matriculaId IS NULL OR m.id <> :matriculaId)
        AND (:dataFim IS NULL OR m.dataInicio <= :dataFim)
        AND (m.dataFim IS NULL OR m.dataFim >= :dataInicio)
      """)
  boolean existeSobreposicao(
      @Param("alunoId") Long alunoId,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("matriculaId") Long matriculaId);

  @Query("""
      SELECT m
      FROM Matricula m
      WHERE m.turma.id = :turmaId
        AND m.dataInicio <= :dataFim
        AND (m.dataFim IS NULL OR m.dataFim >= :dataInicio)
      ORDER BY m.id ASC
      """)
  List<Matricula> findByTurmaAndPeriodoSobreposto(
      @Param("turmaId") Long turmaId,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim);
}
