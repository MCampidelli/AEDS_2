// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 01
// Questão 04 - Inversão de String

#include <stdio.h>
#include <stdlib.h>

// Função para inverter string
char* inverte (char* str) {
	int tamanho = 0;
	
	while (str[tamanho] != '\0') {
		tamanho++;
	}
	
	char* invertida = (char*) malloc ((tamanho + 1) * sizeof(char));

	// For para inverter a string
	for (int i = 0; i < tamanho; i++) {
		invertida[i] = str[tamanho - 1 - i];
	}

	invertida[tamanho] = '\0';

	return invertida;
}

int isFim(char* s) {
	return s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0';
}

int main () {
	char* str = (char*) malloc (200 * sizeof(char));

	scanf(" %[^\n]", str);

	while (!isFim(str)) {
		char* resp = inverte(str); // Vai receber a string invertida
		printf("%s\n", resp); // Imprime a string
		free(resp);
		scanf(" %[^\n]", str);
	}

	free(str);

	return 0;
}
