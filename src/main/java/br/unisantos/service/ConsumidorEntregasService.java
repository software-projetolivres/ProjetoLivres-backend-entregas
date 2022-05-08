package br.unisantos.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.repository.ConsumidorEntregasRepository;

@Service
public class ConsumidorEntregasService {
	@Autowired
	ConsumidorEntregasRepository consumidorEntregasRepo;
	
	public List<ConsumidorEntregas> montarListaEntregas(@RequestBody Date dataEntrega) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
        String strDate = dateFormat.format(dataEntrega);
		List<ConsumidorEntregas> lConsumidorEntregas = postEntregasLivresAPI(strDate);
		//lConsumidorEntregas = montaListaConsumidorEntregas(lConsumidorEntregas);
		
		//Verificar no bd se há algum registro com a data passada, caso haja, atualiza no bd
		//senão, vamos CRIAR os registros no bd
		return lConsumidorEntregas;
	}
	
	/*public montaListaConsumidorEntregas(List<ConsumidorEntregas> lConsumidorEntregas) {
	 	// TO DO: implementar o método que verifica se a pessoa optou por entrega e se o end está null
	 	//se estiver null, devemos atribuir a uma lista de endereços nulos para informar, senão
	 	//apenas adicionamos para o retorno do método
	 }*/
	
	public String roteirizarEntregas(@RequestBody List<ConsumidorEntregas> lConsumidor){
		//TO DO: persistir no banco as entregas que foram selecionadas e chamar a api do google
		return "";
	}
	
	@PostMapping
	public List<ConsumidorEntregas> postEntregasLivresAPI(String dataEntrega){
		String uri = "https://livresbs.com.br/API/entregas/";
		String token = "oFX1r63Az8RRyVbFBS69RKK96oIha0oj";
		
		Map<String, String> params = new HashMap<>();
		params.put("data", dataEntrega);
		params.put("token", token);

		RestTemplate restTemplate = new RestTemplate();
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		restTemplate = restTemplateBuilder.build();
		
		ConsumidorEntregas[] consumidores = restTemplate.postForObject(uri, null, ConsumidorEntregas[].class, params);
		
		System.out.println();
		
		return Arrays.asList(consumidores);
	}
}
