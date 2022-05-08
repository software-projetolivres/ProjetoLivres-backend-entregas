package br.unisantos.control;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.service.ConsumidorEntregasService;

@RestController
@RequestMapping("/consumidorEntregas")
public class ConsumidorEntregasController {

	@Autowired
	private ConsumidorEntregasService consumidorEntregasService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<ConsumidorEntregas> montarListaEntregas(@RequestBody Date dataEntrega){
		return consumidorEntregasService.montarListaEntregas(dataEntrega);
	}
	
	@PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
				produces = {MediaType.APPLICATION_JSON_VALUE})
	public String roteirizarEntregas(@RequestBody List<ConsumidorEntregas> lConsumidor){
		return consumidorEntregasService.roteirizarEntregas(lConsumidor);
	}
	
}