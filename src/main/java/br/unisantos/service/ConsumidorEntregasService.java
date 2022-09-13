package br.unisantos.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import br.unisantos.dto.ConsumidorEntregasDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.functions.DataEntrega;
import br.unisantos.mapper.ConsumidorEntregasMapper;
import br.unisantos.mapper.UsuarioMapper;
import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.model.DirectionsGoogleApi;
import br.unisantos.model.Usuario;
import br.unisantos.repository.ConsumidorEntregasRepository;
import br.unisantos.repository.UsuarioRepository;

@Service
public class ConsumidorEntregasService {
	
	@Autowired
	private ConsumidorEntregasRepository repo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private DirectionsGoogleApiService directionsAPIService;
	
	@Autowired
	private ConsumidorEntregasMapper entregasMapper;
	
	@Autowired
	private UsuarioMapper usuarioMapper;

	public List<ConsumidorEntregasDTO> montarListaEntregas(@RequestBody String req)
			throws JsonMappingException, JsonProcessingException {
		String dataEntrega = DataEntrega.getDataEntrega(req);
		String consumidorEntregasResponse = postEntregasLivresAPI(dataEntrega);
		List<ConsumidorEntregasDTO> consumidoresEntregas = montaListaConsumidorEntregas(consumidorEntregasResponse, dataEntrega);

		return consumidoresEntregas;
	}
	
	public List<ConsumidorEntregasDTO> montaListaConsumidorEntregas(String consumidorEntregasResponse, String dataEntrega)
			throws JsonMappingException, JsonProcessingException {
		JSONObject root = new JSONObject(consumidorEntregasResponse);
		
		if(root.has("data")) {		
			JSONArray entregas = root.getJSONArray("data");
			
			for(int i = 0; i < entregas.length(); i++) {
				JSONObject entregaJSON = entregas.getJSONObject(i);
				ConsumidorEntregasDTO entrega = new ConsumidorEntregasDTO();
				String opcao_entrega = entregaJSON.optString("opcao_entrega");
				
				if(opcao_entrega.equals("Sim") && opcao_entrega != null && !opcao_entrega.isEmpty()) {
					entrega.setId(dataEntrega + "c" + entregaJSON.optLong("id_consumidor"));
					entrega.setId_consumidor(entregaJSON.optLong("id_consumidor"));
					entrega.setNome_consumidor(entregaJSON.optString("nome_consumidor"));
					entrega.setComunidade_consumidor(entregaJSON.optInt("comunidade_consumidor"));
					entrega.setTelefone_consumidor(entregaJSON.optString("telefone_consumidor"));
					entrega.setEndereco_entrega(entregaJSON.optString("endereco_entrega"));
					entrega.setOpcao_entrega(entregaJSON.optString("opcao_entrega"));
					entrega.setValor_entrega(entregaJSON.optDouble("valor_entrega"));
					entrega.setData_entrega(dataEntrega);
					salvar(entrega);
				}
			}
		}
		
		return listarNaoSelecionados(dataEntrega);
	}

	public DirectionsResult roteirizarEntregas(@RequestBody String requestBody) throws ApiException, InterruptedException, IOException {
		String enderecoLivres = "Almeida de Moraes 175, Vila Mathias, Santos SP";
		List<String> waypoints = new ArrayList<String>();
		JSONObject root = new JSONObject(requestBody);
		JSONArray entregas = root.getJSONArray("entregas");
		
		DirectionsGoogleApi directionsAPI = new DirectionsGoogleApi();
		directionsAPI.setOrigin(enderecoLivres);
		directionsAPI.setDestination(enderecoLivres);
		directionsAPI.setOptimizeWaypoints(true);
		
		for(int i = 0; i < entregas.length(); i++) {
			JSONObject jsonIdsEntregas = entregas.getJSONObject(i);
			
			String id_entrega = jsonIdsEntregas.getString("id_entrega");
			Optional<ConsumidorEntregas> entrega = repo.findById(id_entrega);
			String endereco = entrega.get().getEndereco_entrega();
			
			if(entrega.isPresent()) {
				endereco = endereco + " baixada santista";
				waypoints.add(endereco);	
			}
		}

		directionsAPI.setWaypoints(waypoints);
		return directionsAPIService.directionsApiGoogle(directionsAPI);
	}

	public String postEntregasLivresAPI(String dataEntrega) throws JsonMappingException, JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String url = System.getenv("url_api_entregas");
		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("date", dataEntrega);
		map.add("token", System.getenv("token"));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		String response = restTemplate.postForObject(url, request, String.class);
		
