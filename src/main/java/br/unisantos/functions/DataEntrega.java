package br.unisantos.functions;

import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataEntrega {
	
	// Método auxiliar que tem como objetivo retornar a data de entrega em formato de texto a partir do JSON recebido. 
	public static String getDataEntrega(@RequestBody String responseBody)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("dataEntrega").asText();
	}
}
