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
import br.unisantos.repository.ConsumidorEntregasRepository;

@Service
public class ConsumidorEntregasService {
	
	@Autowired
	private ConsumidorEntregasRepository repo;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private DirectionsGoogleApiService directionsAPIService;
	
	@Autowired
	private ConsumidorEntregasMapper entregasMapper;
	
	@Autowired
	private UsuarioMapper usuarioMapper;

	/* Método responsável por devolver a lista de ConsumidorEntregasDTO à aplicação */
	public List<ConsumidorEntregasDTO> montarListaEntregas(@RequestBody String req)
			throws JsonMappingException, JsonProcessingException {
		String dataEntrega = DataEntrega.getDataEntrega(req);	// pega a data de entrega do JSON
		String consumidorEntregasResponse = postEntregasLivresAPI(dataEntrega);	// captura as entregas direto da API do livres
		List<ConsumidorEntregasDTO> consumidoresEntregas = montaListaConsumidorEntregas(consumidorEntregasResponse, dataEntrega); // salva as entregas e as devolve do BD

		return consumidoresEntregas;
	}
	
	/* Método responsável por salvar as entregas vindas da API no BD e devolver as importantes para a montagem da lista de não selecionadas */
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
	
	/* Método responsável por devolver o DirectionsResult à aplicação, de acordo com as entregas passadas para roteirização */
	public DirectionsResult roteirizarEntregas(@RequestBody List<ConsumidorEntregasDTO> entregas) throws ApiException, InterruptedException, IOException {
		String enderecoLivres = "Almeida de Moraes 175, Vila Mathias, Santos SP";
		List<String> waypoints = new ArrayList<String>();
		
		DirectionsGoogleApi directionsAPI = new DirectionsGoogleApi();
		directionsAPI.setOrigin(enderecoLivres);
		directionsAPI.setDestination(enderecoLivres);
		directionsAPI.setOptimizeWaypoints(true);
		
		for(ConsumidorEntregasDTO c :entregas) {
			ConsumidorEntregasDTO entrega = findById(c.getId());
			
			// montagem de um waypoint
			if(entrega != null) {
				String endereco = entrega.getEndereco_entrega() + " baixada santista";	// adicionando 'BS' p/ tentar contornar GAP de infos. de endereço
				waypoints.add(endereco);	
			}
		}

		directionsAPI.setWaypoints(waypoints);
		return directionsAPIService.directionsApiGoogle(directionsAPI);
	}

	/* Método responsável por devolver um JSON em formato de String com as entregas da data passada,
	 * informações consumidas da API do próprio sistema do Livres */
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

	/* Método responsável por devolver a lista de ConsumidorEntregasDTO das entregas válidas não selecionadas */
	public List<ConsumidorEntregasDTO> listarNaoSelecionados(String dataEntrega) {
		List<ConsumidorEntregasDTO> lista = entregasMapper.toDTO(repo.entregasValidasNaoSelecionadas(dataEntrega));
		return lista;
	}

	/* Método responsável por devolver a lista de ConsumidorEntregasDTO das entregas de acordo com o entregador responsável */
	public List<ConsumidorEntregasDTO> listarSelecionadosResponsavel(String dataEntrega, String email) {
		UsuarioDTO entregador = usuarioService.findByEmail(email);
		if (entregador == null) {
			return null;
		}
		
		List<ConsumidorEntregasDTO> lista = entregasMapper.toDTO(repo.entregasSelecionadasPorEntregador(dataEntrega, usuarioMapper.toEntity(entregador)));
		return lista;
	}

	/* Método responsável por devolver a lista de entregas inválidas */
	public List<ConsumidorEntregasDTO> listarEntregasInvalidas(String dataEntrega) {
		List<ConsumidorEntregasDTO> lista = entregasMapper.toDTO(repo.entregasInvalidas(dataEntrega));
		return lista;
	}
	
	/* Método responsável por realizar as atualizações de uma entrega */
	public ResponseEntity<String> atualizarEntregas(List<ConsumidorEntregasDTO> entregas, String email) {
		String result = "Entrega(s) atualizada(s) com sucesso!";
		Boolean msgEntregaAtribuida = false, msgEntregaInexistente = false;
		UsuarioDTO entregadorDTO = usuarioService.findByEmail(email);
		
		// verifica se o e-mail que está requisitando existe no sistema
		if (entregadorDTO == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Este e-mail não pertence a nenhum entregador cadastrado no sistema.");
		}
		
		for(ConsumidorEntregasDTO c :entregas) {
			Boolean selecionadoJson = c.getSelecionado();
			Boolean entregueJson = c.getEntregue();
			ConsumidorEntregasDTO entrega = findById(c.getId());			
			
			if(entrega != null) {	// caso a entrega exista no BD
				Boolean selecionado = entrega.getSelecionado();
				Boolean entregue = entrega.getEntregue();
				
				if(selecionadoJson != selecionado) {	//caso o campo "selecionado" tenha sido alterado
					entrega.setSelecionado(selecionadoJson);
					entrega.setEntregador_responsavel(!selecionadoJson ? null : entregadorDTO);
				} else {
					if(entrega.getSelecionado() == true && entrega.getEntregador_responsavel() != entregadorDTO) {
						msgEntregaAtribuida = true;
					}
				}
				
				if(entregueJson != entregue) {	//caso o campo "entregue" tenha sido alterado
					entrega.setEntregue(entregueJson);
				}
				
				alterar(entrega);
			} else {
				msgEntregaInexistente = true;
			}		
		}
		
		result = result + (msgEntregaAtribuida ? " Alguma(s) entrega(s) pode(m) não ter sido atribuída(s) a você "
				+ "pois alguém já se apropriou." : "");
		result = result + (msgEntregaInexistente ? " Alguma(s) entrega(s) selecionada(s) não existe(m) no sistema." : "");
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	/* Método responsável por salvar as entregas no BD */
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
		
		// se for a inserção do registro no BD, setar estes atributos como falso
		consumidorEntregas.setEntregue(false);
		consumidorEntregas.setSelecionado(false);

		repo.save(entregasMapper.toEntity(consumidorEntregas));
		return "Registro criado com sucesso!";
	}

	/* Método responsável por alterar as entregas no BD */
	public String alterar(ConsumidorEntregasDTO consumidorEntregas) {
		ConsumidorEntregasDTO cons = consumidorEntregas;
		BeanUtils.copyProperties(consumidorEntregas, cons, "id");	// copia as propriedades da entidade
		
		if(consumidorEntregas.getOpcao_entrega() == "Não") {
			deletar(consumidorEntregas);
		}
		repo.save(entregasMapper.toEntity(cons));

		return "Registro(s) atualizados(s) com sucesso!";
	}
	
	/* Método responsável por deletar uma entrega do BD */
	public void deletar(ConsumidorEntregasDTO consumidorEntregas) {
		repo.deleteById(consumidorEntregas.getId());
	}
	
	/* Método responsável por procurar uma entrega no BD, devolvendo ConsumidorEntregasDTO */
	public ConsumidorEntregasDTO findById(String id){
		if(repo.findById(id).isPresent()) {
			return entregasMapper.toDTO(repo.findById(id).get());
		} else {
			return null;
		}
	}
}
