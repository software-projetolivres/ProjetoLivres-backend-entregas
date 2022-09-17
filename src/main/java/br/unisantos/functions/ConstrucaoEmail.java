package br.unisantos.functions;

public class ConstrucaoEmail {
	// E-mail que é enviado ao(s) gestor(es) quando uma conta está com a ativação pendente.
	public static String emailConfirmacaoPendente(String nome, String sobrenome, String link) {
		return "Olá!<br>"
		+ "<p>O(a) entregador(a) " + nome + " " + sobrenome + " realizou o cadastro e está aguardando sua conta ser verificada.<br>"
		+ "Por favor, para realizar a verificação clique no link abaixo em <b>até 2 horas</b>: </p>"
		+ "<blockquote><p><a href=\"" + link + "\">Ativar conta</a></p></blockquote>"
		+ "O entregador será notificado quando a conta for ativada.";
	}
	
	// E-mail que é enviado ao usuário cadastrado que a ativação da conta foi realizada com sucesso.
	public static String emailConfirmacaoConcluida(String nome) {
		return "<p>Caro(a) " + nome + ",</p>"
		+ "<p>Sua conta foi ativada com sucesso!</p>";
	}
	
	// E-mail que é enviado ao usuário cadastrado informando que a ativação da conta depende deve ser feita pelo gestor.
	public static String emailAguardeConfirmacao(String nome) {
		return "<p>Caro(a) " + nome + ",</p>"
		+ "<p>Um e-mail de confirmação do seu cadastro foi enviado aos responsáveis!<br>"
		+ " Lembrando que a confirmação deve ser feita em até <b>2 horas</b>. Por favor, aguarde!</p>";
	}
}
