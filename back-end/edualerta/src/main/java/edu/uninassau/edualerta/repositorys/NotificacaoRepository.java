package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Notificacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository
    extends JpaRepository<Notificacao, Long> {
  List<Notificacao> findByAlertaIdOrderByCriadoEmDescIdDesc(Long alertaId);
}
