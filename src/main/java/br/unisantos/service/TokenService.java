package br.unisantos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.dto.TokenDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.mapper.TokenMapper;
import br.unisantos.model.Token;
import br.unisantos.repository.TokenRepository;

@Service
public class TokenService {
	
	@Autowired
	private TokenRepository tokenRepo;
	
	@Autowired
	private TokenMapper tokenMapper;
	
	public void salvar(TokenDTO token) {
		tokenRepo.save(tokenMapper.toEntity(token));
	}
	
	public Optional<Token> encontrarToken(String token){
		return tokenRepo.findByToken(token);
	}
	
	public void atualizarConfirmacao(String pToken) {
		Optional<Token> token = encontrarToken(pToken);
		
		if(token.isPresent()) {
			Token tokenToUpdate = token.get();
			tokenToUpdate.setData_confirmacao(LocalDateTime.now());
			tokenRepo.save(tokenToUpdate);
		}
	}
	
	public Boolean usuarioTemTokenValido(UsuarioDTO usuario){
		Boolean result = false;
		List<Token> token = tokenRepo.procurarTokenValidoUsuario(usuario.getId());

		if(token.size() > 0) {
			result = true;
		}
		
		return result;
	}
}
