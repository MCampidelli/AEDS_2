//Algoritmos e Estruturas de Dados  01 ----- Trabalho Prático 01
//Questão 12 - Soma de Dígitos (RECURSIVO)

import java.util.Scanner;

class SomaDigitosRec {

	public static boolean isFim(String s) {
		return (s.length() == 3 &&
				s.charAt(0) == 'F' &&
				s.charAt(1) == 'I' &&
				s.charAt(2) == 'M');
	}

	static int SomaDigitos(int n) {
		return SomaDigitos(Math.abs((long) n));
	}

	static int SomaDigitos( long n) {

		int resp;

		if (n < 10) {
			resp = (int) n;
		} else {
			resp = (int)(n % 10) + SomaDigitos(n / 10);
		}
		return resp;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (sc.hasNextLine()) {
			String linha = sc.nextLine();
			if (isFim(linha)) { break; }

			int numero = 0;
			try {
				numero = Integer.parseInt(linha.trim());
			} catch (NumberFormatException e) {
				numero = 0;
			}

			System.out.println(SomaDigitos(numero));
		}

		sc.close();
	}
}
