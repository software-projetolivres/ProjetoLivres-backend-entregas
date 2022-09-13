package br.unisantos.dto;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumidorEntregasDTO {

	private String id;
	private Long id_consumidor;
	private String nome_consumidor;
	private Integer comunidade_consumidor;
	private String telefone_consumidor;
	private String endereco_entrega;
	private String opcao_entrega;
	private Double valor_entrega;
	private Boolean selecionado;
	private Boolean entregue;
	private String data_entrega;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter(onMethod = @__(@JsonIgnore))
	private UsuarioDTO entregador_responsavel;
}