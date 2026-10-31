package edu.uninassau.edualerta.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
public class UserTeste
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

	public UserTeste(String nome) {
		this.nome = nome;
	}

}
