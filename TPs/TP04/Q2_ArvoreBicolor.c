#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// ==================== ESTRUTURAS DE DADOS ====================

typedef struct {
    int ano;
    int mes;
    int dia;
} Data;

typedef struct {
    int hora;
    int minuto;
} Hora;

typedef struct {
    int id;
    char nome[100];
    char cidade[100];
    int capacidade;
    double avaliacao;
    char tiposCozinha[10][50];
    int numTipos;
    int faixaPreco;
    Hora horarioAbertura;
    Hora horarioFechamento;
    Data dataAbertura;
    int aberto;
} Restaurante;

typedef struct {
    Restaurante *restaurantes;
    int tamanho;
} ColecaoRestaurantes;

// Cores para a árvore Bicolor (Red-Black Tree)
typedef enum {
    VERMELHO = 0,
    PRETO = 1
} Cor;

/**
 * Nó da árvore Bicolor.
 * Cada nó possui uma cor (vermelho ou preto) além dos dados
 * de um nó de árvore binária comum.
 */
typedef struct No {
    Restaurante *elemento;
    struct No *esq;
    struct No *dir;
    struct No *pai;        // ponteiro para o pai, necessário para rotações
    Cor cor;               // VERMELHO ou PRETO
} No;

/**
 * Árvore Bicolor (Red-Black Tree).
 * Mantém a raiz e contadores para análise de desempenho.
 */
typedef struct {
    No *raiz;
    long comparacoes;
} ArvoreBicolor;

// ==================== FUNÇÕES AUXILIARES ====================

/**
 * Converte string YYYY-MM-DD em Data
 */
