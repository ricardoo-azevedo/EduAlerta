package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Responsavel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsavelRepository
    extends JpaRepository<Responsavel, Long> {
  boolean existsByEmailIgnoreCase(String email);

  List<Responsavel> findAllByOrderByNomeAsc();
}
