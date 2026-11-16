package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.FechamentoDia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
