package br.unisantos.email;

public interface EnvioEmail {
	void enviar(String destinatario, String email, String assunto);
}
