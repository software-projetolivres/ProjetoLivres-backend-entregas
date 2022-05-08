package br.unisantos.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbConsumidorEntregas")
public class ConsumidorEntregas {
	
	@Id
	private String idEntrega;
	private Long id_consumidor;
	private String nome_consumidor;
	private Integer comunidade_consumidor;
	private Integer telefone_consumidor;
	private String endereco_entrega;
	private String opcao_entrega;
	private Double valor_entrega;
	private Boolean selecionado;
	private String entregadorResponsavel;
	private Boolean entregue;
	private String dataEntrega;
	
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
	public Integer getTelefone_consumidor() {
		return telefone_consumidor;
	}
	public void setTelefone_consumidor(Integer telefone_consumidor) {
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
	public String getEntregadorResponsavel() {
		return entregadorResponsavel;
	}
	public void setEntregadorResponsavel(String entregadorResponsavel) {
		this.entregadorResponsavel = entregadorResponsavel;
	}
	public Boolean getEntregue() {
		return entregue;
	}
	public void setEntregue(Boolean entregue) {
		this.entregue = entregue;
	}
	public String getIdEntrega() {
		return idEntrega;
	}
	public void setIdEntrega(String idEntrega) {
		this.idEntrega = idEntrega;
	}
	public String getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
}
