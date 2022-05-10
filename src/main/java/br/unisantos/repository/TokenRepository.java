package br.unisantos.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByToken(String token);
	
	@Modifying
	@Transactional
	@Query("UPDATE Token t SET t.data_confirmacao = :data_confirmacao WHERE t.token = :token")
	int atualizarDataConfirmacao(LocalDateTime data_confirmacao, String token);
	
}
