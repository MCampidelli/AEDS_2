//Algoritmos e Estruturas de Dados 02 ----- Trabalho Prático 01
//Questão 10 - Is (RECURSIVO)

#include <stdio.h>
#include <stdlib.h>

int isFim(char* s) {
	return s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0';
}

// erro '\n'
void removeQuebraLinha(char* s) {
	int i = 0;
	while (s[i] != '\0') {
		if (s[i] == '\n') s[i] = '\0';
		i++;
	}
}

// Função que verifica se é vogal
int isVogal(char* s, int i) {

	if (s[i] == '\0') return 1;

	char c = s[i];

	if (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u' &&
			c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U') {
		return 0;
	}

	return isVogal(s, i + 1);
}

int ehLetra(char c) {
	return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
}

int isConsoante(char* s, int i) {

	if (s[i] == '\0') return 1;

	char c = s[i];

	if (!ehLetra(c) ||
			c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
			c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
		return 0;
	}

	return isConsoante(s, i + 1);
}

int isInteiro(char* s, int i) {

	if (s[i] == '\0') return (i > 0);

	char c = s[i];

	if (i == 0 && (c == '+' || c == '-')) {
		return isInteiro(s, i + 1);
	}

	if (c < '0' || c > '9') return 0;

	return isInteiro(s, i + 1);
}

int isReal(char* s, int i, int cont) {

	if (s[i] == '\0') {
		return (i > 0 && cont <= 1);
	}

	char c = s[i];

	if (i == 0 && (c == '+' || c == '-')) {
		return isReal(s, i + 1, cont);
	}

	if (c == '.' || c == ',') {
		return isReal(s, i + 1, cont + 1);
	}

	if (c < '0' || c > '9') return 0;

	return isReal(s, i + 1, cont);
}

int main() {
	char str[200];

	while (fgets(str, 200, stdin) != NULL) {

		removeQuebraLinha(str);

		if (isFim(str)) break;

		int x1 = isVogal(str, 0);
		int x2 = isConsoante(str, 0);
		int x3 = isInteiro(str, 0);
		int x4 = isReal(str, 0, 0);

		printf("%s %s %s %s\n",
				x1 ? "SIM" : "NAO",
				x2 ? "SIM" : "NAO",
				x3 ? "SIM" : "NAO",
				x4 ? "SIM" : "NAO");
	}

	return 0;
}
