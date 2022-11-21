import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
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
		UsuarioDTO entregador = getUsuarioDTO();
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
		ConsumidorEntregas consumidorEntregas01 = getConsumidorEntregas();
		ConsumidorEntregasDTO consumidorEntregasDTO1 = getConsumidorEntregasDTO();
		consumidorEntregasDTO1.setSelecionado(true);
		Optional<ConsumidorEntregas> optConsEntregas = Optional.of(consumidorEntregas01);
		
		ArrayList<ConsumidorEntregasDTO> consumidorEntregasDTOList = new ArrayList<ConsumidorEntregasDTO>();
		consumidorEntregasDTOList.add(consumidorEntregasDTO1);
		
		// When
		Mockito.when(repo.findById("1")).thenReturn(optConsEntregas);
		Mockito.when(consumidorEntregasService.findById("1")).thenReturn(consumidorEntregasDTO1);
		Mockito.when(usuarioService.findByEmail("teste@emailteste.com")).thenReturn(entregador);
		
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
