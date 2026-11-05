package edu.uninassau.edualerta.repositorys;

import edu.uninassau.edualerta.models.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  Optional<Usuario> findByAuthUserId(UUID authUserId);

  Optional<Usuario> findByEmailIgnoreCase(String email);

  boolean existsByEmailIgnoreCase(String email);

  List<Usuario> findByNomeContainingIgnoreCase(String nome);
}
