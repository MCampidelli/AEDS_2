//Algoritmos e Estruturas de Dados 02 ----- Trabalho Prático 01
//Questão 09 - Ciframento de César (RECURSIVO)

#include <stdio.h>
#include <stdlib.h>

int isFim(char* s) {
    return s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0';
}

// ! Erro do \n
void removeQuebraLinha(char* s) {
    int i = 0;
    while (s[i] != '\0') {
        if (s[i] == '\n') {
            s[i] = '\0';
        }
        i++;
    }
}

// Função de cifra
void cifra(char* s, int i) {

    if (s[i] == '\0') {
        return;
    }

    // modifica o atual
    s[i] = s[i] + 3;

    cifra(s, i + 1);
}

int main() {
    char str[200];

    while (fgets(str, 200, stdin) != NULL) {

        removeQuebraLinha(str);

        if (isFim(str)) break;

        cifra(str, 0);

        printf("%s\n", str);
    }

    return 0;
}
