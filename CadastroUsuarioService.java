package br.unisantos.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.dto.EmailDTO;
import br.unisantos.dto.TokenDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.email.EnvioEmail;
import br.unisantos.functions.ConstrucaoEmail;
import br.unisantos.functions.ValidaEmail;
import br.unisantos.mapper.TokenMapper;
import br.unisantos.model.Token;
import br.unisantos.model.UsuarioTipoPerfil;

@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private EnvioEmail envioEmail;
	
	@Autowired
	private TokenMapper tokenMapper;
	
	/* CONSTANTES */
	private final static String LINK_SITE_CONFIRM = System.getenv("link") + "api/cadastroUsuario/";
	private final static String EMAIL_DESTINATARIO_CONFIRMACAO = System.getenv("email_destinatario_confirm") == null ? "" : System.getenv("email_destinatario_confirm");
	private final static String ASSUNTO_EMAIL_NOVO_CADASTRO = "Novo Entregador Cadastrado - Pendente Confirmação";
	private final static String ASSUNTO_EMAIL_AGUARDE_CONFIRMACAO = "Aguardando Confirmação do Cadastro";
	private final static String ASSUNTO_EMAIL_CONFIRMADO_SUCESSO = "Sucesso na Confirmação do Cadastro";

	/* Método responsável por realizar o cadastro, devolvendo uma entidade de resposta com o resultado da operação */
	public ResponseEntity<String> cadastrar(UsuarioDTO cadastro) {
		boolean usuarioCadastrado = usuarioService.findByEmail(cadastro.getEmail()) != null ? true : false;

		if (usuarioCadastrado) {	// verifica se já existe um usuário cadastrado com esse e-mail e dispara uma mensagem de erro
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um usuário cadastrado com este e-mail!");
		}
		
		if (ValidaEmail.isEmailValido(cadastro.getEmail())) {	// se o	e-mail for válido e não tiver um usuário já cadastrado
			cadastro.setUsuarioTipoPerfil(UsuarioTipoPerfil.USUARIO);
			cadastro.setBloqueado(false);
			cadastro.setAtivo(false);

			String token = usuarioService.cadastrar(cadastro);	// realiza o cadastro, gerando um token de ativação do usuário
			String link = LINK_SITE_CONFIRM + token;	// seta o link para ativação do usuário de acordo com o token gerado
			
			// realiza os disparos de e-mail, respectivamente: ao(s) gestor(es) e ao usuário
			envioEmail.enviar(new EmailDTO(EMAIL_DESTINATARIO_CONFIRMACAO,
					ConstrucaoEmail.emailConfirmacaoPendente(cadastro.getNome(), cadastro.getSobrenome(), link),
					ASSUNTO_EMAIL_NOVO_CADASTRO));
			envioEmail.enviar(new EmailDTO(cadastro.getEmail(),
					ConstrucaoEmail.emailAguardeConfirmacao(cadastro.getNome()), ASSUNTO_EMAIL_AGUARDE_CONFIRMACAO));
			
			return ResponseEntity.status(HttpStatus.OK).body("Cadastrado com Sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este endereço de e-mail não é válido!");
		}
	}

	/* Método responsável pela validação do token e, consequentemente, ativação do usuário ou geração de novo token */
	@Transactional
	public ResponseEntity<String> confirmarToken(String pToken) {
		Token token = tokenService.encontrarToken(pToken)
				.orElseThrow(() -> new IllegalStateException("Este token não foi encontrado"));
		
		TokenDTO tokenDTO = tokenMapper.toDTO(token);

		if (tokenDTO.getData_confirmacao() != null) {	// verifica se o token já foi confirmado
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Este endereço de e-mail já foi confirmado!!");
		} else if(tokenService.usuarioTemTokenValido(tokenDTO.getUsuario())) {	// token está válido
			// realiza as atualizações das informações do token e do usuário
			tokenService.atualizarConfirmacao(pToken);
			usuarioService.ativarUsuario(tokenDTO.getUsuario().getEmail());
			
			// realiza o envio do e-mail informando a ativação
			envioEmail.enviar(new EmailDTO(tokenDTO.getUsuario().getEmail(),
					ConstrucaoEmail.emailConfirmacaoConcluida(token.getUsuario().getNome()),
					ASSUNTO_EMAIL_CONFIRMADO_SUCESSO));
			return ResponseEntity.status(HttpStatus.OK).body("O usuário foi confirmado com sucesso!");
		} else if (tokenDTO.getData_expiracao().isBefore(LocalDateTime.now())) {	// se o token expirou
			String link = LINK_SITE_CONFIRM + usuarioService.gerarNovoToken(tokenDTO);	// seta o novo token gerado no link
			
			// reenvia o e-mail ao(s) gestor(es) para ativação do usuário
			envioEmail.enviar(new EmailDTO(EMAIL_DESTINATARIO_CONFIRMACAO,
					ConstrucaoEmail.emailConfirmacaoPendente(token.getUsuario().getNome(),
							tokenDTO.getUsuario().getSobrenome(), link), ASSUNTO_EMAIL_NOVO_CADASTRO));
			return ResponseEntity.status(HttpStatus.OK).body("Este token expirou! Um novo token foi enviado.");
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado.");
	}

}
