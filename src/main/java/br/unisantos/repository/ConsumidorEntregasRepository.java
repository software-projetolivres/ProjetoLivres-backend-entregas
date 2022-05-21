package br.unisantos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.unisantos.model.ConsumidorEntregas;
import br.unisantos.model.Usuario;

@Repository
public interface ConsumidorEntregasRepository extends JpaRepository<ConsumidorEntregas, Long> {
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.selecionado = false AND c.data_entrega = :data_entrega"
			+ " AND c.endereco_entrega != '' AND c.opcao_entrega = 'Sim' ORDER BY c.valor_entrega ASC")
	List<ConsumidorEntregas> findByEntregaValidaNaoSelec(@Param("data_entrega") String data_entrega);
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.selecionado = true AND c.data_entrega = :data_entrega"
			+ " AND c.entregador_responsavel = :entregador_responsavel AND c.entregue = false ORDER BY c.valor_entrega ASC")
	List<ConsumidorEntregas> findSelecionadosEntregadorResp(@Param("data_entrega") String data_entrega,
    	@Param("entregador_responsavel") Usuario entregador_responsavel);
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.opcao_entrega = 'Sim' AND"
			+ " (c.endereco_entrega = '' OR c.endereco_entrega = null) AND c.data_entrega = :data_entrega")
    List<ConsumidorEntregas> findByEntregaAndEnderecoEmpty(@Param("data_entrega") String data_entrega);
	
	Optional<ConsumidorEntregas> findById(String id);
	
	@Query("DELETE FROM ConsumidorEntregas c WHERE c.id = :id")
	void deletar(String id);
}
