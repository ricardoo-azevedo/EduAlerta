package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.PeriodoLetivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoLetivoRepository
    extends JpaRepository<PeriodoLetivo, Long> {

  List<PeriodoLetivo> findAllByOrderByAnoLetivoDescNumeroSemestreDesc();
}
