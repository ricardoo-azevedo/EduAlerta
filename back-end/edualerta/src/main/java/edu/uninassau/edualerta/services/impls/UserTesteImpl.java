package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.dtos.UserTesteDto;
import edu.uninassau.edualerta.models.UserTeste;
import edu.uninassau.edualerta.repositorys.UserTesteRepo;
import edu.uninassau.edualerta.services.interfaces.UserTesteInterface;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTesteImpl implements UserTesteInterface {

  @Autowired UserTesteRepo repo;

  private UserTesteDto toDto(UserTeste u) {
    return new UserTesteDto(u.getId(), u.getNome());
  }

  private UserTeste toEntity(UserTesteDto dto) {
    return new UserTeste(dto.getNome());
  }

  @Override
  public UserTesteDto salvar(UserTesteDto dto) {
    UserTeste salvo = repo.save(toEntity(dto));
    return toDto(salvo);
  }

  @Override
  public List<UserTesteDto> listar() {
    return repo.findAll()
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }
}
