package br.unisantos.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Token {
	
	@SequenceGenerator(name = "sequencia_token", sequenceName = "sequencia_token", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequencia_token")
	@Id
	private Long id;
	
	@Column(nullable = false)
	private String token;
	
	@Column(nullable = false)
	private LocalDateTime data_criacao;
	
	@Column(nullable = false)
	private LocalDateTime data_expiracao;
	
	@Column(nullable = true)
	private LocalDateTime data_confirmacao;
	
	@Autowired
	@ManyToOne	//um usuário pode ter solicitado vários tokens
	@JoinColumn(nullable = false)
	private Usuario usuario;
	
	public Token() {
		
	}
	
	public Token(String token, LocalDateTime data_criacao, LocalDateTime data_expiracao,
			Usuario usuario) {
		this.token = token;
		this.data_criacao = data_criacao;
		this.data_expiracao = data_expiracao;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getData_criacao() {
		return data_criacao;
	}
	public void setData_criacao(LocalDateTime data_criacao) {
		this.data_criacao = data_criacao;
	}
	public LocalDateTime getData_expiracao() {
		return data_expiracao;
	}
	public void setData_expiracao(LocalDateTime data_expiracao) {
		this.data_expiracao = data_expiracao;
	}
	public LocalDateTime getData_confirmacao() {
		return data_confirmacao;
	}
	public void setData_confirmacao(LocalDateTime data_confirmacao) {
		this.data_confirmacao = data_confirmacao;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}