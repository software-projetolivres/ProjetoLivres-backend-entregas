package br.unisantos.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.model.Token;
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
	
	public int atualizarConfirmacao(String pToken) {
		
		return tokenRepo.atualizarDataConfirmacao(LocalDateTime.now(), pToken);
	}
}
