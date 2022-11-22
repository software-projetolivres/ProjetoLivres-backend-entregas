import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import br.unisantos.dto.ConsumidorEntregasDTO;
import br.unisantos.dto.UsuarioDTO;
import br.unisantos.mapper.ConsumidorEntregasMapper;
import br.unisantos.mapper.UsuarioMapper;
import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.model.Usuario;
import br.unisantos.model.UsuarioTipoPerfil;
import br.unisantos.repository.ConsumidorEntregasRepository;
import br.unisantos.service.ConsumidorEntregasService;
import br.unisantos.service.DirectionsGoogleApiService;
import br.unisantos.service.UsuarioService;

public class ConsumidorEntregasServiceTest {
	
	@InjectMocks
	private ConsumidorEntregasService consumidorEntregasService;
	
	@Mock
	private ConsumidorEntregasRepository repo;
	
	@Mock
	private UsuarioService usuarioService;
	
	@Mock
	private DirectionsGoogleApiService directionsAPIService;
	
	@Mock
	private ConsumidorEntregasMapper entregasMapper;
	
	@Mock
	private UsuarioMapper usuarioMapper;
	
	@Mock
	private ConsumidorEntregasDTO consDTO;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testRoteirizarEntregas() throws ApiException, InterruptedException, IOException {
		ConsumidorEntregas consumidorEntregas01 = getConsumidorEntregas();
		Optional<ConsumidorEntregas> optConsEntregas = Optional.of(consumidorEntregas01);
		consumidorEntregas01.setEntregador_responsavel(null);
		ConsumidorEntregasDTO consumidorEntregasDTO1 = getConsumidorEntregasDTO();
		consumidorEntregasDTO1.setEntregador_responsavel(null);
		
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		consumidorEntregasDTOList.add(consumidorEntregasDTO1);
		
		//When
		Mockito.when(repo.findById(consumidorEntregasDTO1.getId())).thenReturn(optConsEntregas);
		Mockito.when(entregasMapper.toDTO(repo.findById(consumidorEntregasDTO1.getId()).get())).thenReturn(consumidorEntregasDTO1);
		
		// Then
		DirectionsResult result = consumidorEntregasService.roteirizarEntregas(consumidorEntregasDTOList);

		Assert.assertEquals(result, result);
	}
	
	@Test
	void testMontaListaConsumidorEntregas() throws JsonMappingException, JsonProcessingException, JSONException {
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		ConsumidorEntregasDTO cons = new ConsumidorEntregasDTO();
		cons.setId_consumidor((long) 44);
		cons.setNome_consumidor("teste");
		cons.setComunidade_consumidor(4);
		cons.setTelefone_consumidor("13999999999");
		cons.setEndereco_entrega("teste");
		cons.setOpcao_entrega("Sim");
		cons.setValor_entrega(7.0);
		cons.setSelecionado(false);
		cons.setEntregue(false);
		cons.setData_entrega("2022-11-22");
		consumidorEntregasDTOList.add(cons);
		
		String jsonEntregas = "{\"data\": [ { \"id_consumidor\": 44, \"nome_consumidor\": \"teste\", \"comunidade_consumidor\": 4, \"telefone_consumidor\": \"13999999999\", "
				+ "\"endereco_entrega\": \"teste\", \"opcao_entrega\": \"Sim\", \"valor_entrega\": 7 } ]}";
		
		//When
		Mockito.when(consumidorEntregasService.listarNaoSelecionados("2022-11-22")).thenReturn(consumidorEntregasDTOList);
		
		// Then
		List<ConsumidorEntregasDTO> resp = consumidorEntregasService.montaListaConsumidorEntregas(jsonEntregas, "2022-11-22");

		Assert.assertEquals(consumidorEntregasDTOList, resp);
	}
	
