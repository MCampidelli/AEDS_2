// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 2
// Questão 12 ----- Pilha com Alocação Sequencial em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int dia, mes, ano;
} Data;

typedef struct {
    int hora, minuto;
} Hora;

typedef struct {
    int id;
    char nome[200];
    char cidade[200];
    int capacidade;
    double avaliacao;
    char tipos[10][100];
    int numTipos;
    int faixaPreco;
    Hora abertura, fechamento;
    Data data;
    int aberto;
} Restaurante;

// Pilha
typedef struct {
    Restaurante* array[1000];
    int topo;
} Pilha;

void start(Pilha* p) {
    p->topo = 0;
}

void push(Pilha* p, Restaurante* x) {
    p->array[p->topo++] = x;
}

Restaurante* pop(Pilha* p) {
    return p->array[--p->topo];
}

// Print
void printRestaurante(Restaurante* r) {

    printf("[%d ## %s ## %s ## %d ## %.1lf ## [",
           r->id, r->nome, r->cidade, r->capacidade, r->avaliacao);

    for (int i = 0; i < r->numTipos; i++) {
        printf("%s", r->tipos[i]);
        if (i < r->numTipos - 1) printf(",");
    }

    printf("] ## ");

    for (int i = 0; i < r->faixaPreco; i++) printf("$");

    printf(" ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]\n",
           r->abertura.hora, r->abertura.minuto,
           r->fechamento.hora, r->fechamento.minuto,
           r->data.dia, r->data.mes, r->data.ano,
           r->aberto ? "true" : "false");
}

Hora parseHora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

Data parseData(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

Restaurante* parseRestaurante(char* linha) {

    Restaurante* r = malloc(sizeof(Restaurante));

    char tiposStr[200], precoStr[10], horario[50], dataStr[50], abertoStr[10];

    sscanf(linha,
        "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^,],%[^,],%s",
        &r->id,
        r->nome,
        r->cidade,
        &r->capacidade,
        &r->avaliacao,
        tiposStr,
        precoStr,
        horario,
        dataStr,
        abertoStr
    );

    r->numTipos = 0;
    char* token = strtok(tiposStr, ";");
    while (token) {
        strcpy(r->tipos[r->numTipos++], token);
        token = strtok(NULL, ";");
    }

    r->faixaPreco = strlen(precoStr);

    char h1[10], h2[10];
    sscanf(horario, "%[^-]-%s", h1, h2);
    r->abertura = parseHora(h1);
    r->fechamento = parseHora(h2);

    r->data = parseData(dataStr);
    r->aberto = strcmp(abertoStr, "true") == 0;

    return r;
}

typedef struct {
    int tamanho;
    Restaurante* array[10000];
} Colecao;

void lerCSV(Colecao* c) {

    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    char linha[1000];

    fgets(linha, sizeof(linha), f);

    c->tamanho = 0;

    while (fgets(linha, sizeof(linha), f)) {
        linha[strcspn(linha, "\n")] = 0;
        c->array[c->tamanho++] = parseRestaurante(linha);
    }

    fclose(f);
}

// Buscar
Restaurante* buscar(Colecao* c, int id) {
    for (int i = 0; i < c->tamanho; i++) {
        if (c->array[i]->id == id) return c->array[i];
    }
    return NULL;
}

int main() {

    Colecao c;
    lerCSV(&c);

    Pilha p;
    start(&p);

    int id;

    while (scanf("%d", &id) && id != -1) {
        push(&p, buscar(&c, id));
    }

    int m;
    scanf("%d", &m);

    char comando[10];

    for (int i = 0; i < m; i++) {

        scanf("%s", comando);

        if (strcmp(comando, "I") == 0) {
            scanf("%d", &id);
            push(&p, buscar(&c, id));
        }

        else if (strcmp(comando, "R") == 0) {
            Restaurante* r = pop(&p);
            printf("(R)%s\n", r->nome);
        }
    }

    for (int i = p.topo -1;i >= 0; i--) {
        printRestaurante(p.array[i]);
    }

    return 0;
}
