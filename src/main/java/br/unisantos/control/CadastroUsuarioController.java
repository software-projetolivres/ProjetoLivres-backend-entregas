package br.unisantos.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unisantos.model.CadastroUsuario;
import br.unisantos.service.CadastroUsuarioService;

@CrossOrigin
@RestController
@RequestMapping("/api/cadastroUsuario")
public class CadastroUsuarioController {
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public String cadastrar(@RequestBody CadastroUsuario cadastro) {
		return cadastroUsuarioService.cadastrar(cadastro);
	}

	@GetMapping(path = "confirmar")
	public String confirmarCadastro(@RequestParam("token") String token) {
		return cadastroUsuarioService.confirmarToken(token);
	}
}
