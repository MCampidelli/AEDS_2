// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 02
// Questão 02 ----- Modelagem em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int dia;
    int mes;
    int ano;
} Data;

Data parse_data(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* d, char* buffer) {
    sprintf(buffer, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}

typedef struct {
    int hora;
    int minuto;
} Hora;

Hora parse_hora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* h, char* buffer) {
    sprintf(buffer, "%02d:%02d", h->hora, h->minuto);
}

typedef struct {
    int id;
    char nome[200];
    char cidade[200];
    int capacidade;
    double avaliacao;

    char tiposCozinha[10][100];
    int numTipos;

    int faixaPreco;

    Hora abertura;
    Hora fechamento;

    Data dataAbertura;

    int aberto;
} Restaurante;

// converter $$$ → 3
int converter_preco(char* s) {
    return strlen(s);
}

Restaurante* parse_restaurante(char* linha) {

    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));

    char tipos[200];
    char horario[50];
    char data[50];
    char abertoStr[10];

    sscanf(linha,
        "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^,],%[^,],%s",
        &r->id,
        r->nome,
        r->cidade,
        &r->capacidade,
        &r->avaliacao,
        tipos,
        abertoStr,  
        horario,
        data,
        abertoStr
    );

    // tipos cozinha
    r->numTipos = 0;
    char* token = strtok(tipos, ";");
    while (token != NULL) {
        strcpy(r->tiposCozinha[r->numTipos++], token);
        token = strtok(NULL, ";");
    }

    // faixa preco
    r->faixaPreco = converter_preco(abertoStr);

    // horario
    char h1[10], h2[10];
    sscanf(horario, "%[^-]-%s", h1, h2);
    r->abertura = parse_hora(h1);
    r->fechamento = parse_hora(h2);

    // data
    r->dataAbertura = parse_data(data);

    r->aberto = (strcmp(abertoStr, "true") == 0);

    return r;
}

void formatar_restaurante(Restaurante* r, char* buffer) {

    char tipos[300] = "[";
    for (int i = 0; i < r->numTipos; i++) {
        strcat(tipos, r->tiposCozinha[i]);
        if (i < r->numTipos - 1) strcat(tipos, ",");
    }
    strcat(tipos, "]");

    char preco[10] = "";
    for (int i = 0; i < r->faixaPreco; i++) strcat(preco, "$");

    char abertura[10], fechamento[10], data[20];
    formatar_hora(&r->abertura, abertura);
    formatar_hora(&r->fechamento, fechamento);
    formatar_data(&r->dataAbertura, data);

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

typedef struct {
    int tamanho;
    Restaurante* restaurantes[10000];
} ColecaoRestaurantes;

void ler_csv_colecao(ColecaoRestaurantes* c, char* path) {

    FILE* f = fopen(path, "r");

    char linha[1000];

    fgets(linha, sizeof(linha), f); 

    c->tamanho = 0;

    while (fgets(linha, sizeof(linha), f)) {
        linha[strcspn(linha, "\n")] = 0;
        c->restaurantes[c->tamanho++] = parse_restaurante(linha);
    }

    fclose(f);
}

ColecaoRestaurantes* ler_csv() {
    ColecaoRestaurantes* c = (ColecaoRestaurantes*) malloc(sizeof(ColecaoRestaurantes));
    ler_csv_colecao(c, "/tmp/restaurantes.csv");
    return c;
}

int main() {

    ColecaoRestaurantes* c = ler_csv();

    int id;

    while (scanf("%d", &id) && id != -1) {

        for (int i = 0; i < c->tamanho; i++) {
            if (c->restaurantes[i]->id == id) {

                char buffer[1000];
                formatar_restaurante(c->restaurantes[i], buffer);
                printf("%s\n", buffer);

                break;
            }
        }
    }

    return 0;
}
