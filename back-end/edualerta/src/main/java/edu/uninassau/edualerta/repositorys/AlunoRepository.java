package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Aluno;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

  List<Aluno> findAllByOrderByIdAsc();

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT a FROM Aluno a WHERE a.id = :id")
  Optional<Aluno> findByIdForUpdate(@Param("id") Long id);
}
