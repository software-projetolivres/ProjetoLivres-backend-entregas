package br.unisantos.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.unisantos.dto.TokenDTO;
import br.unisantos.model.Token;

@Component
public class TokenMapper {
	
	@Autowired
	UsuarioMapper usuarioMapper;

	public Token toEntity(TokenDTO tokenDTO) {
		Token token = new Token();
		token.setId(tokenDTO.getId());
		token.setToken(tokenDTO.getToken());
		token.setData_criacao(tokenDTO.getData_criacao());
		token.setData_expiracao(tokenDTO.getData_expiracao());
		token.setData_confirmacao(tokenDTO.getData_confirmacao());
		token.setUsuario(usuarioMapper.toEntity(tokenDTO.getUsuario()));
		
		return token;
	}
	
	public TokenDTO toDTO(Token token) {
		TokenDTO tokenDTO = new TokenDTO();
		tokenDTO.setId(token.getId());
		tokenDTO.setToken(token.getToken());
		tokenDTO.setData_criacao(token.getData_criacao());
		tokenDTO.setData_expiracao(token.getData_expiracao());
		tokenDTO.setData_confirmacao(token.getData_confirmacao());
		tokenDTO.setUsuario(usuarioMapper.toDTO(token.getUsuario()));
		
		return tokenDTO;
	}
	
	public List<Token> toEntity(List<TokenDTO> tokensDTO) {
		List<Token> tokens = new ArrayList<>();
		for (TokenDTO t : tokensDTO) {
			tokens.add(toEntity(t));
		}
		
		return tokens;
	}
	
	public List<TokenDTO> toDTO(List<Token> tokens) {
		List<TokenDTO> tokensDTO = new ArrayList<>();
		for (Token t : tokens) {
			tokensDTO.add(toDTO(t));
		}
		
		return tokensDTO;
	}
}
