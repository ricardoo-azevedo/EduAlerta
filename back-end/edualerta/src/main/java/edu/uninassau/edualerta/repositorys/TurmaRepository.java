package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Turma;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

  List<Turma> findAllByOrderByIdAsc();
}
