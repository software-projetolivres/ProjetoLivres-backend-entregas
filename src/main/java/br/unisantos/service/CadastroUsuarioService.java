package br.unisantos.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.email.EnvioEmail;
import br.unisantos.functions.ConstrucaoEmail;
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
	
	@Autowired
	private EnvioEmail envioEmail;
	
	private final static String LINK_SITE = "http://localhost:5000"; 
	private final static String LINK_SITE_CONFIRMACAO_TOKEN = LINK_SITE + "/api/cadastroUsuario/confirmar?token=";
	private final static String EMAIL_DESTINATARIO_CONFIRMACAO = "gabi.sperandeo@hotmail.com";
	private final static String ASSUNTO_EMAIL_NOVO_CADASTRO = "Novo Entregador Cadastrado - Pendente Confirmação";
	private final static String ASSUNTO_EMAIL_AGUARDE_CONFIRMACAO = "Aguardando Confirmação do Cadastro";
	private final static String ASSUNTO_EMAIL_CONFIRMADO_SUCESSO = "Sucesso na Confirmação do Cadastro";

	public String cadastrar(CadastroUsuario cadastro) {
		if (ValidaEmail.isEmailValido(cadastro.getEmail())) {
			Usuario usuario = new Usuario(cadastro.getNome(), cadastro.getSobrenome(), cadastro.getEmail(),
					cadastro.getSenha(), UsuarioTipoPerfil.USUARIO);

			String token = usuarioService.cadastrar(usuario);
			String link = LINK_SITE_CONFIRMACAO_TOKEN + token;
			envioEmail.enviar(EMAIL_DESTINATARIO_CONFIRMACAO,
					ConstrucaoEmail.emailConfirmacaoPendente(usuario.getNome(), usuario.getSobrenome(), link),
					ASSUNTO_EMAIL_NOVO_CADASTRO);
			envioEmail.enviar(usuario.getEmail(),
					ConstrucaoEmail.emailAguardeConfirmacao(usuario.getNome()), ASSUNTO_EMAIL_AGUARDE_CONFIRMACAO);
			return token;
		} else {
			throw new IllegalStateException("Este endereço de e-mail não é válido!");
		}
	}

	@Transactional
	public String confirmarToken(String pToken) {
		Token token = tokenService.encontrarToken(pToken)
				.orElseThrow(() -> new IllegalStateException("Este token não foi encontrado"));

		if (token.getData_confirmacao() != null) {
			return "Este endereço de e-mail já foi confirmado!!";
		}

		if (token.getData_expiracao().isBefore(LocalDateTime.now())) {
			if(tokenService.usuarioTemTokenValido(token.getUsuario())) {
				return "Este usuário possui token válido!";
			}
			
			usuarioService.gerarNovoToken(token);
			String link = LINK_SITE_CONFIRMACAO_TOKEN + token;
			envioEmail.enviar(EMAIL_DESTINATARIO_CONFIRMACAO,
					ConstrucaoEmail.emailConfirmacaoPendente(token.getUsuario().getNome(),
							token.getUsuario().getSobrenome(), link), ASSUNTO_EMAIL_NOVO_CADASTRO);
			return "Este token expirou! Um novo token foi enviado.";
		}

		tokenService.atualizarConfirmacao(pToken);
		usuarioService.ativarUsuario(token.getUsuario().getEmail());
		envioEmail.enviar(token.getUsuario().getEmail(),
				ConstrucaoEmail.emailConfirmacaoConcluida(token.getUsuario().getNome()),
				ASSUNTO_EMAIL_CONFIRMADO_SUCESSO);
		return "O usuário foi confirmado com sucesso!";
	}

}
