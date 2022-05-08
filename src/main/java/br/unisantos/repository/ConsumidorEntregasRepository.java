package br.unisantos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.unisantos.model.ConsumidorEntregas;

@Repository
public interface ConsumidorEntregasRepository extends JpaRepository<ConsumidorEntregas, Long> {
	
	List<ConsumidorEntregas> findBySelecionadoFalse();
	
	@Query("SELECT * FROM tbConsumidorEntregas WHERE selecionado = true AND"
			+ "entregadorResponsavel = :resp AND dataEntrega = :dataEntrega")
    public List<ConsumidorEntregas> findBySelecionadoAndEntregadorResponsavel(@Param("resp") String resp,
    	@Param("dataEntrega") String dataEntrega);
	
	@Query("SELECT * FROM tbConsumidorEntregas WHERE opcao_entrega = 'Sim' AND"
			+ "(endereco_entrega = '' OR endereco_entrega = null) AND dataEntrega = :dataEntrega")
    public List<ConsumidorEntregas> findByEntregaAndEnderecoEmpty(@Param("dataEntrega") String dataEntrega);
	
    public List<ConsumidorEntregas> findByIdEntregaLike(String idEntrega);
    
    @Query("SELECT * FROM tbConsumidorEntregas WHERE dataEntrega = :dataEntrega LIMIT 1")
    public List<ConsumidorEntregas> findByDataEntregaLimit(@Param("dataEntrega") String dataEntrega);
}
