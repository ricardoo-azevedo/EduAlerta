package edu.uninassau.edualerta.dtos;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class UserTesteDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "nome não pode ser nulo")
    private String nome;

	public UserTesteDto(@NotNull(message = "nome não pode ser nulo") String nome) {
		this.nome = nome;
	}

}
