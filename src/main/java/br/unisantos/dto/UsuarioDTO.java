package br.unisantos.dto;

import br.unisantos.model.UsuarioTipoPerfil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class UsuarioDTO {
	
	private Long id;
	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	private UsuarioTipoPerfil usuarioTipoPerfil;
	private Boolean bloqueado;
	private Boolean ativo;
	
}