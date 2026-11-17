
package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.FrequenciaSemanal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
