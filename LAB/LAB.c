#include <stdio.h>
#include <string.h>

typedef struct {
    char nome[50];
    int peso;
} Atleta;

int main() {
    Atleta atletas[100];
    int n = 0;
     
    while (scanf("%s %d", atletas[n].nome, &atletas[n].peso) != EOF) {
        n++;
    }

    for (int i = 0; i < n - 1; i++) {
        for (int j = i + 1; j < n; j++) {
            if (atletas[i].peso < atletas[j].peso) {
                Atleta temp = atletas[i];
                atletas[i] = atletas[j];
                atletas[j] = temp;
            } else if (atletas[i].peso == atletas[j].peso) {
                if (strcmp(atletas[i].nome, atletas[j].nome) > 0) {
                    Atleta temp = atletas[i];
                    atletas[i] = atletas[j];
                    atletas[j] = temp;
                }
            }
        }
    }

    for (int i = 0; i < n; i++) {
        printf("%s %d\n", atletas[i].nome, atletas[i].peso);
    }

    return 0;
}