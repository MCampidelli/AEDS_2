// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 04
// Questão 5 ----- Tabela Hash Indireta com Lista Simples em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define tamTab 31


typedef struct {
    int id;
    char nome[100];
    char cidade[200];
    int capacidade;
    double avaliacao;
} Restaurante;


typedef struct No {
    Restaurante* r;
    struct No* prox;
} No;


typedef struct {
    No* tabela[tamTab];
} Hash;


int getTamanho(char* s) {
    int i = 0;
    while (s[i] != '\0') i++;
    return i;
}

int hash(char* nome) {
    int soma = 0;
    for (int i = 0; nome[i] != '\0'; i++) {
        soma += (int) nome[i];
    }
    return soma % tamTab;
}


void inserirHash(Restaurante* r, Hash* h) {
    int pos = hash(r->nome);

    No* novo = (No*) malloc(sizeof(No));
    novo->r = r;
    novo->prox = NULL;

    if (h->tabela[pos] == NULL) {
        h->tabela[pos] = novo;
    } else {
        novo->prox = h->tabela[pos];
        h->tabela[pos] = novo;
    }
}


int pesquisaHash(char* nome, long* comp, Hash* h) {
    int pos = hash(nome);

    No* atual = h->tabela[pos];

    while (atual != NULL) {
        (*comp)++;
        if (strcmp(atual->r->nome, nome) == 0) {
            return pos;
        }
        atual = atual->prox;
    }

    return -1;
}


void arqLog(char* matricula, double tempo, long comp) {
    char nomeArq[100];
    sprintf(nomeArq, "%s_hash_indireta.txt", matricula);

    FILE* arq = fopen(nomeArq, "w");
    if (arq != NULL) {
        fprintf(arq, "%s\t%ld\t%.2f\n", matricula, comp, tempo);
        fclose(arq);
    }
}


int main() {

    Hash* h = (Hash*) malloc(sizeof(Hash));

    for (int i = 0; i < tamTab; i++) {
        h->tabela[i] = NULL;
    }

    char linha[200];
    int id;
    long comparacoes = 0;

    clock_t inicio = clock();


    while (scanf(" %[^\n]", linha) == 1) {
        id = atoi(linha);
        if (id == -1) break;

        Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));
        r->id = id;

        sprintf(r->nome, "Nome%d", id);

        inserirHash(r, h);
    }


    char nomeBusca[200];

    while (scanf(" %[^\n]", nomeBusca) == 1) {
        if (strcmp(nomeBusca, "FIM") == 0) break;

        int pos = pesquisaHash(nomeBusca, &comparacoes, h);

        if (pos != -1) {
            No* atual = h->tabela[pos];

            while (atual != NULL) {
                if (strcmp(atual->r->nome, nomeBusca) == 0) {
                    printf("%d %s\n", pos, atual->r->nome);
                    break;
                }
                atual = atual->prox;
            }
        } else {
            printf("-1\n");
        }
    }

    clock_t fim = clock();

    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000;

    arqLog("810688", tempo, comparacoes);

    return 0;
}
