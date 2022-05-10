package br.unisantos.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.functions.ValidaEmail;
import br.unisantos.model.CadastroUsuario;
import br.unisantos.model.Token;
import br.unisantos.model.Usuario;
import br.unisantos.model.UsuarioTipoPerfil;

@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TokenService tokenService;

	public String cadastrar(CadastroUsuario cadastro) {
		if (ValidaEmail.isEmailValido(cadastro.getEmail())) {
			Usuario usuario = new Usuario(cadastro.getNome(), cadastro.getSobrenome(), cadastro.getEmail(),
					cadastro.getSenha(), UsuarioTipoPerfil.USUARIO);

			return usuarioService.cadastrar(usuario);
		} else {
			throw new IllegalStateException("Este endereço de e-mail não é válido!");
		}
	}

	@Transactional
	public String confirmarToken(String pToken) {
		Token token = tokenService.encontrarToken(pToken)
				.orElseThrow(() -> new IllegalStateException("Este token não foi encontrado"));
		
		if(token.getData_confirmacao() != null) {
			return "Este endereço de e-mail já foi confirmado!";
		}
		
		if(token.getData_expiracao().isBefore(LocalDateTime.now())) {
			return "O token expirou!";
		}
		
		tokenService.atualizarConfirmacao(pToken);
		usuarioService.ativarUsuario(token.getUsuario().getEmail());
		return "O usuário foi confirmado com sucesso!";
	}

}
