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

import br.unisantos.model.Token;
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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USUARIO_EMAIL_NAO_ENCONTRADO)));
	}

	public String cadastrar(Usuario usuario) {
		boolean usuarioCadastrado = usuarioRepo.findByEmail(usuario.getEmail()).isPresent();

		if (usuarioCadastrado) {
			return "Já existe um usuário cadastrado com este e-mail!";
		}

		String senhaCodificada = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCodificada);
		usuarioRepo.save(usuario);

		String tokenGerado = UUID.randomUUID().toString();
		Token token = new Token(tokenGerado, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120), usuario);
		tokenService.salvar(token);

		return "Seu cadastro foi realizado com sucesso!";
	}

	public void ativarUsuario(String email) {
		Optional<Usuario> usuario = usuarioRepo.findByEmail(email);

		if (usuario.isPresent()) {
			Usuario usuarioToUpdate = usuario.get();
			usuarioToUpdate.setAtivo(true);
			usuarioRepo.save(usuarioToUpdate);
		}
	}

	public String gerarNovoToken(Token token) {
		String tokenGerado = UUID.randomUUID().toString();
		Token novoToken = new Token(tokenGerado, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120),
				token.getUsuario());
		tokenService.salvar(novoToken);

		return "Um novo token foi gerado.";
	}
}
