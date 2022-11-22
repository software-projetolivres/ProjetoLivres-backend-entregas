package br.unisantos.email;

import br.unisantos.dto.EmailDTO;

public interface EnvioEmail {
	void enviar(EmailDTO email);
}
