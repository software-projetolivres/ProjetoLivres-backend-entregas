package br.unisantos.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unisantos.model.CadastroUsuario;
import br.unisantos.service.CadastroUsuarioService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/cadastroUsuario")
public class CadastroUsuarioController {
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@PostMapping()
	public ResponseEntity<String> cadastrar(@RequestBody CadastroUsuario cadastro) {
		return cadastroUsuarioService.cadastrar(cadastro);
	}

	@GetMapping(value = "/{token}")
	public ResponseEntity<String> confirmarCadastro(@PathVariable String token) {
		return cadastroUsuarioService.confirmarToken(token);
	}
}
