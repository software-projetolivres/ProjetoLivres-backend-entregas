package br.unisantos.service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.unisantos.dto.EmailDTO;
import br.unisantos.email.EnvioEmail;

@Service
public class EmailService implements EnvioEmail {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	/* Método responsável por realizar o envio do e-mail */
	@Override
	@Async //para não bloquear o cliente 
	public void enviar(EmailDTO email) {
		try {
			MimeMessage mimeMsg = prepareHtmlEmailMessage(email);
			javaMailSender.send(mimeMsg);
		} catch(MessagingException e) {
			throw new IllegalStateException("Não foi possível enviar o e-mail! Exception: " + e);
		}
		
	}
	
	/* Método responsável por pegar o html do template de e-mail devolvendo-o */
	private String htmlFromTemplate(EmailDTO email) {
		Context context = new Context();
		context.setVariable("email", email);
		return templateEngine.process("email/email", context);
	}
	
	/* Método responsável por devolver a MimeMessage preenchida de acordo com as informações
	 * importantes para a realização do envio do e-mail */
	private MimeMessage prepareHtmlEmailMessage(EmailDTO email) throws MessagingException {
		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMsg, "utf-8");
		mimeHelper.setTo(InternetAddress.parse(email.getTo()));
		mimeHelper.setFrom("livresprojetosoftware@gmail.com");
		mimeHelper.setSubject(email.getSubject());
		mimeHelper.setText(htmlFromTemplate(email), true);
		
		return mimeMsg;
	}
}
