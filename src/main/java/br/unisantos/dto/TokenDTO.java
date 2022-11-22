package br.unisantos.dto;

import java.time.LocalDateTime;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TokenDTO {
	
	private Long id;
	
	@NonNull
	private String token;
	
	@NonNull
	private LocalDateTime data_criacao;
	
	@NonNull
	private LocalDateTime data_expiracao;
	private LocalDateTime data_confirmacao;
	
	@NonNull
	@ManyToOne(fetch = FetchType.LAZY)	//um usuário pode ter solicitado vários tokens
	private UsuarioDTO usuario;
	
	public TokenDTO() {
		
	}
}
