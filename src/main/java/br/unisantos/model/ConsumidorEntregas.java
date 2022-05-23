package br.unisantos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_consumidor_entregas")
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
	private Usuario entregador_responsavel;
	
	public Long getId_consumidor() {
		return id_consumidor;
	}
	public void setId_consumidor(Long id_consumidor) {
		this.id_consumidor = id_consumidor;
	}
	public String getNome_consumidor() {
		return nome_consumidor;
	}
	public void setNome_consumidor(String nome_consumidor) {
		this.nome_consumidor = nome_consumidor;
	}
	public Integer getComunidade_consumidor() {
		return comunidade_consumidor;
	}
	public void setComunidade_consumidor(Integer comunidade_consumidor) {
		this.comunidade_consumidor = comunidade_consumidor;
	}
	public String getTelefone_consumidor() {
		return telefone_consumidor;
	}
	public void setTelefone_consumidor(String telefone_consumidor) {
		this.telefone_consumidor = telefone_consumidor;
	}
	public String getEndereco_entrega() {
		return endereco_entrega;
	}
	public void setEndereco_entrega(String endereco_entrega) {
		this.endereco_entrega = endereco_entrega;
	}
	public String getOpcao_entrega() {
		return opcao_entrega;
	}
	public void setOpcao_entrega(String opcao_entrega) {
		this.opcao_entrega = opcao_entrega;
	}
	public Double getValor_entrega() {
		return valor_entrega;
	}
	public void setValor_entrega(Double valor_entrega) {
		this.valor_entrega = valor_entrega;
	}
	public Boolean getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	@JsonIgnore
	public Usuario getEntregador_responsavel() {
		return entregador_responsavel;
	}
	public void setEntregador_responsavel(Usuario entregadorResponsavel) {
		this.entregador_responsavel = entregadorResponsavel;
	}
	public Boolean getEntregue() {
		return entregue;
	}
	public void setEntregue(Boolean entregue) {
		this.entregue = entregue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData_entrega() {
		return data_entrega;
	}
	public void setData_entrega(String data_entrega) {
		this.data_entrega = data_entrega;
	}
}
