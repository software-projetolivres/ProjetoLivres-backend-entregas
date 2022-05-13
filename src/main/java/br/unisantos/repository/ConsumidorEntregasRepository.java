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
			+ " AND c.endereco_entrega != '' AND c.opcao_entrega = 'Sim'")
	List<ConsumidorEntregas> findByEntregaValidaNaoSelec(@Param("data_entrega") String data_entrega);
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.selecionado = true AND c.data_entrega = :data_entrega"
			+ " AND entregador_responsavel = :entregador_responsavel")
	/*@Query("SELECT id, c.comunidade_consumidor, c.endereco_entrega, c.entregue, c.nome_consumidor, c.selecionado,"
			+ " c.telefone_consumidor, c.valor_entrega FROM ConsumidorEntregas c WHERE c.selecionado = true"
			+ " AND c.data_entrega = :data_entrega AND entregador_responsavel = :entregador_responsavel")*/
	List<ConsumidorEntregas> findSelecionadosEntregadorResp(@Param("data_entrega") String data_entrega,
    	@Param("entregador_responsavel") Usuario entregador_responsavel);
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.opcao_entrega = 'Sim' AND"
			+ " (c.endereco_entrega = '' OR c.endereco_entrega = null) AND c.data_entrega = :data_entrega")
    List<ConsumidorEntregas> findByEntregaAndEnderecoEmpty(@Param("data_entrega") String data_entrega);
	
	Optional<ConsumidorEntregas> findById(String id);
	
	@Query("DELETE FROM ConsumidorEntregas c WHERE c.id = :id")
	void deletar(String id);
}
