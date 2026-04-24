// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 01
// Questão 06 - Verificação de Anagrama

#include <stdio.h>
#include <stdlib.h>

int isFim(char* s) {
    return s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0';
}

int getTamanho(char* s) {
    int tamanho = 0;

    for (int i = 0; s[i] != '\0'; i++) {
        tamanho++;
    }

    return tamanho;
}

void toLowe (char* s) {

     for (int i = 0; s[i] != '\0'; i++) {
          if (s[i] >= 'A' && s[i] <= 'Z') {
               s[i] = s[i] + ('a' - 'A');
          }
     }
}

int anagrama(char* s1, char* s2) {
    int tam1 = getTamanho(s1);
    int tam2 = getTamanho(s2);

    if (tam1 != tam2) return 0;

    for (int i = 0; i < tam1; i++) {
        int countS1 = 0;
        int countS2 = 0;
        for (int j = 0; j < tam1; j++) {
            if (s1[j] == s1[i]) countS1++;
            if (s2[j] == s1[i]) countS2++;
        }
        if (countS1 != countS2) return 0;
    }

    return 1;
}

int main() {
    char* palavra1 = (char*) malloc(100 * sizeof(char));
    char* palavra2 = (char*) malloc(100 * sizeof(char));

    scanf("%s", palavra1);

    while (!isFim(palavra1)) {
        scanf("%s", palavra2);
        toLowe(palavra1);
        toLowe(palavra2);
        printf("%s\n", anagrama(palavra1, palavra2) ? "SIM" : "NAO");
        scanf("%s", palavra1);
    }

    free(palavra1);
    free(palavra2);

    return 0;
}
