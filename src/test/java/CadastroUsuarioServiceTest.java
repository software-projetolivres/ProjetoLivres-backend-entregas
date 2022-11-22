import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import br.unisantos.dto.EmailDTO;
import br.unisantos.dto.TokenDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.email.EnvioEmail;
import br.unisantos.functions.ConstrucaoEmail;
import br.unisantos.mapper.TokenMapper;
import br.unisantos.model.Token;
import br.unisantos.model.Usuario;
import br.unisantos.model.UsuarioTipoPerfil;
import br.unisantos.service.CadastroUsuarioService;
import br.unisantos.service.TokenService;
import br.unisantos.service.UsuarioService;

public class CadastroUsuarioServiceTest {

	@InjectMocks
	private CadastroUsuarioService cadastroUsuarioService;

	@InjectMocks
	private ConstrucaoEmail consEmail;

	@Mock
	private UsuarioService usuarioService;

	@Mock
	private TokenService tokenService;

	@Mock
	private EnvioEmail envioEmail;

	@Mock
	private TokenMapper tokenMapper;

	@Mock
	private EmailDTO emailDTO;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testCadastrarJaExiste() {
		UsuarioDTO usuarioDTO = getUsuario();

		// When
		Mockito.when(usuarioService.findByEmail(usuarioDTO.getEmail())).thenReturn(usuarioDTO);

		// Then
		ResponseEntity<String> resposta = cadastroUsuarioService.cadastrar(usuarioDTO);

		Assert.assertTrue(resposta.getStatusCodeValue() == 409);
	}

	@Test
	void testCadastrarEmailInvalido() {
		UsuarioDTO usuarioDTO = getUsuario();
		usuarioDTO.setEmail("@tst.com");

		ResponseEntity<String> resposta = cadastroUsuarioService.cadastrar(usuarioDTO);

		Assert.assertTrue(resposta.getStatusCodeValue() == 400);
	}
	 
	@Test
	void testCadastrarEmailValido() {
		UsuarioDTO usuarioDTO = getUsuario();

		// Then
		ResponseEntity<String> resposta = cadastroUsuarioService.cadastrar(usuarioDTO);

		Assert.assertTrue(resposta.getStatusCodeValue() == 200);
	}
	
	@Test
	void testConfirmarTokenJaConfirmado() {
		Token t = getToken();
		Optional<Token> token = Optional.of(t);
		
		TokenDTO tokenDTO = getTokenDTO();
		tokenDTO.setData_confirmacao(LocalDateTime.now());
		
		// When
		Mockito.when(tokenService.encontrarToken("123456")).thenReturn(token);
		Mockito.when(tokenMapper.toDTO(token.get())).thenReturn(tokenDTO);

		// Then
		ResponseEntity<String> resposta = cadastroUsuarioService.confirmarToken("123456");

		Assert.assertTrue(resposta.getStatusCodeValue() == 409);
	}
	
	@Test
	void testConfirmarTokenTokenValido() {
		Token t = getToken();
		Optional<Token> token = Optional.of(t);
		
		TokenDTO tokenDTO = getTokenDTO();
		
		
		// When
		Mockito.when(tokenService.encontrarToken("123456")).thenReturn(token);
		Mockito.when(tokenMapper.toDTO(token.get())).thenReturn(tokenDTO);
		Mockito.when(tokenService.usuarioTemTokenValido(tokenDTO.getUsuario())).thenReturn(true);

		// Then
		ResponseEntity<String> resposta = cadastroUsuarioService.confirmarToken("123456");

		Assert.assertTrue(resposta.getStatusCodeValue() == 200);
	}
	
	@Test
	void testConfirmarTokenTokenExpirado() {
		Token t = getToken();
		Optional<Token> token = Optional.of(t);
		
		TokenDTO tokenDTO = getTokenDTO();
		tokenDTO.setData_criacao(LocalDateTime.now().plusHours(-4));
		tokenDTO.setData_expiracao(LocalDateTime.now().plusHours(-2));
		
		// When
		Mockito.when(tokenService.encontrarToken("123456")).thenReturn(token);
		Mockito.when(tokenMapper.toDTO(token.get())).thenReturn(tokenDTO);

		// Then
		ResponseEntity<String> resposta = cadastroUsuarioService.confirmarToken("123456");

		Assert.assertTrue(resposta.getStatusCodeValue() == 200);
	}

	private UsuarioDTO getUsuario() {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNome("Teste");
		usuarioDTO.setSobrenome("Teste");
		usuarioDTO.setEmail("emailgsmftst@gmail.com");
		usuarioDTO.setSenha("123456");
		usuarioDTO.setUsuarioTipoPerfil(UsuarioTipoPerfil.USUARIO);
		usuarioDTO.setBloqueado(false);
		usuarioDTO.setAtivo(true);
		return usuarioDTO;
	}
	
	private Usuario getUsuarioE() {
		Usuario usuario = new Usuario();
		usuario.setNome("Teste");
		usuario.setSobrenome("Teste");
		usuario.setEmail("emailgsmftst@gmail.com");
		usuario.setSenha("123456");
		usuario.setUsuarioTipoPerfil(UsuarioTipoPerfil.USUARIO);
		usuario.setBloqueado(false);
		usuario.setAtivo(true);
		return usuario;
	}
	
	private Token getToken() {
		Token token = new Token();
		token.setToken("123456");
		token.setData_criacao(LocalDateTime.now());
		token.setData_expiracao(LocalDateTime.now().plusHours(2));
		token.setUsuario(getUsuarioE());
		
		return token;
	}
	
	private TokenDTO getTokenDTO() {
		TokenDTO token = new TokenDTO();
		token.setToken("123456");
		token.setData_criacao(LocalDateTime.now());
		token.setData_expiracao(LocalDateTime.now().plusHours(2));
		token.setUsuario(getUsuario());
		
		return token;
	}
}
