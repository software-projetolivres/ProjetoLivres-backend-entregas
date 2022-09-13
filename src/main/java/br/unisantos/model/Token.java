package br.unisantos.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_token")
@Getter
@Setter
@RequiredArgsConstructor
public class Token implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "sequencia_token", sequenceName = "sequencia_token", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequencia_token")
	@Id
	private Long id;
	
	@Column(nullable = false)
	@NonNull
	private String token;
	
	@Column(nullable = false)
	@NonNull
	private LocalDateTime data_criacao;
	
	@Column(nullable = false)
	@NonNull
	private LocalDateTime data_expiracao;
	
	@Column(nullable = true)
	private LocalDateTime data_confirmacao;
	
	@Autowired
	@ManyToOne(fetch = FetchType.LAZY)	//um usuário pode ter solicitado vários tokens
	@JoinColumn(nullable = false)
	@NonNull
	private Usuario usuario;
	
	public Token() {
			
	}
	
}