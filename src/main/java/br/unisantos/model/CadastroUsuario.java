package br.unisantos.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class CadastroUsuario {
	
	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	
}