	@Test
	void testListarNaoSelecionados() {
		ConsumidorEntregas consumidorEntregas01 = getConsumidorEntregas();
		consumidorEntregas01.setEntregador_responsavel(null);
		ConsumidorEntregasDTO consumidorEntregasDTO1 = getConsumidorEntregasDTO();
		consumidorEntregasDTO1.setEntregador_responsavel(null);
		
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		consumidorEntregasDTOList.add(consumidorEntregasDTO1);
		
		// When
		Mockito.when(entregasMapper.toDTO(repo.entregasValidasNaoSelecionadas("2022-11-22"))).thenReturn(consumidorEntregasDTOList);
		
		// Then
		List<ConsumidorEntregasDTO> list = consumidorEntregasService.listarNaoSelecionados("2022-11-22");

		Assert.assertEquals(consumidorEntregasDTOList, list);
	}
	
	@Test
	void testListarSelecionadosResponsavel() {
		UsuarioDTO entregador = getUsuarioDTO();
		ConsumidorEntregas consumidorEntregas01 = getConsumidorEntregas();
		consumidorEntregas01.setSelecionado(true);
		ConsumidorEntregasDTO consumidorEntregasDTO1 = getConsumidorEntregasDTO();
		consumidorEntregasDTO1.setSelecionado(true);
		
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		consumidorEntregasDTOList.add(consumidorEntregasDTO1);
		
		// When
		Mockito.when(usuarioService.findByEmail(entregador.getEmail())).thenReturn(entregador);
		Mockito.when(entregasMapper.toDTO(repo.entregasSelecionadasPorEntregador("2022-11-22", usuarioMapper.toEntity(entregador)))).thenReturn(consumidorEntregasDTOList);
		
		// Then
		List<ConsumidorEntregasDTO> list = consumidorEntregasService.listarSelecionadosResponsavel("2022-11-22", entregador.getEmail());

		Assert.assertEquals(consumidorEntregasDTOList, list);
	}
	
	@Test
	void testListarEntregasInvalidas() {
		ConsumidorEntregas consumidorEntregas01 = getConsumidorEntregas();
		consumidorEntregas01.setEndereco_entrega(null);
		ConsumidorEntregasDTO consumidorEntregasDTO1 = getConsumidorEntregasDTO();
		consumidorEntregasDTO1.setEndereco_entrega(null);
		
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		consumidorEntregasDTOList.add(consumidorEntregasDTO1);
		
		// When
		Mockito.when(entregasMapper.toDTO(repo.entregasInvalidas("2022-11-22"))).thenReturn(consumidorEntregasDTOList);
		
		// Then
		List<ConsumidorEntregasDTO> list = consumidorEntregasService.listarEntregasInvalidas("2022-11-22");

		Assert.assertEquals(consumidorEntregasDTOList, list);
	}
	
	@Test
	void testAtualizarEntregas() {
		UsuarioDTO entregador = getUsuarioDTO();
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		ConsumidorEntregas consumidorEntregas01 = getConsumidorEntregas();
		Optional<ConsumidorEntregas> optConsEntregas = Optional.of(consumidorEntregas01);
		
		//Cenário de Seleção de Entregas
		ConsumidorEntregasDTO consumidorEntregasDTO1 = getConsumidorEntregasDTO();
		consumidorEntregasDTO1.setSelecionado(true);
		consumidorEntregasDTO1.setEntregador_responsavel(entregador);
		consumidorEntregasDTOList.add(consumidorEntregasDTO1);
		
		// When
		Mockito.when(usuarioService.findByEmail("teste@emailteste.com")).thenReturn(entregador);
		Mockito.when(repo.findById(consumidorEntregasDTO1.getId())).thenReturn(optConsEntregas);
		Mockito.when(entregasMapper.toDTO(repo.findById(consumidorEntregasDTO1.getId()).get())).thenReturn(consumidorEntregasDTO1);
		
		// Then
		ResponseEntity<String> resposta = consumidorEntregasService.atualizarEntregas(consumidorEntregasDTOList, "teste@emailteste.com");

		Assert.assertEquals(200, resposta.getStatusCodeValue());
	}
	
	
	@Test
	void testSalvarAlterar() {
		ConsumidorEntregas consumidorEntregas = getConsumidorEntregas();
		Optional<ConsumidorEntregas> optConsEntregas = Optional.of(consumidorEntregas);
		ConsumidorEntregasDTO consumidorEntregasDTO = getConsumidorEntregasDTO();

		// When
		Mockito.when(repo.findById(consumidorEntregas.getId())).thenReturn(optConsEntregas);
		Mockito.when(entregasMapper.toDTO(consumidorEntregas)).thenReturn(consumidorEntregasDTO);

		// Then
		String retorno = consumidorEntregasService.salvar(consumidorEntregasDTO);

		Assert.assertEquals("Registro(s) atualizados(s) com sucesso!", retorno);
	}
	
