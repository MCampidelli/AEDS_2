//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 01
//Questão 02 - Alteração Aleatória

import java.util.*;

class aleatoria {
	
	// Método que irá retornar a string modificada
	public static String alterar(String s, Random gerador) {

		// Gerar letras
		char a = (char) ('a' + (Math.abs(gerador.nextInt()) % 26)); // Primeira letra
		char b = (char) ('a' + (Math.abs(gerador.nextInt()) % 26 )); // Segunda letra
		
		String resp = "";

		// Loop que vai pegar o caracter atual, verificar se é ou não a letra que foi sorteada, e retorná-la.
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if(c == a) {
				resp = resp + b;
			} else {
				resp = resp + c ;
			}
		}
		return resp;
	}

	// Final da entrada
	public static boolean isFim(String s) {
		return (s.length() == 3 && s.charAt(0) =='F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
	}	

	public static void main(String[] args) throws Exception {
		
		// Criação do gerador
		Random gerador = new Random();
		gerador.setSeed(4);

		String entrada = MyIO.readLine();

		while(!isFim(entrada)) {
			String resp = alterar(entrada, gerador);
			System.out.println(resp);
			entrada = MyIO.readLine();
		}
	}
}
