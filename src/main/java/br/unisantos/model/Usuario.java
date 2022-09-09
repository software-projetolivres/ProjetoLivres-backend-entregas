package br.unisantos.model;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	public Usuario() {
			
	}


	@SequenceGenerator(name = "sequencia_usuario", sequenceName = "sequencia_usuario", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequencia_usuario")
	@Id
	private Long id;
	
	@NonNull
	private String nome;
	
	@NonNull
	private String sobrenome;
	
	@NonNull
	private String email;
	
	@NonNull
	private String senha;
	
	@Enumerated(EnumType.STRING)
	@NonNull
	private UsuarioTipoPerfil usuarioTipoPerfil;
	private Boolean bloqueado = false;
	private Boolean ativo = false;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuarioTipoPerfil.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !bloqueado;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return ativo;
	}
	
}
