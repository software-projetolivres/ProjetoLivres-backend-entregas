package br.unisantos.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EmailDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NonNull
	private String to;
	
	@NonNull
	private String text;
	
	@NonNull
	private String subject;
}
