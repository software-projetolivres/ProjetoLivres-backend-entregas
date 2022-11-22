package br.unisantos.control;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import br.unisantos.dto.ConsumidorEntregasDTO;
import br.unisantos.service.ConsumidorEntregasService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/entregas")
public class ConsumidorEntregasController {

	@Autowired
	private ConsumidorEntregasService consumidorEntregasService;
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<ConsumidorEntregasDTO> montarListaEntregas(@RequestBody String dataEntrega) throws JsonMappingException, JsonProcessingException {
		return consumidorEntregasService.montarListaEntregas(dataEntrega);
	}
	
	@GetMapping(path = "entregasInvalidas", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<ConsumidorEntregasDTO> listarEntregasInvalidas(@RequestParam("dataEntrega") String dataEntrega) throws JsonMappingException, JsonProcessingException{
		return consumidorEntregasService.listarEntregasInvalidas(dataEntrega);
	}
	
	@GetMapping(path = "entregasResp", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<ConsumidorEntregasDTO> listarEntregasResponsavel(@RequestParam("dataEntrega") String dataEntrega,
			@RequestParam("resp") String resp) {
		return consumidorEntregasService.listarSelecionadosResponsavel(dataEntrega, resp);
	}
	 
	@PostMapping(value="/roteirizar", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public DirectionsResult roteirizarEntregas(@RequestBody List<ConsumidorEntregasDTO> entregas) throws ApiException, InterruptedException, IOException{
		return consumidorEntregasService.roteirizarEntregas(entregas);
	}
	
	@PutMapping(value="/atualizar/{email}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> atualizarEntregas(@RequestBody List<ConsumidorEntregasDTO> entregas, @PathVariable("email") String email){
		return consumidorEntregasService.atualizarEntregas(entregas, email);
	}
}