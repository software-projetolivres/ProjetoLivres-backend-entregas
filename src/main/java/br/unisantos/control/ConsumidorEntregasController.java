package br.unisantos.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.service.ConsumidorEntregasService;

@RestController
@RequestMapping("/consumidorEntregas")
public class ConsumidorEntregasController {

	@Autowired
	private ConsumidorEntregasService consumidorEntregasService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String montarListaEntregas(@RequestBody String dataEntrega) throws JsonMappingException, JsonProcessingException {
		return consumidorEntregasService.montarListaEntregas(dataEntrega);
		//return consumidorEntregasService.postEntregasLivresAPI(consumidorEntregasService.getDataEntrega(dataEntrega));
	}
	 
	@PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
				produces = {MediaType.APPLICATION_JSON_VALUE})
	public String roteirizarEntregas(@RequestBody List<ConsumidorEntregas> lConsumidor){
		return consumidorEntregasService.roteirizarEntregas(lConsumidor);
	}
	
	/*@PostMapping(value="/entregasInvalidas")
	public List<ConsumidorEntregas> listarEntregasInvalidas(@RequestBody String dataEntrega) throws JsonMappingException, JsonProcessingException{
		return consumidorEntregasService.listarEntregasInvalidas(consumidorEntregasService.getDataEntrega(dataEntrega));
	}*/
	
}