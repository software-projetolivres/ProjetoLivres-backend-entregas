package br.unisantos.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.repository.ConsumidorEntregasRepository;

@Service
public class ConsumidorEntregasService {
	@Autowired
	private ConsumidorEntregasRepository repo;

	public String montarListaEntregas(@RequestBody String responseBody)
			throws JsonMappingException, JsonProcessingException {
		String dataEntrega = getDataEntrega(responseBody);
		String consumidorEntregasResponse = postEntregasLivresAPI(dataEntrega);
		String sConsumidorEntregas = montaListaConsumidorEntregas(consumidorEntregasResponse, dataEntrega);

		return sConsumidorEntregas;
	}

	public String getDataEntrega(@RequestBody String responseBody)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("dataEntrega").asText();
	}

	public String montaListaConsumidorEntregas(String consumidorEntregasResponse, String dataEntrega)
			throws JsonMappingException, JsonProcessingException {

		JSONObject root = new JSONObject(consumidorEntregasResponse);
		JSONArray consumidores = root.getJSONArray("data");

		for (int i = 0; i < consumidores.length(); i++) {
			JSONObject jsonConsumidor = consumidores.getJSONObject(i);

			Long id_consumidor = jsonConsumidor.getLong("id_consumidor");
			String nome_consumidor = jsonConsumidor.getString("nome_consumidor");
			Integer comunidade_consumidor = jsonConsumidor.getInt("comunidade_consumidor");
			//Integer telefone_consumidor = jsonConsumidor.getInt("telefone_consumidor");
			//Object telefone_consumidor = jsonConsumidor.opt("telefone_consumidor"); //Devolve um Long
			String endereco_entrega = jsonConsumidor.getString("endereco_entrega");
			String opcao_entrega = jsonConsumidor.getString("opcao_entrega");
			Double valor_entrega = jsonConsumidor.getDouble("valor_entrega");

			if (opcao_entrega != "Não" && opcao_entrega != null && opcao_entrega != "") {
				ConsumidorEntregas consumidorEntregas = new ConsumidorEntregas();
				
				consumidorEntregas.setId_entrega(dataEntrega + "c" + id_consumidor);
				consumidorEntregas.setId_consumidor(id_consumidor);
				consumidorEntregas.setNome_consumidor(nome_consumidor);
				consumidorEntregas.setComunidade_consumidor(comunidade_consumidor);
				//consumidorEntregas.setTelefone_consumidor(telefone_consumidor);
				consumidorEntregas.setEndereco_entrega(endereco_entrega);
				consumidorEntregas.setOpcao_entrega(opcao_entrega);
				consumidorEntregas.setValor_entrega(valor_entrega);
				consumidorEntregas.setData_entrega(dataEntrega);
				salvar(consumidorEntregas);
			}
		}
		
		return new ObjectMapper().writeValueAsString(listarNaoSelecionados(dataEntrega));
	}

	public String roteirizarEntregas(@RequestBody List<ConsumidorEntregas> lConsumidor) {
		// TO DO: persistir no banco as entregas que foram selecionadas e chamar a api
		// do google
		return "";
	}

	public String postEntregasLivresAPI(String dataEntrega) throws JsonMappingException, JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String url = "https://livresbs.com.br/API/entregas/";
		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("date", dataEntrega);
		map.add("token", "oFX1r63Az8RRyVbFBS69RKK96oIha0oj");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		String response = restTemplate.postForObject(url, request, String.class);
		return response.toString();
	}

	public List<ConsumidorEntregas> listarNaoSelecionados(String dataEntrega) {
		List<ConsumidorEntregas> lista = repo.findByEntregaValidaNaoSelec(dataEntrega);
		return lista;
	}

	/*public List<ConsumidorEntregas> listarSelecionadosResponsavel(String resp, String dataEntrega) {
		List<ConsumidorEntregas> lista = repo.findBySelecionadoAndEntregadorResponsavel(resp,
				dataEntrega);
		return lista;
	}*/

	public List<ConsumidorEntregas> listarEntregasInvalidas(String dataEntrega) {
		List<ConsumidorEntregas> lista = repo.findByEntregaAndEnderecoEmpty(dataEntrega);
		return lista;
	}

	public String salvar(ConsumidorEntregas consumidorEntregas) {
		List<ConsumidorEntregas> consumidorExistente = repo
				.findByIdEntrega(consumidorEntregas.getId_entrega());

		if (consumidorExistente.size() > 0) {
			alterar(consumidorEntregas);
		}
		
		consumidorEntregas.setEntregue(false);
		consumidorEntregas.setSelecionado(false);

		repo.save(consumidorEntregas);
		return "Registro criado com sucesso!";
	}

	public String alterar(ConsumidorEntregas consumidorEntregas) {
		List<ConsumidorEntregas> consumidorExistente = repo
				.findByIdEntrega(consumidorEntregas.getId_entrega());

		if (consumidorExistente.size() == 0) {
			return "Não existe nenhum registro com esse id!";
		}

		ConsumidorEntregas cons = consumidorExistente.get(0);
		BeanUtils.copyProperties(consumidorEntregas, cons, "id");
		cons = repo.save(cons);

		return "Registro alterado com sucesso!";
	}
}
