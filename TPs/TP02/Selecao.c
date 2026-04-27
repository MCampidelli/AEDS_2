// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 2
// Questão 3 ----- Ordenação por Seleção em C

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
    Data dataAbertura;
    int aberto;
} Restaurante;

typedef struct {
    int tamanho;
    Restaurante* array[10000];
} Colecao;

Hora parse_hora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

Data parse_data(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

Restaurante* parse_restaurante(char* linha) {

    Restaurante* r = malloc(sizeof(Restaurante));

    char tiposStr[200], precoStr[10], horario[50], data[50], abertoStr[10];

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
        data,
        abertoStr
    );

    // tipos
    r->numTipos = 0;
    char* token = strtok(tiposStr, ";");
    while (token) {
        strcpy(r->tipos[r->numTipos++], token);
        token = strtok(NULL, ";");
    }

    // preço
    r->faixaPreco = strlen(precoStr);

    // horario
    char h1[10], h2[10];
    sscanf(horario, "%[^-]-%s", h1, h2);
    r->abertura = parse_hora(h1);
    r->fechamento = parse_hora(h2);

    // data
    r->dataAbertura = parse_data(data);

    r->aberto = strcmp(abertoStr, "true") == 0;

    return r;
}

void ler_csv(Colecao* c) {

    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    char linha[1000];

    fgets(linha, sizeof(linha), f); 

    c->tamanho = 0;

    while (fgets(linha, sizeof(linha), f)) {
        linha[strcspn(linha, "\n")] = 0;
        c->array[c->tamanho++] = parse_restaurante(linha);
    }

    fclose(f);
}

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
long movimentacoes = 0;

void swap(Restaurante** a, Restaurante** b) {
    Restaurante* tmp = *a;
    *a = *b;
    *b = tmp;
    movimentacoes += 3;
}

void selection_sort(Restaurante* arr[], int n) {

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

// Formatar
void formatar(Restaurante* r, char* buffer) {

    char tipos[300] = "[";
    for (int i = 0; i < r->numTipos; i++) {
        strcat(tipos, r->tipos[i]);
        if (i < r->numTipos - 1) strcat(tipos, ",");
    }
    strcat(tipos, "]");

    char preco[10] = "";
    for (int i = 0; i < r->faixaPreco; i++) strcat(preco, "$");

    char data[20];
    sprintf(data, "%02d/%02d/%04d",
        r->dataAbertura.dia,
        r->dataAbertura.mes,
        r->dataAbertura.ano
    );

    char abertura[10], fechamento[10];
    sprintf(abertura, "%02d:%02d", r->abertura.hora, r->abertura.minuto);
    sprintf(fechamento, "%02d:%02d", r->fechamento.hora, r->fechamento.minuto);

    sprintf(buffer,
        "[%d ## %s ## %s ## %d ## %.1lf ## %s ## %s ## %s-%s ## %s ## %s]",
        r->id,
        r->nome,
        r->cidade,
        r->capacidade,
        r->avaliacao,
        tipos,
        preco,
        abertura,
        fechamento,
        data,
        r->aberto ? "true" : "false"
    );
}

int main() {

    clock_t inicio = clock();

    Colecao c;
    ler_csv(&c);

    Restaurante* selecionados[1000];
    int n = 0;

    int id;

    while (scanf("%d", &id) && id != -1) {
        selecionados[n++] = buscar(&c, id);
    }

    selection_sort(selecionados, n);

    for (int i = 0; i < n; i++) {
        char buffer[1000];
        formatar(selecionados[i], buffer);
        printf("%s\n", buffer);
    }

    clock_t fim = clock();

    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    FILE* log = fopen("matricula_selecao.txt", "w");
    fprintf(log, "123456\t%ld\t%ld\t%f", comparacoes, movimentacoes, tempo);
    fclose(log);

    return 0;
}
