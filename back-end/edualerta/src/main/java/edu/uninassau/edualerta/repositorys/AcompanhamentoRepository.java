package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Acompanhamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcompanhamentoRepository
    extends JpaRepository<Acompanhamento, Long> {

  List<Acompanhamento> findByAlertaIdOrderByDataHoraDescIdDesc(Long alertaId);
}
