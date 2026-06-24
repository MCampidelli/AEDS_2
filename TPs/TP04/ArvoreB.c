// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 04
// Questão 7 ----- Árvore Binária de Lista Simples

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

//////////////////// RESTAURANTE ////////////////////

typedef struct {
    int id;
    char nome[100];
    char cidade[200];
    int capacidade;
    double avaliacao;
} Restaurante;

//////////////////// LISTA ////////////////////

typedef struct NoLista {
    Restaurante* r;
    struct NoLista* prox;
} NoLista;

//////////////////// ÁRVORE ////////////////////

typedef struct NoArvore {
    char chave;
    struct NoArvore* esq;
    struct NoArvore* dir;
    NoLista* lista;
} NoArvore;

//////////////////// UTIL ////////////////////

int getTamanho(char* s) {
    int i = 0;
    while (s[i] != '\0') i++;
    return i;
}

//////////////////// CRIA NÓ ////////////////////

NoArvore* criarNo(char chave) {
    NoArvore* n = (NoArvore*) malloc(sizeof(NoArvore));
    n->chave = chave;
    n->esq = NULL;
    n->dir = NULL;
    n->lista = NULL;
    return n;
}

//////////////////// ÁRVORE BST ////////////////////

NoArvore* inserirArvore(NoArvore* raiz, char chave) {
    if (raiz == NULL) return criarNo(chave);

    if (chave < raiz->chave)
        raiz->esq = inserirArvore(raiz->esq, chave);
    else if (chave > raiz->chave)
        raiz->dir = inserirArvore(raiz->dir, chave);

    return raiz;
}

//////////////////// LISTA ORDENADA ////////////////////

void inserirLista(NoLista** lista, Restaurante* r) {
    NoLista* novo = (NoLista*) malloc(sizeof(NoLista));
    novo->r = r;
    novo->prox = NULL;

    if (*lista == NULL || strcmp(r->nome, (*lista)->r->nome) < 0) {
        novo->prox = *lista;
        *lista = novo;
        return;
    }

    NoLista* aux = *lista;

    while (aux->prox != NULL &&
           strcmp(aux->prox->r->nome, r->nome) < 0) {
        aux = aux->prox;
    }

    novo->prox = aux->prox;
    aux->prox = novo;
}

//////////////////// INSERÇÃO HÍBRIDA ////////////////////

NoArvore* inserir(NoArvore* raiz, Restaurante* r) {

    char chave = r->nome[0];

    raiz = inserirArvore(raiz, chave);

    NoArvore* atual = raiz;

    while (atual != NULL) {
        if (chave < atual->chave)
            atual = atual->esq;
        else if (chave > atual->chave)
            atual = atual->dir;
        else {
            inserirLista(&atual->lista, r);
            break;
        }
    }

    return raiz;
}

//////////////////// PESQUISA ////////////////////

int pesquisar(NoArvore* raiz, char* nome, long* comp) {

    printf("RAIZ ");

    char chave = nome[0];
    NoArvore* atual = raiz;

    // BUSCA NA ÁRVORE
    while (atual != NULL && atual->chave != chave) {
        (*comp)++;

        if (chave < atual->chave) {
            printf("ESQ ");
            atual = atual->esq;
        } else {
            printf("DIR ");
            atual = atual->dir;
        }
    }

    if (atual == NULL) {
        printf("NAO\n");
        return 0;
    }

    // BUSCA NA LISTA
    NoLista* aux = atual->lista;

    while (aux != NULL && strcmp(aux->r->nome, nome) <= 0) {

        printf("%s ", aux->r->nome);

        if (strcmp(aux->r->nome, nome) == 0) {
            printf("SIM [");
            printf("%d ## %s ## %s ## %d ## %.1f]\n",
                aux->r->id,
                aux->r->nome,
                aux->r->cidade,
                aux->r->capacidade,
                aux->r->avaliacao
            );
            return 1;
        }

        aux = aux->prox;
    }

    printf("NAO\n");
    return 0;
}

//////////////////// CSV ////////////////////

Restaurante* parseRestaurante(char* linha) {
    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));

    sscanf(linha,
        "%d , %[^,] , %[^,] , %d , %lf",
        &r->id,
        r->nome,
        r->cidade,
        &r->capacidade,
        &r->avaliacao
    );

    return r;
}

typedef struct {
    Restaurante** restaurantes;
    int tamanho;
} colecaoRestaurante;

void lerCSV(colecaoRestaurante* c, char* path) {
    FILE* arq = fopen(path, "r");
    if (!arq) return;

    char linha[1024];
    fgets(linha, sizeof(linha), arq);

    int count = 0;
    while (fgets(linha, sizeof(linha), arq)) count++;

    rewind(arq);
    fgets(linha, sizeof(linha), arq);

    c->restaurantes = (Restaurante**) malloc(count * sizeof(Restaurante*));
    c->tamanho = count;

    int i = 0;

    while (fgets(linha, sizeof(linha), arq)) {
        linha[strcspn(linha, "\n")] = '\0';
        c->restaurantes[i++] = parseRestaurante(linha);
    }

    fclose(arq);
}

colecaoRestaurante* lerCsv() {
    colecaoRestaurante* c = (colecaoRestaurante*) malloc(sizeof(colecaoRestaurante));
    lerCSV(c, "/tmp/restaurantes.csv");
    return c;
}

//////////////////// LOG ////////////////////

void arqLog(char* matricula, double tempo, long comp) {
    char nomeArq[100];
    sprintf(nomeArq, "%s_hibrida_arvore_lista.txt", matricula);

    FILE* arq = fopen(nomeArq, "w");
    fprintf(arq, "%s\t%ld\t%.2f\n", matricula, comp, tempo);
    fclose(arq);
}

//////////////////// MAIN ////////////////////

int main() {

    NoArvore* raiz = NULL;

    colecaoRestaurante* c = lerCsv();

    char linha[200];
    int id;
    long comp = 0;

    clock_t inicio = clock();

    // INSERÇÃO
    while (scanf(" %[^\n]", linha) == 1) {
        id = atoi(linha);
        if (id == -1) break;

        for (int i = 0; i < c->tamanho; i++) {
            if (c->restaurantes[i]->id == id) {
                raiz = inserir(raiz, c->restaurantes[i]);
                break;
            }
        }
    }

    // BUSCA
    char nome[200];

    while (scanf(" %[^\n]", nome) == 1) {
        if (strcmp(nome, "FIM") == 0) break;

        pesquisar(raiz, nome, &comp);
    }

    clock_t fim = clock();

    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000;

    arqLog("810688", tempo, comp);

    return 0;
}
