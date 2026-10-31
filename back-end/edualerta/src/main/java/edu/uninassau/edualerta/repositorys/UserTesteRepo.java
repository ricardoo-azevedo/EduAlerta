package edu.uninassau.edualerta.repositorys;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uninassau.edualerta.models.UserTeste;

@Repository
public interface UserTesteRepo extends JpaRepository<UserTeste, Long> {

    boolean existsByNome(String nome);


    List<UserTeste> findByNomeContainingIgnoreCase(String nome);

}
