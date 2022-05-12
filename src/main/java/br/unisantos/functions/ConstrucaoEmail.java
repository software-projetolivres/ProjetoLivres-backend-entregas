package br.unisantos.functions;

public class ConstrucaoEmail {
	public static String emailConfirmacaoPendente(String nome, String sobrenome, String link) {
		return "<div style=\"font-family:Arial;font-size:18px;margin:0;color:#0b0c0c\"> "
		+ "<p>Olá!</p>"
		+ "<p>O(a) entregador(a) " + nome + " " + sobrenome + " realizou o cadastro e está aguardando sua conta ser verificada.<br>"
		+ "Por favor, para realizar a verificação clique no link abaixo em <b>até 2 horas</b>: </p>"
		+ "<blockquote><p><a href=\"" + link + "\">Ativar conta</a></p></blockquote>"
		+ "O entregador será notificado quando a conta for ativada.</div>";
	}
	
	public static String emailConfirmacaoConcluida(String nome) {
		return "<div style=\"font-family:Arial;font-size:18px;margin:0;color:#0b0c0c\"> "
		+ "<p>Caro(a) " + nome + ",</p>"
		+ "<p>Sua conta foi ativada com sucesso!</p></div>";
	}
	
	public static String emailAguardeConfirmacao(String nome) {
		return "<div style=\"font-family:Arial;font-size:18px;margin:0;color:#0b0c0c\"> "
		+ "<p>Caro(a) " + nome + ",</p>"
		+ "<p>Um e-mail de confirmação do seu cadastro foi enviado aos responsáveis!<br>"
		+ " Lembrando que a confirmação deve ser feita em até <b>2 horas</b>. Por favor, aguarde!</p></div>";
	}
}
