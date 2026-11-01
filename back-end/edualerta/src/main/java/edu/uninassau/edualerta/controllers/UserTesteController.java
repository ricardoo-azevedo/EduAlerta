package edu.uninassau.edualerta.controllers;

import edu.uninassau.edualerta.dtos.UserTesteDto;
import edu.uninassau.edualerta.services.impls.UserTesteImpl;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserTesteController {

  @Autowired private UserTesteImpl impl;

  @PostMapping()
  public ResponseEntity<?> registrar(@RequestBody @Valid UserTesteDto dto,
                                     BindingResult br) {
    if (br.hasErrors()) {
      return ResponseEntity.badRequest().body(br.getAllErrors());
    }

    UserTesteDto usersalvo = impl.salvar(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(usersalvo);
  }

  @GetMapping
  public ResponseEntity<List<?>> listar(){
    List<UserTesteDto> listarusers = impl.listar();
    return ResponseEntity.ok().body(listarusers);
  }



}
