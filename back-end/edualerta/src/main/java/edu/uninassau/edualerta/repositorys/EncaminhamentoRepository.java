package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Encaminhamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncaminhamentoRepository
    extends JpaRepository<Encaminhamento, Long> {
  List<Encaminhamento> findByAlertaIdOrderByDataEncaminhamentoDescIdDesc(Long alertaId);
}
