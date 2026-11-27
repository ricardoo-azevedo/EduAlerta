package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.AlunoResponsavel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoResponsavelRepository
    extends JpaRepository<AlunoResponsavel, Long> {
  List<AlunoResponsavel> findByAlunoIdOrderByResponsavelPrincipalDescResponsavelNomeAsc(Long alunoId);

  Optional<AlunoResponsavel> findByAlunoIdAndResponsavelId(Long alunoId,
      Long responsavelId);

  List<AlunoResponsavel> findByAlunoIdAndResponsavelPrincipalTrue(Long alunoId);

  List<AlunoResponsavel> findByResponsavelId(Long responsavelId);
}
