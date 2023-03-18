package br.com.cedi.util.utilitarios;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriptografiaSenha {

	public static String criptografar(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
}
