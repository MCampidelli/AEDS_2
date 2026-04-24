//Algoritmos e Estruturas de Dados 02 ----- Trabalho Prático 01
//Questão 08 - Validação de Senha

import java.util.Scanner;

public class Senha {

	static boolean isMaiuscula(char c) {
		return (c >= 'A' && c <= 'Z');
	}

	static boolean isMinuscula(char c) {
		return (c >= 'a' && c <= 'z');
	}

	static boolean isDigito(char c) {
		return (c >= '0' && c <= '9');
	}

	static boolean isEspecial(char c) {
		boolean resp;
		resp = (!isMaiuscula(c) && !isMinuscula(c) && !isDigito(c));
		return resp;
	}

	static boolean senhaValida(String senha) {
		boolean resp;
		boolean temMaiuscula = false;
		boolean temMinuscula = false;
		boolean temDigito    = false;
		boolean temEspecial  = false;

		for (int i = 0; i < senha.length(); i++) {
			char c = senha.charAt(i);
			if (isMaiuscula(c)) { temMaiuscula = true; }
			if (isMinuscula(c)) { temMinuscula = true; }
			if (isDigito(c))    { temDigito    = true; }
			if (isEspecial(c))  { temEspecial  = true; }
		}

		resp = (senha.length() >= 8 && temMaiuscula && temMinuscula
				&& temDigito && temEspecial);
		return resp;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (sc.hasNextLine()) {
			String linha = sc.nextLine();
			if (linha.equals("FIM")) { break; }
			if (senhaValida(linha)) {
				System.out.println("SIM");
			} else {
				System.out.println("NAO");
			}
		}

		sc.close();
	}
}
