package br.unisantos.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.unisantos.dto.ConsumidorEntregasDTO;
import br.unisantos.model.ConsumidorEntregas;

@Component
public class ConsumidorEntregasMapper {
	
	@Autowired
	UsuarioMapper usuarioMapper;

	public ConsumidorEntregas toEntity(ConsumidorEntregasDTO consumidorEntregasDTO) {
		ConsumidorEntregas consumidorEntregas = new ConsumidorEntregas();
		consumidorEntregas.setId(consumidorEntregasDTO.getId());
		consumidorEntregas.setId_consumidor(consumidorEntregasDTO.getId_consumidor());
		consumidorEntregas.setNome_consumidor(consumidorEntregasDTO.getNome_consumidor());
		consumidorEntregas.setComunidade_consumidor(consumidorEntregasDTO.getComunidade_consumidor());
		consumidorEntregas.setTelefone_consumidor(consumidorEntregasDTO.getTelefone_consumidor());
		consumidorEntregas.setEndereco_entrega(consumidorEntregasDTO.getEndereco_entrega());
		consumidorEntregas.setOpcao_entrega(consumidorEntregasDTO.getOpcao_entrega());
		consumidorEntregas.setValor_entrega(consumidorEntregasDTO.getValor_entrega());
		consumidorEntregas.setSelecionado(consumidorEntregasDTO.getSelecionado());
		consumidorEntregas.setEntregue(consumidorEntregasDTO.getEntregue());
		consumidorEntregas.setData_entrega(consumidorEntregasDTO.getData_entrega());
		consumidorEntregas.setEntregador_responsavel(consumidorEntregasDTO.getEntregador_responsavel() != null ?
				usuarioMapper.toEntity(consumidorEntregasDTO.getEntregador_responsavel()) : null);
		
		return consumidorEntregas;
	}
	
	public ConsumidorEntregasDTO toDTO(ConsumidorEntregas consumidorEntregas) {
		ConsumidorEntregasDTO consumidorEntregasDTO = new ConsumidorEntregasDTO();
		consumidorEntregasDTO.setId(consumidorEntregas.getId());
		consumidorEntregasDTO.setId_consumidor(consumidorEntregas.getId_consumidor());
		consumidorEntregasDTO.setNome_consumidor(consumidorEntregas.getNome_consumidor());
		consumidorEntregasDTO.setComunidade_consumidor(consumidorEntregas.getComunidade_consumidor());
		consumidorEntregasDTO.setTelefone_consumidor(consumidorEntregas.getTelefone_consumidor());
		consumidorEntregasDTO.setEndereco_entrega(consumidorEntregas.getEndereco_entrega());
		consumidorEntregasDTO.setOpcao_entrega(consumidorEntregas.getOpcao_entrega());
		consumidorEntregasDTO.setValor_entrega(consumidorEntregas.getValor_entrega());
		consumidorEntregasDTO.setSelecionado(consumidorEntregas.getSelecionado());
		consumidorEntregasDTO.setEntregue(consumidorEntregas.getEntregue());
		consumidorEntregasDTO.setData_entrega(consumidorEntregas.getData_entrega());
		consumidorEntregasDTO.setEntregador_responsavel(consumidorEntregas.getEntregador_responsavel() != null ?
				usuarioMapper.toDTO(consumidorEntregas.getEntregador_responsavel()) : null);
		
		return consumidorEntregasDTO;
	}
	
	public List<ConsumidorEntregas> toEntity(List<ConsumidorEntregasDTO> consumidoresEntregasDTO) {
		List<ConsumidorEntregas> entregas = new ArrayList<>();
		for (ConsumidorEntregasDTO e : consumidoresEntregasDTO) {
			entregas.add(toEntity(e));
		}
		
		return entregas;
	}
	
	public List<ConsumidorEntregasDTO> toDTO(List<ConsumidorEntregas> consumidoresEntregas) {
		List<ConsumidorEntregasDTO> entregasDTO = new ArrayList<>();
		for (ConsumidorEntregas e : consumidoresEntregas) {
			entregasDTO.add(toDTO(e));
		}
		
		return entregasDTO;
	}
}
