package br.unisantos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByEmail(String email);
	
	@Modifying
	@Transactional
	@Query("UPDATE Usuario u SET u.ativo = true WHERE u.email = :email")
	int ativarUsuario(String email);
}
