package br.unisantos.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.unisantos.email.EnvioEmail;

@Service
public class EmailService implements EnvioEmail {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	@Async //para não bloquear o cliente 
	public void enviar(String destinatarios, String email, String assunto) {
		try {
			MimeMessage mimeMsg = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMsg, "utf-8");
			System.out.println(destinatarios);
			mimeHelper.setTo(separaDestinatarios(destinatarios));
			mimeHelper.setFrom("livresprojetosoftware@gmail.com");
			mimeHelper.setSubject(assunto);
			mimeHelper.setText(email, true);
			javaMailSender.send(mimeMsg);
		} catch(MessagingException e) {
			throw new IllegalStateException("Não foi possível enviar o e-mail!");
		}
		
	}
	
	private String[] separaDestinatarios(String destinatarios) {
		String result[];
		if(destinatarios.contains(",")) {
			result = destinatarios.split(",");
			System.out.println("result => " + result);
			return result;
		} else {
			String destin[] = {destinatarios};
			System.out.println("destin => " + destin);
			return destin;
		}		
	}

}
