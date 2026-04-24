//Algoritmos e Estruturas de Dados 02 ----- Trabalho prático 01
//Questão 07 - Substring Mais Longa Sem Repetição

#include <stdio.h>
#include <stdlib.h>

int isFim(char* s) {
	return s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0';
}

// Função 
int maiorSubstring(char* s) {
	int max = 0;

	for (int i = 0; s[i] != '\0'; i++) {

		int usado[256] = {0}; // ASCII
		int tamAtual = 0;

		for (int j = i; s[j] != '\0'; j++) {

			if (usado[(int)s[j]] == 1) {
				break;
			}

			usado[(int)s[j]] = 1;
			tamAtual++;
		}

		// Atualizar o máximo
		if (tamAtual > max) {
			max = tamAtual;
		}
	}

	return max;
}

int main() {
	char str[1000];

	scanf(" %[^\n]", str);

	while (!isFim(str)) {

		printf("%d\n", maiorSubstring(str));

		scanf(" %[^\n]", str);
	}

	return 0;
}
