package br.unisantos.control;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.service.ConsumidorEntregasService;

@RestController
@RequestMapping("/api/entregas")
public class ConsumidorEntregasController {

	@Autowired
	private ConsumidorEntregasService consumidorEntregasService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String montarListaEntregas(@RequestBody String dataEntrega) throws JsonMappingException, JsonProcessingException {
		return consumidorEntregasService.montarListaEntregas(dataEntrega);
	}
	
	@GetMapping(path = "entregasInvalidas", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<ConsumidorEntregas> listarEntregasInvalidas(@RequestParam("dataEntrega") String dataEntrega) throws JsonMappingException, JsonProcessingException{
		return consumidorEntregasService.listarEntregasInvalidas(dataEntrega);
	}
	
	@GetMapping(path = "entregasResp", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<ConsumidorEntregas> listarEntregasResponsavel(@RequestParam("dataEntrega") String dataEntrega,
			@RequestParam("resp") String resp) {
		return consumidorEntregasService.listarSelecionadosResponsavel(dataEntrega, resp);
	}
	 
	@PutMapping(value="/roteirizar", produces = {MediaType.APPLICATION_JSON_VALUE})
	public DirectionsResult roteirizarEntregas(@RequestBody String requestBody) throws ApiException, InterruptedException, IOException{
		return consumidorEntregasService.roteirizarEntregas(requestBody);
	}
	
	@PutMapping(value="/atualizar")
	public String atualizarEntregas(@RequestBody String requestBody){
		return consumidorEntregasService.atualizarEntregas(requestBody);
	}
	
	@GetMapping
	public String teste() {
		return "Teste GET! Ok!";
	}
	
}