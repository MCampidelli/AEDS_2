//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 3
//Questão 4 ----- Ordenação PARCIAL por Heapsort em C

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

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
     char cidade [200];
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

     campos (linhaCSV, &pos, campo);
     r->id = atoi (campo);
     campos(linhaCSV, &pos, r->nome);
     campos(linhaCSV, &pos, r->cidade);
     campos(linhaCSV, &pos, campo);
     r->capacidade = atoi (campo);
     campos(linhaCSV, &pos, campo);
     r->avaliacao = atof (campo);

     campos(linhaCSV, &pos, campo);
     r->qtdCozinhas = split (campo, ';', r->tipos_cozinha);

     campos(linhaCSV, &pos, campo);
     r->faixaPreco = getTamanho(campo);

     campos(linhaCSV, &pos, campo);
     int tracinho = -1;
     for (int i = 0; i < getTamanho(campo) && tracinho == -1; i++) {
          if (campo[i] == '-') {
               tracinho = i;
          }
     }

     char horaAb[6], horaFech[6];
     subcampos(campo, 0, tracinho, horaAb);
     subcampos(campo, tracinho + 1, getTamanho(campo), horaFech);
     r->horarioAbertura   = parseHora(horaAb);
     r->horarioFechamento = parseHora(horaFech);

     campos(linhaCSV, &pos, campo);
     r->dataAbertura = parseData(campo);
     campos(linhaCSV, &pos, campo);
     r->aberto = (strcmp(campo, "true") == 0)? 1 : 0;

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
          if (i > 0) {
               tiposC[posTipos++] = ',';
          }

          for (int j = 0; r->tipos_cozinha[i][j] != '\0'; j++) {
               tiposC[posTipos++] = r->tipos_cozinha[i][j];
          }
     }
     tiposC[posTipos++] = ']';
     tiposC[posTipos] = '\0';

     char preco[6];
     int i;
     for (i = 0; i < r->faixaPreco; i++) {
          preco[i] = '$';
     }
     preco[i] = '\0';

     sprintf(buffer, "[%d ## %s ## %s ## %d ## %.01f ## %s ## %s ## %s-%s ## %s ## %s]",
          r->id, 
          r->nome,
          r->cidade,
          r->capacidade,
          r->avaliacao,
          tiposC,
          preco,
          horaAB,
          horaFECH,          
          data,
          r->aberto? "true" : "false"
     );
}

typedef struct {
     int tamanho;
     Restaurante **restaurantes;
} colecaoRestaurante;

void lerCSV (colecaoRestaurante* colecao, char* path) {
     FILE *arq = fopen (path, "r");
     if (arq == NULL) {
          printf ("Erro ao abrir o arquivo\n");
          return;
     } else { 
          char linha[1024];
          fgets(linha, sizeof(linha), arq); 

          int count = 0;
          while (fgets(linha, sizeof(linha), arq) != NULL) {
               if (linha[0] != '\n' && linha[0] != '\0') {
                    count++;
               }
          }

          fclose(arq);

          colecao->restaurantes = (Restaurante**) malloc (count * sizeof(Restaurante*));
          colecao->tamanho = count;

          arq = fopen (path, "r");
          fgets(linha, sizeof(linha), arq);
          int i = 0;
          int len = 0;
          while (fgets (linha, sizeof(linha), arq) != NULL && i < count) {
               len = getTamanho(linha);
               if (len > 0 && linha[len - 1] == '\n') {
                    linha[len - 1] = '\0';
               }
               if (linha[0] != '\0') {
                    colecao->restaurantes[i++] = parseRestaurante(linha);
               }
          }
          fclose(arq);
     }
}

colecaoRestaurante* lerCsv() {
     colecaoRestaurante *colecao = (colecaoRestaurante*) malloc (sizeof(colecaoRestaurante));
     lerCSV(colecao, "/tmp/restaurantes.csv");
     return colecao;
}

void swap (Restaurante** array, int a, int b) {
     Restaurante* tmp = array[a];
     array[a] = array[b];
     array[b] = tmp;
}

