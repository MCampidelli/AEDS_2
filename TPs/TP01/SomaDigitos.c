// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 01
// Questão 05 - Soma de Dígitos 

#include <stdio.h>
#include <stdlib.h>

// Função que soma os dígitos
int soma(int n) {

	// Número negativo
	if (n < 0) {
		n = -n;
	}

	if (n == 0) {
		return 0;
	} 

	return (n % 10) + soma(n/10);
}

int main() {
        char* numStr = (char*) malloc(50 * sizeof(char));

  	 while (scanf("%s", numStr) != EOF) {
		printf("%d\n", soma(atoi(numStr)));
         }

	free(numStr);
        return 0;
	
}



