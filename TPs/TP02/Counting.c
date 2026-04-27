// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 2
// Questõ 10 ----- Ordenação por Counting Sort em C

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

long comparacoes = 0;
long movimentacoes = 0;

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

// Buscar
Restaurante* buscar(Colecao* c, int id) {
    for (int i = 0; i < c->tamanho; i++) {
        if (c->array[i]->id == id) {
            return c->array[i];
        }
    }
    return NULL;
}

// Inserção
void insertionSort(Restaurante* arr[], int n) {

    for (int i = 1; i < n; i++) {
        Restaurante* tmp = arr[i];
        int j = i - 1;

        while (j >= 0 && strcmp(arr[j]->nome, tmp->nome) > 0) {
            comparacoes++;
            arr[j + 1] = arr[j];
            movimentacoes++;
            j--;
        }

        arr[j + 1] = tmp;
        movimentacoes++;
    }
}

// Counting
void countingSort(Restaurante* arr[], int n) {

    int max = arr[0]->capacidade;

    for (int i = 1; i < n; i++) {
        if (arr[i]->capacidade > max) {
            max = arr[i]->capacidade;
        }
    }

    int* count = calloc(max + 1, sizeof(int));
    Restaurante** ordenado = malloc(n * sizeof(Restaurante*));

    for (int i = 0; i < n; i++) {
        count[arr[i]->capacidade]++;
    }

    for (int i = 1; i <= max; i++) {
        count[i] += count[i - 1];
    }

    for (int i = n - 1; i >= 0; i--) {
        int pos = count[arr[i]->capacidade] - 1;
        ordenado[pos] = arr[i];
        movimentacoes++;
        count[arr[i]->capacidade]--;
    }

    for (int i = 0; i < n; i++) {
        arr[i] = ordenado[i];
    }

    free(count);
    free(ordenado);
}

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

    insertionSort(base, n);

    countingSort(base, n);

    for (int i = 0; i < n; i++) {
        printRestaurante(base[i]);
    }

    clock_t fim = clock();
    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    FILE* log = fopen("matricula_counting.txt", "w");
    fprintf(log, "123456\t%ld\t%ld\t%f", comparacoes, movimentacoes, tempo);
    fclose(log);

    return 0;
}
