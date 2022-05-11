package br.unisantos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.unisantos.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByToken(String token);

	@Query("SELECT t FROM Token t WHERE t.usuario.id = :usuario_id AND t.data_expiracao >= now() AND t.data_confirmacao is null")
	List<Token> procurarTokenValidoUsuario(@Param("usuario_id") Long usuario_id);

}
