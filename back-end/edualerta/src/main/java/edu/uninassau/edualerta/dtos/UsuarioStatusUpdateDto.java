package edu.uninassau.edualerta.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioStatusUpdateDto {

    @NotNull(message = "O status ativo deve ser informado")
    private Boolean ativo;
}
