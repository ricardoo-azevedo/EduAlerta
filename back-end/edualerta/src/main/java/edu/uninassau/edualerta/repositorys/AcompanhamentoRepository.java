
package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Acompanhamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcompanhamentoRepository
    extends JpaRepository<Acompanhamento, Long> {}
