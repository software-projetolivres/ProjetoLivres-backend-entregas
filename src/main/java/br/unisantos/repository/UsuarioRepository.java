package br.unisantos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unisantos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	// Devolve, se houver, um registro de Token de acordo com o email passado.
	Optional<Usuario> findByEmail(String email);
}
