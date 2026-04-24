//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 01
//Questão 03 - IS

import java.util.*;

class Is {

	// Final da entrada
	public static boolean isFim(String s) {
		return (s.length() == 3 &&
				s.charAt(0) == 'F' &&
				s.charAt(1) == 'I' &&
				s.charAt(2) == 'M');
	}

	// Método para verificar vogais
	public static boolean isVogal(String s) {
		boolean resp = true;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u' &&
					c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U') {
				resp = false;
					}
		}

		return resp;
	}

	// Método para verificar se é consoante
	public static boolean isConsoante(String s) {
		boolean resp = true;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') ||
					c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
					c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
				resp = false;
					}
		}

		return resp;
	}

	// Método para verificar se é inteiro válido
	public static boolean isInteiro(String s) {
		boolean resp = true;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (i == 0 && (c == '+' || c == '-')) {
				// Pode ignorar o if
			}
			else if (c < '0' || c > '9') {
				resp = false;
			}
		}

		if (s.length() == 1 && (s.charAt(0) == '+' || s.charAt(0) == '-')) {
			resp = false;
		}

		return resp;
	}

	// Método para verificar se é real
	public static boolean isReal(String s) {
		boolean resp = true;
		int cont = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (i == 0 && (c == '+' || c == '-')) {
				// Pode ignorar o if 
			}
			else if (c == '.' || c == ',') {
				cont++;
				if (cont > 1) {
					resp = false;
				}
			}
			else if (c < '0' || c > '9') {
				resp = false;
			}
		}

		if (s.length() == 1 && (s.charAt(0) == '+' || s.charAt(0) == '-')) {
			resp = false;
		}

		return resp;
	}

	public static void main(String[] args) {

		String entrada = MyIO.readLine();

		while (!isFim(entrada)) {

			boolean x1 = isVogal(entrada);
			boolean x2 = isConsoante(entrada);
			boolean x3 = isInteiro(entrada);
			boolean x4 = isReal(entrada);

			MyIO.println(
					(x1 ? "SIM" : "NAO") + " " +
					(x2 ? "SIM" : "NAO") + " " +
					(x3 ? "SIM" : "NAO") + " " +
					(x4 ? "SIM" : "NAO")
				    );

			entrada = MyIO.readLine();
		}
	}
}
