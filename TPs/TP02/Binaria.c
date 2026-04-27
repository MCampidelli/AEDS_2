// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 2
// Questão 06 ----- Pesquisa Binária em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

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

typedef struct {
    int tamanho;
    Restaurante* array[10000];
} Colecao;

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

// Busca por ID
Restaurante* buscar(Colecao* c, int id) {
    for (int i = 0; i < c->tamanho; i++) {
        if (c->array[i]->id == id) {
            return c->array[i];
        }
    }
    return NULL;
}

// Seleção
long comparacoes = 0;

void swap(Restaurante** a, Restaurante** b) {
    Restaurante* tmp = *a;
    *a = *b;
    *b = tmp;
}

void selectionSort(Restaurante* arr[], int n) {

    for (int i = 0; i < n - 1; i++) {
        int menor = i;

        for (int j = i + 1; j < n; j++) {
            comparacoes++;
            if (strcmp(arr[j]->nome, arr[menor]->nome) < 0) {
                menor = j;
            }
        }
        swap(&arr[i], &arr[menor]);
    }
}

// Pesquisa Binária
int buscaBinaria(Restaurante* arr[], int n, char* chave) {

    int esq = 0, dir = n - 1;

    while (esq <= dir) {
        int meio = (esq + dir) / 2;

        comparacoes++;
        int cmp = strcmp(arr[meio]->nome, chave);

        if (cmp == 0) {
            return 1;
        } else if (cmp < 0) {
            esq = meio + 1;
        } else {
            dir = meio - 1;
        }
    }
    return 0;
}

int main() {

    clock_t inicio = clock();

    Colecao c;
    lerCSV(&c);

    Restaurante* base[1000];
    int n = 0;

    int id;

    while (scanf("%d", &id) && id != -1) {
        base[n++] = buscar(&c, id);
    }

    selectionSort(base, n);

    char nome[200];
    getchar();

    while (fgets(nome, sizeof(nome), stdin)) {

        nome[strcspn(nome, "\n")] = 0;

        if (strcmp(nome, "FIM") == 0) break;

        int resp = buscaBinaria(base, n, nome);
        printf("%s\n", resp ? "SIM" : "NAO");
    }

    clock_t fim = clock();
    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    FILE* log = fopen("matricula_binaria.txt", "w");
    fprintf(log, "123456\t%ld\t%f", comparacoes, tempo);
    fclose(log);

    return 0;
}
