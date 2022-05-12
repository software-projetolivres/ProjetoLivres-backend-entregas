package br.unisantos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.unisantos.model.ConsumidorEntregas;

@Repository
public interface ConsumidorEntregasRepository extends JpaRepository<ConsumidorEntregas, Long> {
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.selecionado = false AND c.data_entrega = :data_entrega"
			+ " AND c.endereco_entrega != '' AND c.opcao_entrega = 'Sim'")
	List<ConsumidorEntregas> findByEntregaValidaNaoSelec(@Param("data_entrega") String data_entrega);
	
	/*@Query("SELECT * FROM tb_consumidor_entregas WHERE selecionado = true AND"
			+ "entregador_responsavel = :resp AND data_entrega = :data_entrega")
    List<ConsumidorEntregas> findBySelecionadoAndEntregadorResponsavel(@Param("resp") String resp,
    	@Param("data_entrega") String data_entrega);*/
	
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.opcao_entrega = 'Sim' AND"
			+ " (c.endereco_entrega = '' OR c.endereco_entrega = null) AND c.data_entrega = :data_entrega")
    List<ConsumidorEntregas> findByEntregaAndEnderecoEmpty(@Param("data_entrega") String data_entrega);
	
	/*@Query("SELECT c FROM ConsumidorEntregas c WHERE c.id_entrega = :id_entrega")
    ConsumidorEntregas findByIdEntrega(@Param("id_entrega") String id_entrega);*/
	
	Optional<ConsumidorEntregas> findById(String id);
    
    /*@Query("SELECT * FROM tb_consumidor_entregas WHERE data_entrega = :dataEntrega LIMIT 1")
    List<ConsumidorEntregas> findByDataEntregaLimit(@Param("dataEntrega") String data_entrega);*/
}