int compara(Restaurante** a, int pos1, Restaurante** b, int pos2) {
     Data data1 = a[pos1]->dataAbertura;
     Data data2 = b[pos2]->dataAbertura;
     int cmp = data1.ano - data2.ano;
     if (cmp == 0) {
          cmp = data1.mes - data2.mes;
          if (cmp == 0) {
               cmp = data1.dia - data2.dia;

               if (cmp == 0) {
                    cmp = strcmp(a[pos1]->nome, b[pos2]->nome);
               }
          }
     }

     return cmp;
}

void construir(Restaurante** array, int n, long* comp, long* mov) {
     int i = n;
     while (i > 1) {
          (*comp)++;
          if (compara(array, i, array, i/2) > 0) {
               swap (array, i, i/2);
               (*mov) += 3;
               i /= 2;
          } else {
               i = 0;
          }
     }
}

int getMaiorFilho(Restaurante** array, int i, int n, long* comp) {
     int filho;
     if (2 * i > n) {
          filho = 2*i;
     } else {
          (*comp)++;
          if (compara(array, 2*i, array, 2*i+1) >= 0) {
               filho = 2*i;
          } else {
               filho = 2*i+1;
          }
     }

     return filho;
}

void reconstruir(Restaurante** array, int n, long* comp, long* mov) {
     int i = 1;
     int filho;

     while (2*i <= (n)) {
          filho = getMaiorFilho(array, i, n, comp);
          if (compara(array, i, array, filho) < 0) {
               (*comp)++;
               swap(array, i, filho);
               (*mov) += 3;
               i = filho;
          } else {
               i = n + 1;
          }
     }
}

void heapsort(Restaurante** array, int n, int k, long* comp, long* mov) {
     Restaurante** tmp = (Restaurante**) malloc ((n + 1) * sizeof(Restaurante*));
     for (int i = 0; i < n; i++) {
          tmp[i + 1] = array[i];
     }

     for (int tamHeap = 2; tamHeap <= k; tamHeap++) {
          construir(tmp, tamHeap, comp, mov);
     }

     for (int i = k + 1; i <= n; i++) {
          (*comp)++;
          if (compara(tmp, i, tmp, 1) < 0) {
               swap(tmp, i, 1);
               (*mov) += 3;
               reconstruir(tmp, k, comp, mov);
          }
     }

     int tamHeap = k;  
     while (tamHeap > 1) {
          swap(tmp, 1, tamHeap--);
          (*mov)+=3;
          reconstruir(tmp,tamHeap, comp, mov);
     }

     for (int i = 0; i < n; i++) {
          array[i] = tmp[i+1];
     }
}

void arqLog (char* matricula, double tempo, long comp, long mov) {
     char nomeArq[100];
     sprintf (nomeArq, "%s_heapsort_parcial.txt", matricula);
     FILE* arqLog = fopen (nomeArq, "w");
     if (arqLog != NULL) {
          fprintf(arqLog, "%s\t%ld\t%ld\t%.2f\n", matricula, comp, mov, tempo);
          fclose(arqLog);
     } else {
          printf ("Erro no arquivo.");
     }
}

int main() {
     colecaoRestaurante* colecao = lerCsv();
    
     char linha[100];
     int id = 0, k = 10;
     char buffer[512];
     long comparacoes = 0, movimentacoes = 0;

     colecaoRestaurante ordenados; 
     ordenados.restaurantes = (Restaurante**) malloc (colecao->tamanho * sizeof(Restaurante*));
     ordenados.tamanho = 0;

    while (scanf("%s", linha) == 1 && (id = atoi(linha)) != -1) {
        for (int i = 0; i < colecao->tamanho; i++) {
            if (colecao->restaurantes[i]->id == id) {
               ordenados.restaurantes[ordenados.tamanho++] = colecao->restaurantes[i];
            }
        }
    }

    clock_t inicio = clock();
    heapsort(ordenados.restaurantes, ordenados.tamanho, k, &comparacoes, &movimentacoes);
    clock_t fim = clock();

    for (int i = 0; i < ordenados.tamanho; i++) {
          formatar_Restaurante(ordenados.restaurantes[i], buffer);
          printf("%s\n", buffer);
    }

    double tempo = (double) (fim - inicio) / CLOCKS_PER_SEC * 1000;
    arqLog("810688", tempo, comparacoes, movimentacoes);

    return 0;
}
