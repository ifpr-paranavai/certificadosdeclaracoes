package br.com.cedi.util.utilitarios;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GeradorCodigosProvisorios {
	public static String gerarCodigo() {
		Random random = new Random();
		String resultado = "";
		String valores[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
		String d = format.format(new Date());
		for (int i = 0; i <= 14; i++) {
			int a = random.nextInt(valores.length);
			if(i<14){
				resultado+=d.charAt(i);
			}
			resultado += valores[a];
		}
		
		return resultado;
	}

}
