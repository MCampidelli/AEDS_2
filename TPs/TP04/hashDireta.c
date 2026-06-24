// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 04
//  Questão 3 ----- Tabela Hash Direta com Reserva em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define tamTab 31
#define tamReserva 19

int getTamanho (char* s) {
     int tamanho = 0;
     for (int i = 0; *(s + i) != '\0'; i++) {
          tamanho++;
     }
     return tamanho;
}


typedef struct {
     int ano;
     int mes;
     int dia;
} Data;

Data parseData (char* s) {
     Data data;
     sscanf (s, "%d-%d-%d", &data.ano, &data.mes, &data.dia);
     return data;
}

void formatar_Data (Data* d, char* buffer) {
     sprintf (buffer, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}


typedef struct {
     int hora;
     int minuto;
} Hora;

Hora parseHora (char* s) {
     Hora hora;
     sscanf(s, "%d:%d", &hora.hora, &hora.minuto);
     return hora;
}

void formatar_Hora (Hora* h, char* buffer) {
     sprintf (buffer, "%02d:%02d", h->hora, h->minuto);
}

typedef struct {
     int id;
     char nome[100];
     char cidade[200];
     int capacidade;
     double avaliacao;
     char tipos_cozinha[50][50];
     int qtdCozinhas;
     int faixaPreco;
     Hora horarioAbertura;
     Hora horarioFechamento;
     Data dataAbertura;
     int aberto;
} Restaurante;

char* subcampos (char* s, int inicio, int fim, char* campo) {
     for (int i = inicio; i < fim; i++) {
          campo[i - inicio] = *(s + i);
     }
     campo[fim - inicio] = '\0';
}

char* campos (char* linha, int* pos, char* campo) {
     int j = 0;
     while (linha[*pos] != ',' && linha[*pos] != '\0' && linha[*pos] != '\n') {
          campo[j++] = linha[(*pos)++];
     }
     campo[j] = '\0';
     if (linha[*pos] == ',') {
          (*pos)++;
     }
}

int split (char* s, char delimit, char saida[][50]) {
     int count = 0;
     int j = 0;
     for (int i = 0; *(s + i) != '\0'; i++) {
          if (*(s + i) == delimit) {
               saida[count][j] = '\0';
               count++; j = 0;
          } else {
               saida[count][j] = *(s + i);
               j++;
          }
     }
     saida[count][j] = '\0';
     count++;
     return count;
}

Restaurante* parseRestaurante(char* linhaCSV) {
     Restaurante* r = (Restaurante*) malloc (sizeof(Restaurante));
     char campo[200];
     int pos = 0;

     campos(linhaCSV, &pos, campo);
     r->id = atoi(campo);
     campos(linhaCSV, &pos, r->nome);
     campos(linhaCSV, &pos, r->cidade);
     campos(linhaCSV, &pos, campo);
     r->capacidade = atoi(campo);
     campos(linhaCSV, &pos, campo);
     r->avaliacao = atof(campo);
     campos(linhaCSV, &pos, campo);
     r->qtdCozinhas = split(campo, ';', r->tipos_cozinha);
     campos(linhaCSV, &pos, campo);
     r->faixaPreco = getTamanho(campo);

     campos(linhaCSV, &pos, campo);
     int tracinho = -1;
     for (int i = 0; i < getTamanho(campo) && tracinho == -1; i++) {
          if (campo[i] == '-') tracinho = i;
     }
     char horaAb[6], horaFech[6];
     subcampos(campo, 0, tracinho, horaAb);
     subcampos(campo, tracinho + 1, getTamanho(campo), horaFech);
     r->horarioAbertura   = parseHora(horaAb);
     r->horarioFechamento = parseHora(horaFech);

     campos(linhaCSV, &pos, campo);
     r->dataAbertura = parseData(campo);
     campos(linhaCSV, &pos, campo);
     r->aberto = (strcmp(campo, "true") == 0) ? 1 : 0;

     return r;
}

char* formatar_Restaurante(Restaurante* r, char* buffer) {
     char horaAB[6], horaFECH[6];
     char data[11];

     formatar_Hora(&r->horarioAbertura, horaAB);
     formatar_Hora(&r->horarioFechamento, horaFECH);
     formatar_Data(&r->dataAbertura, data);

     char tiposC[200];
     int posTipos = 0;
     tiposC[posTipos++] = '[';
     for (int i = 0; i < r->qtdCozinhas; i++) {
          if (i > 0) tiposC[posTipos++] = ',';
          for (int j = 0; r->tipos_cozinha[i][j] != '\0'; j++)
               tiposC[posTipos++] = r->tipos_cozinha[i][j];
     }
     tiposC[posTipos++] = ']';
     tiposC[posTipos] = '\0';

     char preco[6];
     int i;
     for (i = 0; i < r->faixaPreco; i++) preco[i] = '$';
     preco[i] = '\0';

     sprintf(buffer, "[%d ## %s ## %s ## %d ## %.01f ## %s ## %s ## %s-%s ## %s ## %s]",
          r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
          tiposC, preco, horaAB, horaFECH, data,
          r->aberto ? "true" : "false");
}

typedef struct {
     int tamanho;
     Restaurante **restaurantes;
} colecaoRestaurante;

void lerCSV (colecaoRestaurante* colecao, char* path) {
     FILE *arq = fopen(path, "r");
     if (arq == NULL) {
          printf("Erro ao abrir o arquivo\n");
          return;
     }
     char linha[1024];
     fgets(linha, sizeof(linha), arq);

     int count = 0;
     while (fgets(linha, sizeof(linha), arq) != NULL) {
          if (linha[0] != '\n' && linha[0] != '\0') count++;
     }
     fclose(arq);

     colecao->restaurantes = (Restaurante**) malloc(count * sizeof(Restaurante*));
     colecao->tamanho = count;

     arq = fopen(path, "r");
     fgets(linha, sizeof(linha), arq);
     int i = 0, len = 0;
     while (fgets(linha, sizeof(linha), arq) != NULL && i < count) {
          len = getTamanho(linha);
          if (len > 0 && linha[len - 1] == '\n') linha[len - 1] = '\0';
          if (linha[0] != '\0') colecao->restaurantes[i++] = parseRestaurante(linha);
     }
     fclose(arq);
}

colecaoRestaurante* lerCsv() {
     colecaoRestaurante *colecao = (colecaoRestaurante*) malloc(sizeof(colecaoRestaurante));
     lerCSV(colecao, "/tmp/restaurantes.csv");
     return colecao;
}


typedef struct {
     Restaurante* tabela[tamTab + tamReserva];
} Hash;

int hash(char* nome) {
     int soma = 0;
     for (int i = 0; i < getTamanho(nome); i++) {
          soma += (int)nome[i];
     }

     return soma % tamTab;
}

void inserirHash(Restaurante* r, Hash* h) {
     int tamNome = getTamanho (r->nome);
     int pos = hash(r->nome);

     if (h->tabela[pos] == NULL) {
          h->tabela[pos] = r;
     } else {
          int reserva = tamTab; //começa da pos 31
          while (reserva < tamTab + tamReserva && h->tabela[reserva] != NULL) {
               reserva++;
          }
          if (reserva < tamTab + tamReserva) {
               h->tabela[reserva] = r;
          } else {
               printf("%s\n", r->nome);
          }
     }
}

int pesquisaHash(char* nome, long* comp, Hash* h) {
     int tamNome = getTamanho(nome);
     int pos = hash(nome);

     (*comp)++;
     if (h->tabela[pos] == NULL) {
          return -1;
     } else if (strcmp(h->tabela[pos]->nome, nome) == 0) { //encontrou na area principal
          return pos;
     } else { //procura na area reserva
          for (int i = tamTab; i < tamTab + tamReserva; i++) {
               (*comp)++;
               if (h->tabela[i] != NULL && strcmp(h->tabela[i]->nome, nome) == 0) {
                    return i;
               }
          }
     }

     return -1;
}

void arqLog(char* matricula, double tempo, long comp) {
     char nomeArq[100];
     sprintf(nomeArq, "%s_hash_reserva.txt", matricula);
     FILE* arq = fopen(nomeArq, "w");
     if (arq != NULL) {
          fprintf(arq, "%s\t%ld\t%.2f\n", matricula, comp, tempo);
          fclose(arq);
     } else {
          printf("Erro no arquivo.");
     }
}


int main() {
     colecaoRestaurante* colecao = lerCsv();

     char linha[100];
     int id = 0;
     char buffer[512];
     long comparacoes = 0;

     Hash* h = (Hash*) malloc (sizeof(Hash));
     for (int i = 0; i < tamTab + tamReserva; i++) {
          h->tabela[i] = NULL;
     }

     while (scanf(" %[^\n]", linha) == 1 && (id = atoi(linha)) != -1) {
          int len = getTamanho(linha);
          if (len > 0 && linha[len - 1] == '\r') {
               linha[len - 1] = '\0';
          }

          for (int i = 0; i < colecao->tamanho; i++) {
               if (colecao->restaurantes[i]->id == id) {
                    inserirHash(colecao->restaurantes[i], h);
               }
          }
     }

     clock_t inicio = clock();
     char* nomeR = (char*) malloc (200 * sizeof(char));
     while (scanf(" %[^\n]", nomeR) == 1 && strcmp (nomeR, "FIM") != 0) {
          int len = getTamanho(nomeR);
          if (len > 0 && nomeR[len - 1] == '\r') {
               nomeR[len - 1] = '\0'; 
          }

          int pos = pesquisaHash(nomeR, &comparacoes, h);
          if (pos != -1) {
               formatar_Restaurante(h->tabela[pos], buffer);
               printf("%d %s\n", pos, buffer);
          } else {
               printf("-1\n");
          }
     }
     clock_t fim = clock();

     double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000;
     arqLog("810688", tempo, comparacoes);

     return 0;
}
