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
	
	/* Devolve uma lista de ConsumidorEntregas de acordo com a data de entrega passada;
	 * não podem estar selecionadas, além do critério de entrega válida: precisam estar com o 
	 * endereço preenchido a opção de entrega precisa estar marcada como 'Sim'. */
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.selecionado = false AND c.data_entrega = :data_entrega"
			+ " AND c.endereco_entrega != '' AND c.opcao_entrega = 'Sim' ORDER BY c.valor_entrega ASC")
	List<ConsumidorEntregas> entregasValidasNaoSelecionadas(@Param("data_entrega") String data_entrega);
		
	/* Devolve uma lista de ConsumidorEntregas de acordo com a data de entrega passada;
	 * precisam estar selecionadas, não podem estar entregues e precisam ser do entregador
	 * responsável conforme passado. */
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.selecionado = true AND c.data_entrega = :data_entrega"
			+ " AND c.entregador_responsavel = :entregador_responsavel AND c.entregue = false ORDER BY c.valor_entrega ASC")
	List<ConsumidorEntregas> entregasSelecionadasPorEntregador(@Param("data_entrega") String data_entrega,
    	@Param("entregador_responsavel") Usuario entregador_responsavel);
	
	/* Devolve uma lista de ConsumidorEntregas de acordo com a data passada e possuírem o
	 * critério de entrega inválida: precisam estar SEM endereço e a opção de entrega
	 * precisa estar marcada como 'Sim'. */
	@Query("SELECT c FROM ConsumidorEntregas c WHERE c.opcao_entrega = 'Sim' AND"
			+ " (c.endereco_entrega = '' OR c.endereco_entrega = null) AND c.data_entrega = :data_entrega")
    List<ConsumidorEntregas> entregasInvalidas(@Param("data_entrega") String data_entrega);
	
	// Devolve, se houver, um registro de ConsumidorEntregas de acordo com o id passado.
	Optional<ConsumidorEntregas> findById(String id); //precisa ser declarado pois sobrescrevo o tipo do atributo do parâmetro
	
	// Deleta um registro de ConsumidorEntregas de acordo com o id passado.
	void deleteById(String id);	//precisa ser  declarado pois sobrescrevo o tipo do atributo do parâmetro
}
