package br.unisantos.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidaEmail {
	
	public static Boolean isEmailValido(String email) {
		//permite careacteres: a-z, A-Z, 0-9, _, . e - e precisa ter o @
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(email);
		if(matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
}
