package br.unisantos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.model.Token;
import br.unisantos.model.Usuario;
import br.unisantos.repository.TokenRepository;

@Service
public class TokenService {
	
	@Autowired
	private TokenRepository tokenRepo;
	
	public void salvar(Token token) {
		tokenRepo.save(token);
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
	
	public Boolean usuarioTemTokenValido(Usuario usuario){
		Boolean result = false;
		List<Token> token = tokenRepo.procurarTokenValidoUsuario(usuario.getId());

		if(token != null) {
			result = true;
		}
		
		return result;
	}
}
