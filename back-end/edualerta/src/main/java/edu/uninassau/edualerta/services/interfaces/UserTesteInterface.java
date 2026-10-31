package edu.uninassau.edualerta.services.interfaces;

import java.util.List;

import edu.uninassau.edualerta.dtos.UserTesteDto;

public interface UserTesteInterface {

    UserTesteDto salvar(UserTesteDto dto);

    List<UserTesteDto> listar();

  
}
