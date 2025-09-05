// Algoritmos e Estruturas de Dados II
// Trabalho Prático 2 - Questão 4 - Soma de Dígitos
// Marina Campidelli 

#include <stdio.h>
#include <string.h>
#include <stdlib.h>


int somaDigitos(int n) {
    if (n < 10) {
        return n;
    } else {
        return (n % 10) + somaDigitos(n / 10);
    }
}

int main() {
    char entrada[100];

    while (1) {
        scanf("%s", entrada);

        if (strcmp(entrada, "FIM") == 0) {
            break;
        }

        int n = atoi(entrada);

        if (n < 0) {
            n = -n;
        }

        printf("%d\n", somaDigitos(n));
    }

    return 0;
}

