import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.unisantos.dto.TokenDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.mapper.TokenMapper;
import br.unisantos.model.Token;
import br.unisantos.model.Usuario;
import br.unisantos.model.UsuarioTipoPerfil;
import br.unisantos.repository.TokenRepository;
import br.unisantos.service.TokenService;

public class TokenServiceTest {
	
	@InjectMocks
	private TokenService tokenService;
	
	@Mock
	private TokenRepository tokenRepo;
	
	@Mock
	private TokenMapper tokenMapper;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testSalvar() {
		Token token = getToken();
		TokenDTO tokenDTO = getTokenDTO();
		Optional<Token> optToken = Optional.of(token);

		// When
		Mockito.when(tokenMapper.toEntity(tokenDTO)).thenReturn(token);

		// Then
		tokenService.salvar(tokenDTO);
	}
	
	@Test
	void testEncontrarToken() {
		Token token = getToken();
		Optional<Token> optToken = Optional.of(token);

		// When
		Mockito.when(tokenRepo.findByToken(token.getToken())).thenReturn(optToken);

		// Then
		Optional<Token> tkn = tokenService.encontrarToken(token.getToken());

		Assert.assertEquals(optToken, tkn);
	}
	
	@Test
	void testAtualizarConfirmacao() {
		Token token = getToken();
		Optional<Token> optToken = Optional.of(token);

		// When
		Mockito.when(tokenService.encontrarToken(token.getToken())).thenReturn(optToken);

		// Then
		tokenService.atualizarConfirmacao(token.getToken());
	}
	
	@Test
	void testUsuarioTemTokenValido() {
		Token token = getToken();
		Optional<Token> optToken = Optional.of(token);
		UsuarioDTO usuarioDTO = getUsuarioDTO();
		ArrayList<Token> tokenList = new ArrayList<>();
		tokenList.add(token);

		// When
		Mockito.when(tokenRepo.procurarTokenValidoUsuario(usuarioDTO.getId())).thenReturn(tokenList);

		// Then
		Boolean temTokenValido = tokenService.usuarioTemTokenValido(usuarioDTO);
		
		Assert.assertEquals(temTokenValido, true);
	}
	
	private Token getToken() {
		Token token = new Token();
		token.setToken("123456");
		token.setData_criacao(LocalDateTime.now());
		token.setData_expiracao(LocalDateTime.now().plusHours(2));
		token.setUsuario(getUsuario());
		
		return token;
	}
	
	private TokenDTO getTokenDTO() {
		TokenDTO token = new TokenDTO();
		token.setToken("123456");
		token.setData_criacao(LocalDateTime.now());
		token.setData_expiracao(LocalDateTime.now().plusHours(2));
		token.setUsuario(getUsuarioDTO());
		
		return token;
	}
	
	private UsuarioDTO getUsuarioDTO() {
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
	
	private Usuario getUsuario() {
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
}