Data parseData(char *s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

/**
 * Converte string HH:MM em Hora
 */
Hora parseHora(char *s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

/**
 * Formata Data em DD/MM/YYYY
 */
void formatarData(Data *d, char *buffer) {
    sprintf(buffer, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}

/**
 * Formata Hora em HH:MM
 */
void formatarHora(Hora *h, char *buffer) {
    sprintf(buffer, "%02d:%02d", h->hora, h->minuto);
}

/**
 * Converte uma linha CSV em Restaurante
 */
Restaurante *parseRestaurante(char *s) {
    Restaurante *r = (Restaurante *)malloc(sizeof(Restaurante));
    char tiposCozinha_str[500];
    char faixaPreco_str[20];
    char horarioAbertura_str[20];
    char horarioFechamento_str[20];
    char data_str[20];
    char aberto_str[10];

    sscanf(s, "%d,%99[^,],%99[^,],%d,%lf,%499[^,],%19[^,],%19[^-]-%19[^,],%19[^,],%9s",
           &r->id, r->nome, r->cidade, &r->capacidade, &r->avaliacao,
           tiposCozinha_str, faixaPreco_str, horarioAbertura_str, horarioFechamento_str, data_str, aberto_str);

    r->numTipos = 0;
    int pos = 0;
    int inicio = 0;
    while (tiposCozinha_str[pos] != '\0') {
        if (tiposCozinha_str[pos] == ';') {
            int j = 0;
            int k = inicio;
            while (k < pos && j < 49) {
                r->tiposCozinha[r->numTipos][j] = tiposCozinha_str[k];
                j++;
                k++;
            }
            r->tiposCozinha[r->numTipos][j] = '\0';
            r->numTipos++;
            inicio = pos + 1;
        }
        pos++;
    }
    {
        int j = 0;
        int k = inicio;
        while (tiposCozinha_str[k] != '\0' && j < 49) {
            r->tiposCozinha[r->numTipos][j] = tiposCozinha_str[k];
            j++;
            k++;
        }
        r->tiposCozinha[r->numTipos][j] = '\0';
        r->numTipos++;
    }

    r->faixaPreco = 0;
    for (pos = 0; faixaPreco_str[pos] != '\0'; pos++) {
        r->faixaPreco++;
    }
    r->horarioAbertura = parseHora(horarioAbertura_str);
    r->horarioFechamento = parseHora(horarioFechamento_str);
    r->dataAbertura = parseData(data_str);
    r->aberto = (strcmp(aberto_str, "true") == 0);

    return r;
}

/**
 * Formata Restaurante conforme padrão da disciplina
 */
void formatarRestaurante(Restaurante *r, char *buffer) {
    char tipos_str[500];
    int idx = 0;
    int i;

    tipos_str[idx++] = '[';
    for (i = 0; i < r->numTipos; i++) {
        int j = 0;
        while (r->tiposCozinha[i][j] != '\0') {
            tipos_str[idx++] = r->tiposCozinha[i][j];
            j++;
        }
        if (i < r->numTipos - 1) {
            tipos_str[idx++] = ',';
        }
    }
    tipos_str[idx++] = ']';
    tipos_str[idx] = '\0';

    char preco_str[20];
    idx = 0;
    for (i = 0; i < r->faixaPreco; i++) {
        preco_str[idx++] = '$';
    }
    preco_str[idx] = '\0';

    char horarioAbertura_str[20];
    char horarioFechamento_str[20];
    char data_str[20];
    formatarHora(&r->horarioAbertura, horarioAbertura_str);
    formatarHora(&r->horarioFechamento, horarioFechamento_str);
    formatarData(&r->dataAbertura, data_str);

    sprintf(buffer, "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
            tipos_str, preco_str,
            horarioAbertura_str, horarioFechamento_str,
            data_str, r->aberto ? "true" : "false");
}

/**
 * Mede o tamanho de uma string
 */
int tamanhoString(char *s) {
    int tam = 0;
    while (s[tam] != '\0') {
        tam++;
    }
    return tam;
}

/**
 * Conta número de linhas do arquivo CSV
 */
int numLinhasArq(char *path) {
    FILE *arq = fopen(path, "r");
    char linha[1024];
    int count = 0;

    if (arq == NULL) {
        return 0;
    }

    if (fgets(linha, sizeof(linha), arq) != NULL) {
        while (fgets(linha, sizeof(linha), arq) != NULL) {
            if (tamanhoString(linha) > 1) {
                count++;
            }
        }
    }

    fclose(arq);
    return count;
}

/**
 * Lê o arquivo CSV e retorna coleção de restaurantes
 */
ColecaoRestaurantes *leituraArq() {
    char path[] = "/tmp/restaurantes.csv";
    int total = numLinhasArq(path);
    ColecaoRestaurantes *colecao = (ColecaoRestaurantes *)malloc(sizeof(ColecaoRestaurantes));
    FILE *arq = fopen(path, "r");
    char linha[1024];
    int index = 0;

    if (colecao == NULL || arq == NULL) {
        return NULL;
    }

    colecao->restaurantes = (Restaurante *)malloc(total * sizeof(Restaurante));
    colecao->tamanho = total;

    if (fgets(linha, sizeof(linha), arq) != NULL) {
        while (fgets(linha, sizeof(linha), arq) != NULL && index < total) {
            if (tamanhoString(linha) > 1) {
                Restaurante *r = parseRestaurante(linha);
                colecao->restaurantes[index++] = *r;
                free(r);
            }
        }
    }

    fclose(arq);
    return colecao;
}

/**
 * Busca um restaurante pelo ID na coleção
 */
Restaurante *buscaRestaurante(ColecaoRestaurantes *colecao, int id) {
    int i = 0;
    while (i < colecao->tamanho && colecao->restaurantes[i].id != id) {
        i++;
    }
    if (i < colecao->tamanho) {
        return &colecao->restaurantes[i];
    }
    return NULL;
}

/**
 * Compara dois nomes lexicograficamente
 */
int compararNomes(char *a, char *b) {
    return strcmp(a, b);
}

/**
 * Limpa newline de linha lida com fgets
 */
void limparLinha(char *linha) {
    int i = 0;
    while (linha[i] != '\0') {
        if (linha[i] == '\n' || linha[i] == '\r') {
            linha[i] = '\0';
            return;
        }
        i++;
    }
}

/**
 * Converte string para inteiro
 */
int stringToInt(char *s) {
    int resultado = 0;
    int i = 0;
    while (s[i] != '\0') {
        if (s[i] >= '0' && s[i] <= '9') {
            resultado = resultado * 10 + (s[i] - '0');
        }
        i++;
    }
    return resultado;
}

/**
 * Verifica se a string é "-1"
 */
int ehMenosUm(char *s) {
    return (s[0] == '-' && s[1] == '1' && s[2] == '\0');
}

/**
 * Verifica se a string é "FIM"
 */
int ehFIM(char *s) {
    return (s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0');
}

// ==================== FUNÇÕES DA ÁRVORE BICOLOR ====================

/**
 * Cria uma árvore bicolor vazia
 */
ArvoreBicolor *criarArvore() {
    ArvoreBicolor *arvore = (ArvoreBicolor *)malloc(sizeof(ArvoreBicolor));
    arvore->raiz = NULL;
    arvore->comparacoes = 0;
    return arvore;
}

/**
 * Cria um novo nó com cor VERMELHO (padrão para inserção)
 */
No *criarNo(Restaurante *elemento) {
    No *novo = (No *)malloc(sizeof(No));
    novo->elemento = elemento;
    novo->esq = NULL;
    novo->dir = NULL;
    novo->pai = NULL;
    novo->cor = VERMELHO;  // Novos nós sempre começam vermelhos
    return novo;
}

/**
 * Obtém a cor de um nó (NULL é considerado PRETO)
 */
Cor obterCor(No *no) {
    if (no == NULL) {
        return PRETO;
    }
    return no->cor;
}

/**
 * Rotação à esquerda
 * Reorganiza a árvore mantendo a propriedade de busca binária
 */
No *rotacaoEsquerda(No *no) {
    No *filhoDireito = no->dir;
    no->dir = filhoDireito->esq;
    
    if (filhoDireito->esq != NULL) {
        filhoDireito->esq->pai = no;
    }
    
    filhoDireito->pai = no->pai;
    filhoDireito->esq = no;
    no->pai = filhoDireito;
    
    return filhoDireito;
}

/**
 * Rotação à direita
 * Reorganiza a árvore mantendo a propriedade de busca binária
 */
No *rotacaoDireita(No *no) {
    No *filhoEsquerdo = no->esq;
    no->esq = filhoEsquerdo->dir;
    
    if (filhoEsquerdo->dir != NULL) {
        filhoEsquerdo->dir->pai = no;
    }
    
    filhoEsquerdo->pai = no->pai;
    filhoEsquerdo->dir = no;
    no->pai = filhoEsquerdo;
    
    return filhoEsquerdo;
}

/**
 * Rebalanceia a árvore após inserção
 * Mantém as propriedades Red-Black
 */
No *rebalancearAposPosInser(No *arvore, No *no) {
    while (no != NULL && no->pai != NULL && no->pai->cor == VERMELHO) {
        if (no->pai == no->pai->pai->esq) {
            No *tio = no->pai->pai->dir;
            
            if (obterCor(tio) == VERMELHO) {
                // Caso 1: Tio é vermelho
                no->pai->cor = PRETO;
                tio->cor = PRETO;
                no->pai->pai->cor = VERMELHO;
                no = no->pai->pai;
            } else {
                // Caso 2/3: Tio é preto
                if (no == no->pai->dir) {
                    // Caso 2: Nó é filho direito
                    no = no->pai;
                    arvore = rotacaoEsquerda(no);
                }
                // Caso 3: Nó é filho esquerdo
                no->pai->cor = PRETO;
                no->pai->pai->cor = VERMELHO;
                arvore = rotacaoDireita(no->pai->pai);
            }
        } else {
            No *tio = no->pai->pai->esq;
            
            if (obterCor(tio) == VERMELHO) {
                no->pai->cor = PRETO;
                tio->cor = PRETO;
                no->pai->pai->cor = VERMELHO;
                no = no->pai->pai;
            } else {
                if (no == no->pai->esq) {
                    no = no->pai;
                    arvore = rotacaoDireita(no);
                }
                no->pai->cor = PRETO;
                no->pai->pai->cor = VERMELHO;
                arvore = rotacaoEsquerda(no->pai->pai);
            }
        }
    }
    return arvore;
}

/**
 * Insere um restaurante na árvore bicolor (wrapper)
 */
No *inserirInterno(No *no, Restaurante *elemento, long *comparacoes, No *pai) {
    if (no == NULL) {
        No *novo = criarNo(elemento);
        novo->pai = pai;
        return novo;
    }
    
    (*comparacoes)++;
    int cmp = compararNomes(elemento->nome, no->elemento->nome);
    
    if (cmp < 0) {
        no->esq = inserirInterno(no->esq, elemento, comparacoes, no);
    } else if (cmp > 0) {
        no->dir = inserirInterno(no->dir, elemento, comparacoes, no);
    }
    
    return no;
}

/**
 * Insere um elemento na árvore bicolor e rebalanceia
 */
void inserirArvore(ArvoreBicolor *arvore, Restaurante *elemento) {
    arvore->raiz = inserirInterno(arvore->raiz, elemento, &arvore->comparacoes, NULL);
    arvore->raiz = rebalancearAposPosInser(arvore->raiz, arvore->raiz);
    arvore->raiz->cor = PRETO;  // Raiz sempre preta
}

/**
 * Pesquisa um elemento na árvore, imprimindo caminho
 */
int pesquisar(No *no, char *nome, long *comparacoes) {
    int resp = 0;
    if (no == NULL) {
        resp = 0;
    } else {
        (*comparacoes)++;
        int cmp = compararNomes(nome, no->elemento->nome);
        if (cmp == 0) {
            resp = 1;
        } else if (cmp < 0) {
            printf("esq ");
            resp = pesquisar(no->esq, nome, comparacoes);
        } else {
            printf("dir ");
            resp = pesquisar(no->dir, nome, comparacoes);
        }
    }
    return resp;
}

/**
 * Conta o número de nós na árvore
 */
int contarNos(No *no) {
    int total = 0;
    if (no != NULL) {
        total = 1 + contarNos(no->esq) + contarNos(no->dir);
    }
    return total;
}

/**
 * Exibe a árvore em ordem (esquerda-raiz-direita)
 */
void mostrarEmOrdem(No *no, int total, int *exibidos) {
    if (no != NULL) {
        mostrarEmOrdem(no->esq, total, exibidos);
        (*exibidos)++;
        {
            char buffer[1024];
            formatarRestaurante(no->elemento, buffer);
            if (*exibidos < total) {
                printf("%s\n", buffer);
            } else {
                printf("%s", buffer);
            }
        }
        mostrarEmOrdem(no->dir, total, exibidos);
    }
}

// ==================== FUNÇÃO MAIN ====================

int main() {
    ColecaoRestaurantes *colecao = leituraArq();
    ArvoreBicolor *arvore = criarArvore();
    char linha[200];
    int id;

    if (colecao == NULL || arvore == NULL) {
        return 0;
    }

    // Leitura da primeira parte: IDs dos restaurantes a inserir
    if (fgets(linha, sizeof(linha), stdin) != NULL) {
        limparLinha(linha);
        while (!ehMenosUm(linha)) {
            id = stringToInt(linha);
            Restaurante *restaurante = buscaRestaurante(colecao, id);
            if (restaurante != NULL) {
                inserirArvore(arvore, restaurante);
            }
            if (fgets(linha, sizeof(linha), stdin) == NULL) {
                linha[0] = '\0';
                break;
            }
            limparLinha(linha);
        }
    }

    // Leitura da segunda parte: nomes a pesquisar
    if (fgets(linha, sizeof(linha), stdin) != NULL) {
        limparLinha(linha);
        while (!ehFIM(linha)) {
            printf("raiz ");
            if (pesquisar(arvore->raiz, linha, &arvore->comparacoes)) {
                printf("SIM\n");
            } else {
                printf("NAO\n");
            }
            if (fgets(linha, sizeof(linha), stdin) == NULL) {
                linha[0] = '\0';
                break;
            }
            limparLinha(linha);
        }
    }

    // Exibição em ordem
    {
        int total = contarNos(arvore->raiz);
        int exibidos = 0;
        mostrarEmOrdem(arvore->raiz, total, &exibidos);
    }

    // Escrita do arquivo de log
    {
        FILE *log = fopen("810688_arvore_bicolor.txt", "w");
        double tempo = 0.0;
        fprintf(log, "810688\t%ld\t%ld\t%lf\n", arvore->comparacoes, 0L, tempo);
        fclose(log);
    }

    return 0;
}
