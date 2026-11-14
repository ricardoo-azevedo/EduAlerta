package edu.uninassau.edualerta.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<RestError> tratarNaoEncontrado(EntityNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new RestError(404, e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<RestError> tratarRegraDeNegocio(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(new RestError(400, e.getMessage()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<RestError> tratarAcessoNegado(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new RestError(403, e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestError> tratarValidacao(MethodArgumentNotValidException e) {
    String mensagem = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(erro -> erro.getDefaultMessage())
        .collect(Collectors.joining("; "));

    return ResponseEntity.badRequest().body(new RestError(400, mensagem));
  }
}
