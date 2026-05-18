//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 3
//Questão 02 ----- Ordenação PARCIAL por Inserção em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

typedef struct Data {
    int ano;
    int mes;
    int dia;
} Data;

typedef struct Hora {
    int hora;
    int minuto;
} Hora;

typedef struct Restaurante {
    int id;
    char nome[200];
    char cidade[100];
    int capacidade;
    double avaliacao;

    char tipos_cozinha[20][100];
    int quantidade_tipos;

    int faixa_preco;

    Hora horario_abertura;
    Hora horario_fechamento;

    Data data_abertura;

    int aberto;
} Restaurante;

typedef struct ColecaoRestaurantes {
    Restaurante* restaurantes[10000];
    int tamanho;
} ColecaoRestaurantes;

long comparacoes = 0;
long movimentacoes = 0;

Data parse_data(char* s) {

    Data data;

    sscanf(s, "%d-%d-%d",
           &data.ano,
           &data.mes,
           &data.dia);

    return data;
}

Hora parse_hora(char* s) {

    Hora hora;

    sscanf(s, "%d:%d",
           &hora.hora,
           &hora.minuto);

    return hora;
}

void formatar_data(Data* data, char* buffer) {

    sprintf(buffer,
            "%02d/%02d/%04d",
            data->dia,
            data->mes,
            data->ano);
}

void formatar_hora(Hora* hora, char* buffer) {

    sprintf(buffer,
            "%02d:%02d",
            hora->hora,
            hora->minuto);
}

void separar_csv(char* linha, char campos[][300]) {

    int i = 0;
    int j = 0;
    int campo = 0;
    int aspas = 0;

    while (linha[i] != '\0' && linha[i] != '\n') {

        if (linha[i] == '"') {
            aspas = !aspas;
        }
        else if (linha[i] == ',' && aspas == 0) {

            campos[campo][j] = '\0';

            campo++;
            j = 0;
        }
        else {

            campos[campo][j] = linha[i];
            j++;
        }

        i++;
    }

    campos[campo][j] = '\0';
}

Restaurante* parse_restaurante(char* linha) {

    Restaurante* restaurante =
        (Restaurante*) malloc(sizeof(Restaurante));

    char campos[10][300];

    separar_csv(linha, campos);

    restaurante->id = atoi(campos[0]);

    strcpy(restaurante->nome, campos[1]);

    strcpy(restaurante->cidade, campos[2]);

    restaurante->capacidade = atoi(campos[3]);

    restaurante->avaliacao = atof(campos[4]);

    restaurante->quantidade_tipos = 0;

    int i = 0;
    int j = 0;
    int tipo = 0;

    while (campos[5][i] != '\0') {

        if (campos[5][i] == ';') {

            restaurante->tipos_cozinha[tipo][j] = '\0';

            tipo++;
            j = 0;
        }
        else {

            restaurante->tipos_cozinha[tipo][j] =
                campos[5][i];

            j++;
        }

        i++;
    }

    restaurante->tipos_cozinha[tipo][j] = '\0';

    restaurante->quantidade_tipos = tipo + 1;

    restaurante->faixa_preco = strlen(campos[6]);

    char horario1[20];
    char horario2[20];

    sscanf(campos[7],
           "%[^-]-%s",
           horario1,
           horario2);

    restaurante->horario_abertura =
        parse_hora(horario1);

    restaurante->horario_fechamento =
        parse_hora(horario2);

    restaurante->data_abertura =
        parse_data(campos[8]);

    restaurante->aberto =
        strcmp(campos[9], "true") == 0;

    return restaurante;
}

void formatar_restaurante(Restaurante* restaurante,
                          char* buffer) {

    char data[30];

    char abertura[20];
    char fechamento[20];

    formatar_data(&restaurante->data_abertura,
                  data);

    formatar_hora(&restaurante->horario_abertura,
                  abertura);

    formatar_hora(&restaurante->horario_fechamento,
                  fechamento);

    char tipos[500] = "[";

    for (int i = 0;
         i < restaurante->quantidade_tipos;
         i++) {

        strcat(tipos,
               restaurante->tipos_cozinha[i]);

        if (i <
            restaurante->quantidade_tipos - 1) {

            strcat(tipos, ",");
        }
    }

    strcat(tipos, "]");

    char preco[10] = "";

    for (int i = 0;
         i < restaurante->faixa_preco;
         i++) {

        strcat(preco, "$");
    }

    sprintf(buffer,
            "[%d ## %s ## %s ## %d ## %.1lf ## %s ## %s ## %s-%s ## %s ## %s]",
            restaurante->id,
            restaurante->nome,
            restaurante->cidade,
            restaurante->capacidade,
            restaurante->avaliacao,
            tipos,
            preco,
            abertura,
            fechamento,
            data,
            restaurante->aberto ? "true" : "false");
}

void ler_csv_colecao(ColecaoRestaurantes* colecao,
                     char* path) {

    FILE* arquivo = fopen(path, "r");

    char linha[1000];

    fgets(linha, 1000, arquivo);

    colecao->tamanho = 0;

    while (fgets(linha, 1000, arquivo) != NULL) {

        colecao->restaurantes[colecao->tamanho] =
            parse_restaurante(linha);

        colecao->tamanho++;
    }

    fclose(arquivo);
}

Restaurante* buscar_por_id(ColecaoRestaurantes* colecao,
                           int id) {

    Restaurante* resp = NULL;

    for (int i = 0;
         i < colecao->tamanho;
         i++) {

        if (colecao->restaurantes[i]->id == id) {

            resp = colecao->restaurantes[i];

            i = colecao->tamanho;
        }
    }

    return resp;
}

int comparar(Restaurante* a,
             Restaurante* b) {

    comparacoes++;

    return strcmp(a->cidade,
                  b->cidade);
}

void insercao_parcial(Restaurante* array[],
                      int n,
                      int k) {

    for (int i = 1; i < n; i++) {

        Restaurante* tmp = array[i];

        movimentacoes++;

        int j =
            (i < k) ? i - 1 : k - 1;

        while (j >= 0 &&
               comparar(array[j], tmp) > 0) {

            if (j + 1 < k) {

                array[j + 1] = array[j];

                movimentacoes++;
            }

            j--;
        }

        if (j + 1 < k) {

            array[j + 1] = tmp;

            movimentacoes++;
        }
    }
}

void gerar_log(double tempo) {

    FILE* log =
        fopen("810688_insercao_parcial.txt",
              "w");

    fprintf(log,
            "810688\t%ld\t%ld\t%lf",
            comparacoes,
            movimentacoes,
            tempo);

    fclose(log);
}

int main() {

    clock_t inicio = clock();

    ColecaoRestaurantes colecao;

    ler_csv_colecao(&colecao,
                    "/tmp/restaurantes.csv");

    Restaurante* array[1000];

    int n = 0;

    int id;

    scanf("%d", &id);

    while (id != -1) {

        array[n] =
            buscar_por_id(&colecao, id);

        n++;

        scanf("%d", &id);
    }

    int k = 10;

    if (k > n) {
        k = n;
    }

    insercao_parcial(array, n, k);

    char buffer[1000];

    for (int i = 0; i < n; i++) {

        formatar_restaurante(array[i],
                             buffer);

        printf("%s\n", buffer);
    }

    clock_t fim = clock();

    double tempo =
        ((double)(fim - inicio)) /
        CLOCKS_PER_SEC;

    gerar_log(tempo);

    return 0;
}
