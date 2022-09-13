package br.unisantos.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.unisantos.dto.UsuarioDTO;
import br.unisantos.model.Usuario;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class UsuarioMapper {

	public Usuario toEntity(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		usuario.setId(usuarioDTO.getId());
		usuario.setNome(usuarioDTO.getNome());
		usuario.setSobrenome(usuarioDTO.getSobrenome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setSenha(usuarioDTO.getSenha());
		usuario.setUsuarioTipoPerfil(usuarioDTO.getUsuarioTipoPerfil());
		usuario.setBloqueado(usuarioDTO.getBloqueado());
		usuario.setAtivo(usuarioDTO.getAtivo());
		
		return usuario;
	}
	
	public UsuarioDTO toDTO(Usuario usuario) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(usuario.getId());
		usuarioDTO.setNome(usuario.getNome());
		usuarioDTO.setSobrenome(usuario.getSobrenome());
		usuarioDTO.setEmail(usuario.getEmail());
		usuarioDTO.setSenha(usuario.getSenha());
		usuarioDTO.setUsuarioTipoPerfil(usuario.getUsuarioTipoPerfil());
		usuarioDTO.setBloqueado(usuario.getBloqueado());
		usuarioDTO.setAtivo(usuario.getAtivo());
		
		return usuarioDTO;
	}
	
	public List<Usuario> toEntity(List<UsuarioDTO> usuariosDTO) {
		List<Usuario> usuarios = new ArrayList<>();
		for (UsuarioDTO u : usuariosDTO) {
			usuarios.add(toEntity(u));
		}
		
		return usuarios;
	}
	
	public List<UsuarioDTO> toDTO(List<Usuario> usuarios) {
		List<UsuarioDTO> usuariosDTO = new ArrayList<>();
		for (Usuario u : usuarios) {
			usuariosDTO.add(toDTO(u));
		}
		
		return usuariosDTO;
	}
}
