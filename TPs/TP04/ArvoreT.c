// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 04
// Questão 9 ----- Árvore Trie com Lista Flexível

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

typedef struct {
    int id;
    char nome[100];
    char cidade[200];
    int capacidade;
    double avaliacao;
} Restaurante;

typedef struct Trie {
    char c;
    struct Trie* filho;
    struct Trie* irmao;
    Restaurante* r;
} Trie;

typedef struct {
    Restaurante** v;
    int n;
} Colecao;

Trie* criarTrie(char c) {
    Trie* t = (Trie*) malloc(sizeof(Trie));
    t->c = c;
    t->filho = NULL;
    t->irmao = NULL;
    t->r = NULL;
    return t;
}

Restaurante* parse(char* l) {
    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));
    sscanf(l, "%d , %[^,] , %[^,] , %d , %lf",
           &r->id, r->nome, r->cidade, &r->capacidade, &r->avaliacao);
    return r;
}

void inserirTrie(Trie* raiz, char* nome, Restaurante* r) {
    Trie* atual = raiz;
    for (int i = 0; nome[i] != '\0'; i++) {
        Trie* ant = NULL;
        Trie* p = atual->filho;

        while (p != NULL && p->c != nome[i]) {
            ant = p;
            p = p->irmao;
        }

        if (p == NULL) {
            p = criarTrie(nome[i]);
            if (ant == NULL) atual->filho = p;
            else ant->irmao = p;
        }

        atual = p;
    }
    atual->r = r;
}

Trie* buscarTrie(Trie* raiz, char* nome, long* comp) {
    Trie* atual = raiz;

    for (int i = 0; nome[i] != '\0'; i++) {
        printf("%c ", atual->c);

        Trie* p = atual->filho;
        while (p != NULL && p->c != nome[i]) {
            (*comp)++;
            p = p->irmao;
        }

        if (p == NULL) return NULL;

        atual = p;
    }

    printf("%c ", atual->c);
    return atual;
}

Colecao* lerCSV() {
    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    if (!f) return NULL;

    char l[1024];
    fgets(l, 1024, f);

    int n = 0;
    while (fgets(l, 1024, f)) n++;

    rewind(f);
    fgets(l, 1024, f);

    Colecao* c = (Colecao*) malloc(sizeof(Colecao));
    c->v = (Restaurante**) malloc(n * sizeof(Restaurante*));
    c->n = n;

    int i = 0;
    while (fgets(l, 1024, f)) {
        c->v[i++] = parse(l);
    }

    fclose(f);
    return c;
}

void logg(char* mat, double tempo, long comp) {
    char arq[200];
    sprintf(arq, "%s_arvore_trie_lista.txt", mat);

    FILE* f = fopen(arq, "w");
    fprintf(f, "%s\t%ld\t%.2f\n", mat, comp, tempo);
    fclose(f);
}

int main() {
    Trie* raiz = criarTrie('\0');
    Colecao* c = lerCSV();

    char linha[200];
    int id;
    long comp = 0;

    clock_t ini = clock();

    while (scanf(" %[^\n]", linha) == 1) {
        id = atoi(linha);
        if (id == -1) break;

        for (int i = 0; i < c->n; i++) {
            if (c->v[i]->id == id) {
                inserirTrie(raiz, c->v[i]->nome, c->v[i]);
                break;
            }
        }
    }

    char nome[200];

    while (scanf(" %[^\n]", nome) == 1) {
        if (strcmp(nome, "FIM") == 0) break;

        Trie* res = buscarTrie(raiz, nome, &comp);

        if (res != NULL && res->r != NULL) {
            printf("SIM [%d ## %s ## %s ## %d ## %.1f]\n",
                   res->r->id, res->r->nome, res->r->cidade,
                   res->r->capacidade, res->r->avaliacao);
        } else {
            printf("NAO\n");
        }
    }

    clock_t fim = clock();

    double tempo = (double)(fim - ini) / CLOCKS_PER_SEC * 1000;

    logg("810688", tempo, comp);

    return 0;
}
