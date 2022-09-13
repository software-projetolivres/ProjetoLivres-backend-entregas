package br.unisantos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_consumidor_entregas")
@Getter
@Setter
@EqualsAndHashCode
public class ConsumidorEntregas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;	//(dataEntrega + "c" + id_consumidor);
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
	private Usuario entregador_responsavel;
}