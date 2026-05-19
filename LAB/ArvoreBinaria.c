#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// ==================== STRUCT NO ====================

typedef struct No {
    int elemento;
    struct No* esq;
    struct No* dir;
} No;

// Cria um novo nó
No* new_no(int elemento) {
    No* novo = (No*) malloc(sizeof(No));

    novo->elemento = elemento;
    novo->esq = NULL;
    novo->dir = NULL;

    return novo;
}

// Remove recursivamente os nós
void delete_no(No* no) {
    if (no != NULL) {
        delete_no(no->esq);
        delete_no(no->dir);
        free(no);
    }
}

// ==================== STRUCT ARVORE ====================

typedef struct ArvoreBinaria {
    No* raiz;
} ArvoreBinaria;

// Cria uma nova árvore
ArvoreBinaria* new_arvore_binaria() {
    ArvoreBinaria* ab = (ArvoreBinaria*) malloc(sizeof(ArvoreBinaria));
    ab->raiz = NULL;
    return ab;
}

// Remove a árvore
void delete_arvore_binaria(ArvoreBinaria* ab) {
    if (ab != NULL) {
        delete_no(ab->raiz);
        free(ab);
    }
}

// ==================== INSERÇÃO ====================

No* inserir_rec(No* raiz, int x) {

    if (raiz == NULL) {
        raiz = new_no(x);
    }
    else if (x < raiz->elemento) {
        raiz->esq = inserir_rec(raiz->esq, x);
    }
    else if (x > raiz->elemento) {
        raiz->dir = inserir_rec(raiz->dir, x);
    }

    return raiz;
}

void inserir(ArvoreBinaria* ab, int x) {
    ab->raiz = inserir_rec(ab->raiz, x);
}

// ==================== PESQUISA ====================

int pesquisar_rec(No* raiz, int x) {

    if (raiz == NULL) {
        return 0;
    }

    printf("%d ", raiz->elemento);

    if (x == raiz->elemento) {
        return 1;
    }
    else if (x < raiz->elemento) {
        return pesquisar_rec(raiz->esq, x);
    }
    else {
        return pesquisar_rec(raiz->dir, x);
    }
}

int pesquisar(ArvoreBinaria* ab, int x) {
    return pesquisar_rec(ab->raiz, x);
}

// ==================== CAMINHAMENTOS ====================

void pre_rec(No* raiz) {
    if (raiz != NULL) {
        printf("%d ", raiz->elemento);
        pre_rec(raiz->esq);
        pre_rec(raiz->dir);
    }
}

void caminhar_pre(ArvoreBinaria* ab) {

    if (ab->raiz == NULL) {
        printf("V");
    }
    else {
        pre_rec(ab->raiz);
    }

    printf("\n");
}

// --------------------

void pos_rec(No* raiz) {
    if (raiz != NULL) {
        pos_rec(raiz->esq);
        pos_rec(raiz->dir);
        printf("%d ", raiz->elemento);
    }
}

void caminhar_pos(ArvoreBinaria* ab) {

    if (ab->raiz == NULL) {
        printf("V");
    }
    else {
        pos_rec(ab->raiz);
    }

    printf("\n");
}

// --------------------

void em_rec(No* raiz) {
    if (raiz != NULL) {
        em_rec(raiz->esq);
        printf("%d ", raiz->elemento);
        em_rec(raiz->dir);
    }
}

void caminhar_em(ArvoreBinaria* ab) {

    if (ab->raiz == NULL) {
        printf("V");
    }
    else {
        em_rec(ab->raiz);
    }

    printf("\n");
}

// ==================== MAIN ====================

int main() {

    ArvoreBinaria* ab = new_arvore_binaria();

    char comando[10];
    int valor;

    while (scanf("%s", comando) != EOF) {

        // Inserir
        if (strcmp(comando, "I") == 0) {
            scanf("%d", &valor);
            inserir(ab, valor);
        }

        // Pesquisar
        else if (strcmp(comando, "P") == 0) {

            scanf("%d", &valor);

            int resp = pesquisar(ab, valor);

            if (resp) {
                printf("S\n");
            }
            else {
                printf("N\n");
            }
        }

        // Pré-ordem
        else if (strcmp(comando, "PRE") == 0) {
            caminhar_pre(ab);
        }

        // Pós-ordem
        else if (strcmp(comando, "POS") == 0) {
            caminhar_pos(ab);
        }

        // Em-ordem
        else if (strcmp(comando, "EM") == 0) {
            caminhar_em(ab);
        }
    }

    delete_arvore_binaria(ab);

    return 0;
}
