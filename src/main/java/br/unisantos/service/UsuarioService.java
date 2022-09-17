package br.unisantos.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.unisantos.dto.TokenDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.mapper.UsuarioMapper;
import br.unisantos.model.Usuario;
import br.unisantos.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepo;
	private final static String USUARIO_EMAIL_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário com o e-mail inserido.";

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioMapper usuarioMapper;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USUARIO_EMAIL_NAO_ENCONTRADO)));
	}
	
	/* Método responsável por fazer a chamada à camada de repositório, devolvendo, se houver, um Usuario com o e-mail passado */
	public UsuarioDTO findByEmail(String email){
		if(usuarioRepo.findByEmail(email).isPresent()) {
			return usuarioMapper.toDTO(usuarioRepo.findByEmail(email).get());
		} else {
			return null;
		}
	}

	/* Método responsável por codificar a senha passada, salvar o usuário no banco de dados e devolver o token */
	public String cadastrar(UsuarioDTO usuario) {
		String senhaCodificada = passwordEncoder.encode(usuario.getSenha());	//codifica a senha para salvar no BD
		usuario.setSenha(senhaCodificada);
		UsuarioDTO usuarioInserido = usuarioMapper.toDTO(usuarioRepo.save(usuarioMapper.toEntity(usuario)));

		String tokenGerado = UUID.randomUUID().toString();
		TokenDTO token = new TokenDTO(tokenGerado, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), usuarioInserido);
		tokenService.salvar(token);

		return token.getToken();
	}

	/* Método responsável por atualizar o campo "Ativo" do usuário que é buscado a partir do e-mail passado */
	public void ativarUsuario(String email) {
		Optional<Usuario> usuario = usuarioRepo.findByEmail(email);

		if (usuario.isPresent()) {
			UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario.get());
			usuarioDTO.setAtivo(true);
			usuarioRepo.save(usuarioMapper.toEntity(usuarioDTO));
		}
	}

	// Método responsável por gerar e devolver um novo token para o usuário do token expirado passado
	public String gerarNovoToken(TokenDTO token) {
		String tokenGerado = UUID.randomUUID().toString();
		TokenDTO novoToken = new TokenDTO(tokenGerado, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120),
				token.getUsuario());
		tokenService.salvar(novoToken);

		return tokenGerado;
	}
}