		return response.toString();
	}

	public List<ConsumidorEntregasDTO> listarNaoSelecionados(String dataEntrega) {
		List<ConsumidorEntregasDTO> lista = entregasMapper.toDTO(repo.entregasValidasNaoSelecionadas(dataEntrega));
		return lista;
	}

	public List<ConsumidorEntregasDTO> listarSelecionadosResponsavel(String dataEntrega, String email) {
		
		Optional<Usuario> entregador = usuarioRepo.findByEmail(email);
		if (!entregador.isPresent()) {
			return null;
		}
		
		List<ConsumidorEntregasDTO> lista = entregasMapper.toDTO(repo.entregasSelecionadasPorEntregador(dataEntrega, entregador.get()));
		return lista;
	}

	public List<ConsumidorEntregasDTO> listarEntregasInvalidas(String dataEntrega) {
		List<ConsumidorEntregasDTO> lista = entregasMapper.toDTO(repo.entregasInvalidas(dataEntrega));
		return lista;
	}
	
	public ResponseEntity<String> atualizarEntregas(String requestBody) {
		String result = "Entrega(s) atualizada(s) com sucesso!";
		Boolean msgEntregaAtribuida = false, msgEntregaInexistente = false;
		JSONObject root = new JSONObject(requestBody);
		String emailEntregador = root.getString("emailEntregador");
		JSONArray idsEntregas = root.getJSONArray("entregas");
		
		Optional<Usuario> entregador = usuarioRepo.findByEmail(emailEntregador);
		UsuarioDTO entregadorDTO = usuarioMapper.toDTO(entregador.get());
		
		if (!entregador.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Este e-mail não pertence a nenhum entregador cadastrado no sistema.");
		}
		
		for(int i = 0; i < idsEntregas.length(); i++) {
			JSONObject jsonIdsEntregas = (JSONObject) idsEntregas.get(i);
			String id_entrega = jsonIdsEntregas.getString("id_entrega");
			Boolean selecionadoJson = jsonIdsEntregas.getBoolean("selecionado");
			Boolean entregueJson = jsonIdsEntregas.getBoolean("entregue");
			Optional<ConsumidorEntregas> entrega = repo.findById(id_entrega);
			
			if(entrega.isPresent()) {
				ConsumidorEntregasDTO consumidorEntregas = entregasMapper.toDTO(entrega.get());
				Boolean selecionado = consumidorEntregas.getSelecionado();
				Boolean entregue = consumidorEntregas.getEntregue();
				
				if(selecionadoJson != selecionado) {	//caso o campo "selecionado" tenha sido alterado
					consumidorEntregas.setSelecionado(selecionadoJson);
					consumidorEntregas.setEntregador_responsavel(!selecionadoJson ? null : entregadorDTO);
				} else {
					if(consumidorEntregas.getSelecionado() == true && consumidorEntregas.getEntregador_responsavel() != entregadorDTO) {
						msgEntregaAtribuida = true;
					}
				}
				
				if(entregueJson != entregue) {	//caso o campo "entregue" tenha sido alterado
					consumidorEntregas.setEntregue(entregueJson);
				}
				
				alterar(consumidorEntregas);
			} else {
				msgEntregaInexistente = true;
			}		
		}
		
		result = result + (msgEntregaAtribuida ? " Alguma(s) entrega(s) pode(m) não ter sido atribuída(s) a você "
				+ "pois alguém já se apropriou." : "");
		result = result + (msgEntregaInexistente ? " Alguma(s) entrega(s) selecionada(s) não existe(m) no sistema." : "");
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	public String salvar(ConsumidorEntregasDTO consumidorEntregas) {
		Optional<ConsumidorEntregas> cons = repo
				.findById(consumidorEntregas.getId());
		
		if (cons.isPresent()) {
			ConsumidorEntregasDTO consumidorExistente = entregasMapper.toDTO(cons.get());
			consumidorEntregas.setEntregue(consumidorExistente.getEntregue());
			consumidorEntregas.setSelecionado(consumidorExistente.getSelecionado());
			consumidorEntregas.setEntregador_responsavel(consumidorExistente.getEntregador_responsavel());
			return alterar(consumidorEntregas);
		}
		
		consumidorEntregas.setEntregue(false);
		consumidorEntregas.setSelecionado(false);

		repo.save(entregasMapper.toEntity(consumidorEntregas));
		return "Registro criado com sucesso!";
	}

	public String alterar(ConsumidorEntregasDTO consumidorEntregas) {
		ConsumidorEntregasDTO cons = consumidorEntregas;
		BeanUtils.copyProperties(consumidorEntregas, cons, "id");
		
		if(consumidorEntregas.getOpcao_entrega() == "Não") {
			deletar(consumidorEntregas);
		}
		repo.save(entregasMapper.toEntity(cons));

		return "Registro(s) atualizados(s) com sucesso!";
	}
	
	public void deletar(ConsumidorEntregasDTO consumidorEntregas) {
		repo.deleteById(consumidorEntregas.getId());
	}
}
