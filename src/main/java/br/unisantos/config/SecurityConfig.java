package br.unisantos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.unisantos.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private  UsuarioService usuarioService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
		
	/* Método responsável por definir: as páginas que poderão ser acessadas sem autenticação,
	 * páginas de redirecionamento após login */
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().authorizeRequests()
			.antMatchers("/api/cadastroUsuario/**").permitAll()
			.anyRequest().authenticated().and().formLogin()
			.defaultSuccessUrl(System.getenv("link_site") + "entregas", true)
			.failureUrl(System.getenv("link_site") + "login").and().httpBasic();
		http.logout().logoutSuccessUrl(System.getenv("link_site"));
		http.cors();
	}
	
	
	// Método responsável por definir como será o processamento dos dados de login
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	
	// Método responsável por devolver detalhes do usuário
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(usuarioService);
		return provider;
	}
}
