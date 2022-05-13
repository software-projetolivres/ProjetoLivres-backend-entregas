package br.unisantos.service;

import java.util.List;
import java.util.Optional;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unisantos.functions.DataEntrega;
import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.model.Usuario;
import br.unisantos.repository.ConsumidorEntregasRepository;
import br.unisantos.repository.UsuarioRepository;

@Service
public class ConsumidorEntregasService {
	
	@Autowired
	private ConsumidorEntregasRepository repo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;

	public String montarListaEntregas(@RequestBody String responseBody)
			throws JsonMappingException, JsonProcessingException {
		String dataEntrega = DataEntrega.getDataEntrega(responseBody);
		String consumidorEntregasResponse = postEntregasLivresAPI(dataEntrega);
		String sConsumidorEntregas = montaListaConsumidorEntregas(consumidorEntregasResponse, dataEntrega);

		return sConsumidorEntregas;
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
				
				consumidorEntregas.setId(dataEntrega + "c" + id_consumidor);
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
	
	public String atualizarSelecionarEntregas(String requestBody){
		String result = "Entrega(s) atualizada(s) com sucesso!";
		Boolean msgEntregaAtribuida = false, msgEntregaInexistente = false;
		JSONObject root = new JSONObject(requestBody);
		String emailEntregador = root.getString("emailEntregador");
		JSONArray idsEntregas = root.getJSONArray("data");
		
		Optional<Usuario> entregador = usuarioRepo.findByEmail(emailEntregador);
		if (!entregador.isPresent()) {
			return "Este e-mail não pertence a nenhum entregador cadastrado no sistema.";
		}

		for (int i = 0; i < idsEntregas.length(); i++) {
			JSONObject jsonIdsEntregas = idsEntregas.getJSONObject(i);
			String id_entrega = jsonIdsEntregas.getString("id_entrega");
			Optional<ConsumidorEntregas> entrega = repo.findById(id_entrega);
			
			if(entrega.isPresent()) {
				if(entrega.get().getSelecionado() == false) {
					entrega.get().setSelecionado(true);
					entrega.get().setEntregador_responsavel(entregador.get());
					alterar(entrega.get());
				} else {
					msgEntregaAtribuida = true;
				}
			} else {
				msgEntregaInexistente = true;
			}		
		}
		
		result = result + (msgEntregaAtribuida ? " Alguma(s) entrega(s) pode(m) não ter sido atribuída(s) a você "
				+ "pois alguém já se apropriou" : "");
		result = result + (msgEntregaInexistente ? " Alguma(s) entrega(s) selecionada(s) não existe(m) no sistema." : "");
		return result;
	}

	public String salvar(ConsumidorEntregas consumidorEntregas) {
		Optional<ConsumidorEntregas> consumidorExistente = repo
				.findById(consumidorEntregas.getId());

		if (consumidorExistente.isPresent()) {
			return alterar(consumidorExistente.get());
		}
		
		consumidorEntregas.setEntregue(false);
		consumidorEntregas.setSelecionado(false);

		repo.save(consumidorEntregas);
		return "Registro criado com sucesso!";
	}

	public String alterar(ConsumidorEntregas consumidorEntregas) {
		ConsumidorEntregas cons = consumidorEntregas;
		BeanUtils.copyProperties(consumidorEntregas, cons, "id");
		
		if(consumidorEntregas.getOpcao_entrega() == "Não") {
			deletar(consumidorEntregas);
		}
		cons = repo.save(cons);

		return "Registro(s) atualizados(s) com sucesso!";
	}
	
	public void deletar(ConsumidorEntregas consumidorEntregas) {
		repo.deletar(consumidorEntregas.getId());
	}
}