	@Test
	void testDeletar() {
		ConsumidorEntregasDTO consumidorEntregasDTO = getConsumidorEntregasDTO();

		// When
		Mockito.doNothing().when(repo).deleteById(consumidorEntregasDTO.getId());

		// Then
		consumidorEntregasService.deletar(consumidorEntregasDTO);
	}
	
	@Test
	void testFindById() {
		ConsumidorEntregas consumidorEntregas = getConsumidorEntregas();
		Optional<ConsumidorEntregas> optConsEntregas = Optional.of(consumidorEntregas);
		ConsumidorEntregasDTO consumidorEntregasDTO = getConsumidorEntregasDTO();

		// When
		Mockito.when(repo.findById("1")).thenReturn(optConsEntregas);
		Mockito.when(entregasMapper.toDTO(repo.findById("1").get())).thenReturn(consumidorEntregasDTO);

		// Then
		ConsumidorEntregasDTO dto = consumidorEntregasService.findById("1");

		Assert.assertEquals(consumidorEntregasDTO, dto);
	}
	
	private ConsumidorEntregasDTO getConsumidorEntregasDTO() {
		ConsumidorEntregasDTO consumidorEntregasDTO = new ConsumidorEntregasDTO();
		long idConsum = 1234;
		consumidorEntregasDTO.setId_consumidor(idConsum);
		consumidorEntregasDTO.setNome_consumidor("Teste");
		consumidorEntregasDTO.setComunidade_consumidor(411);
		consumidorEntregasDTO.setTelefone_consumidor("13999999999");
		consumidorEntregasDTO.setEndereco_entrega("Endereço Teste");
		consumidorEntregasDTO.setOpcao_entrega("Sim");
		consumidorEntregasDTO.setValor_entrega(7.5);
		consumidorEntregasDTO.setSelecionado(false);
		consumidorEntregasDTO.setEntregue(false);
		consumidorEntregasDTO.setData_entrega("2022-11-22");
		consumidorEntregasDTO.setEntregador_responsavel(getUsuarioDTO());
		return consumidorEntregasDTO;
	}
	
	private ConsumidorEntregas getConsumidorEntregas() {
		ConsumidorEntregas consumidorEntregas = new ConsumidorEntregas();
		long idConsum = 1234;
		consumidorEntregas.setId_consumidor(idConsum);
		consumidorEntregas.setNome_consumidor("Teste");
		consumidorEntregas.setComunidade_consumidor(411);
		consumidorEntregas.setTelefone_consumidor("13999999999");
		consumidorEntregas.setEndereco_entrega("Endereço Teste");
		consumidorEntregas.setOpcao_entrega("Sim");
		consumidorEntregas.setValor_entrega(7.5);
		consumidorEntregas.setSelecionado(false);
		consumidorEntregas.setEntregue(false);
		consumidorEntregas.setData_entrega("2022-11-22");
		consumidorEntregas.setEntregador_responsavel(getUsuario());
		return consumidorEntregas;
	}
	
	private UsuarioDTO getUsuarioDTO() {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNome("Teste");
		usuarioDTO.setSobrenome("Teste");
		usuarioDTO.setEmail("teste@emailteste.com");
		usuarioDTO.setSenha("123456");
		usuarioDTO.setUsuarioTipoPerfil(UsuarioTipoPerfil.USUARIO);
		usuarioDTO.setBloqueado(false);
		usuarioDTO.setAtivo(true);
		return usuarioDTO;
	}
	
	private Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("Teste");
		usuario.setSobrenome("Teste");
		usuario.setEmail("teste@emailteste.com");
		usuario.setSenha("123456");
		usuario.setUsuarioTipoPerfil(UsuarioTipoPerfil.USUARIO);
		usuario.setBloqueado(false);
		usuario.setAtivo(true);
		return usuario;
	}
}
