import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.unisantos.dto.TokenDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.mapper.UsuarioMapper;
import br.unisantos.model.Usuario;
import br.unisantos.model.UsuarioTipoPerfil;
import br.unisantos.repository.UsuarioRepository;
import br.unisantos.service.TokenService;
import br.unisantos.service.UsuarioService;

public class UsuarioServiceTest {
	
	@InjectMocks
	private UsuarioService usuarioService;

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@Mock
	private TokenService tokenService;
	
	@Mock
	private UsuarioMapper usuarioMapper;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testFindByEmail() {
		Usuario usuario = getUsuario();
		Optional<Usuario> optUsuario = Optional.of(usuario);
		UsuarioDTO usuarioDTO = getUsuarioDTO();

		// When
		Mockito.when(usuarioRepo.findByEmail(usuarioDTO.getEmail())).thenReturn(optUsuario);
		Mockito.when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);
		
		// Then
		UsuarioDTO u = usuarioService.findByEmail("email@teste.com");

		Assert.assertEquals(u, usuarioDTO);
	}
	
	@Test
	void testCadastrar() {
		Usuario usuario = getUsuario();
		UsuarioDTO usuarioDTO = getUsuarioDTO();

		// When
		Mockito.when(passwordEncoder.encode(usuario.getSenha())).thenReturn("1!2@3#4$");
		Mockito.when(usuarioMapper.toDTO(usuarioRepo.save(usuarioMapper.toEntity(usuarioDTO)))).thenReturn(usuarioDTO);

		// Then
		String tkn = usuarioService.cadastrar(usuarioDTO);
		Assert.assertNotEquals(tkn, null);
	}
	
	@Test
	void testAtivarUsuario() {
		Usuario usuario = getUsuario();
		Optional<Usuario> optUsuario = Optional.of(usuario);
		UsuarioDTO usuarioDTO = getUsuarioDTO();

		// When
		Mockito.when(usuarioRepo.findByEmail("email@teste.com")).thenReturn(optUsuario);
		Mockito.when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

		// Then
		usuarioService.ativarUsuario("email@teste.com");

		//Assert.assertEquals(u, usuarioDTO);
	}
	
	@Test
	void testGerarNovoToken() {
		TokenDTO tokenDTO = getTokenDTO();

		// When
		//Mockito.doNothing(tokenService.salvar(novoToken));

		// Then
		String tkn = usuarioService.gerarNovoToken(tokenDTO);
		Assert.assertNotEquals(tkn, null);
	}
	
	private UsuarioDTO getUsuarioDTO() {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNome("Teste");
		usuarioDTO.setSobrenome("Teste");
		usuarioDTO.setEmail("email@teste.com");
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
		usuario.setEmail("email@teste.com");
		usuario.setSenha("123456");
		usuario.setUsuarioTipoPerfil(UsuarioTipoPerfil.USUARIO);
		usuario.setBloqueado(false);
		usuario.setAtivo(true);
		return usuario;
	}
	
	private TokenDTO getTokenDTO() {
		TokenDTO token = new TokenDTO();
		token.setToken("123456");
		token.setData_criacao(LocalDateTime.now());
		token.setData_expiracao(LocalDateTime.now().plusHours(2));
		token.setUsuario(getUsuarioDTO());
		
		return token;
	}
}
