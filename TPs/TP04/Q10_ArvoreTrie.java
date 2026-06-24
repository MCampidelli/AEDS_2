import java.util.Scanner;
import java.io.FileWriter;

class Data {
    private int ano;
    private int mes;
    private int dia;

    private Data() {
        this.ano = 0;
        this.mes = 0;
        this.dia = 0;
    }

    public int getAno() {
        return ano;
    }
    public int getMes() {
        return mes;
    }
    public int getDia() {
        return dia;
    }

    public static Data parseData(String s) {
       int ano = stringToInt(s, 0, 4);
       int mes = stringToInt(s, 5, 7);
       int dia = stringToInt(s, 8, 10);
       Data d = new Data();
       d.ano = ano;
       d.mes = mes;
       d.dia = dia;
        return d;
    }

    private static int stringToInt(String s, int inicio, int fim) {
        int resp = 0;
        for (int i = inicio; i < fim; i++) {
            resp = resp * 10 + (s.charAt(i) - '0');
        }
        return resp;
    }

    public String formatarData() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {
    private int hora;
    private int minuto;

    private Hora() {
        this.hora = 0;
        this.minuto = 0;
    }

    public int getHora() {
        return hora;
    }
    public int getMinuto() {
        return minuto;
    }

    public static Hora parseHora(String s) {
        int hora = stringToInt(s, 0, 2);
        int minuto = stringToInt(s, 3, 5);
        Hora h = new Hora();
        h.hora = hora;
        h.minuto = minuto;
        return h;
    }

     private static int stringToInt(String s, int inicio, int fim) {
        int resp = 0;
        for (int i = inicio; i < fim; i++) {
            resp = resp * 10 + (s.charAt(i) - '0');
        }
        return resp;
    }

    public String formatarHora() {
        return String.format("%02d:%02d", hora, minuto);
    }
}

class Restaurante{
    private int id;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixaPreco;
    private Hora horarioAbertura;
    private Hora horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;

    private Restaurante(int id, String nome, String cidade, int capacidade,
                        double avaliacao, String[] tiposCozinha, int faixaPreco,
                        Hora horarioAbertura, Hora horarioFechamento,
                        Data dataAbertura, boolean aberto) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha;
        this.faixaPreco = faixaPreco;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.dataAbertura = dataAbertura;
        this.aberto = aberto;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getCidade() {
        return cidade;
    }
    public int getCapacidade() {
        return capacidade;
    }
    public double getAvaliacao() {
        return avaliacao;
    }
    public String[] getTiposCozinha() {
        return tiposCozinha;
    }
    public int getNumTipos() {
        return tiposCozinha.length;
    }
    public int getFaixaPreco() {
        return faixaPreco;
    }
    public Hora getHorarioAbertura() {
        return horarioAbertura;
    }
    public Hora getHorarioFechamento() {
        return horarioFechamento;
    }
    public Data getDataAbertura() {
        return dataAbertura;
    }
    public boolean isAberto() {
        return aberto;
    }

    private static int stringToInt(String s) {
        int resp = 0;
        for (int i = 0; i < s.length(); i++) {
            resp = resp * 10 + (s.charAt(i) - '0');
        }
        return resp;
    }

    private static double stringToDouble(String s) {
        double resp = 0;
        int i = 0;
        while (i < s.length() && s.charAt(i) != '.') {
            resp = resp * 10 + (s.charAt(i) - '0');
            i++;
        }
        if (i < s.length() && s.charAt(i) == '.') {
            i++;
            double divisor = 1;
            while (i < s.length()) {
                resp = resp * 10 + (s.charAt(i) - '0');
                divisor *= 10;
                i++;
            }
            resp /= divisor;
        }
        return resp;
    }

    private static String[] stringPartes(String s, char separador) {
        int count = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == separador){
                count++;
            }
        }
        String[] partes = new String[count];
        int index = 0;
        int ini = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == separador) {
                partes[index++] = s.substring(ini, i);
                ini = i + 1;
            }
        }
        partes[index] = s.substring(ini);
        return partes;
    }

    public static Restaurante parseRestaurante(String s) {
        if (s == null || s.length() == 0) return null;

        String[] campos = new String[10];
        int tmp = 0;
        int in = 0;

        for (int i = 0; i <= s.length(); i++) {
            if (i == s.length() || (s.charAt(i) == ',' && tmp < 9)) {
                campos[tmp++] = s.substring(in, i);
                in = i + 1;
            }
        }

        if (tmp < 10) return null;
        
        int id = stringToInt(campos[0]);
        String nome = campos[1];
        String cidade = campos[2];
        int capacidade = stringToInt(campos[3]);
        double avaliacao = stringToDouble(campos[4]);
        String[] tiposCozinha = stringPartes(campos[5], ';');
        int faixaPreco = campos[6].length();
        if (campos[7].length() < 11) return null;
        Hora horarioAbertura = Hora.parseHora(campos[7].substring(0, 5));
        Hora horarioFechamento = Hora.parseHora(campos[7].substring(6, 11));
        Data dataAbertura = Data.parseData(campos[8]);
        boolean aberto = campos[9].charAt(0) == 't';

         Restaurante r = new Restaurante(id, nome, cidade, capacidade, avaliacao,
                                        tiposCozinha, faixaPreco, horarioAbertura,
                                        horarioFechamento, dataAbertura, aberto);

        return r;
    }

    public String formataRestaurante() {
        String strTipos = "[";
        for (int i = 0; i < tiposCozinha.length; i++) {
            strTipos += tiposCozinha[i];
            if (i < tiposCozinha.length - 1) {
                strTipos += ",";
            }
        }
        strTipos += "]";

        String preco = "";
        for (int i = 0; i < faixaPreco; i++) {
            preco += "$";
        }

        return String.format("[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %s]",
                id, nome, cidade, capacidade, avaliacao,
                strTipos, preco,
                horarioAbertura.formatarHora(), horarioFechamento.formatarHora(),
                dataAbertura.formatarData(),
                aberto ? "true" : "false");
    }
}

class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;

    private ColecaoRestaurantes() {
        this.tamanho = 0;
        this.restaurantes = null;
    }

    public int getTamanho() {
        return tamanho;
    }
    public Restaurante[] getRestaurantes() {
        return restaurantes;
    }

    public void leituraArquivo(String path) throws Exception {
        Scanner sc = new Scanner(new java.io.File(path));
        sc.nextLine();
        int count = 0;
        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            if (linha.length() > 0) count++;
        }
        sc.close();

        tamanho = count;
        restaurantes = new Restaurante[tamanho];

        Scanner sc2 = new Scanner(new java.io.File(path));
        sc2.nextLine();
        int i = 0;
        while (sc2.hasNextLine() && i < tamanho) {
            String linha = sc2.nextLine();
            if (linha != null && linha.length() > 0) {
                restaurantes[i++] = Restaurante.parseRestaurante(linha);
            }
        }
        sc2.close();
    }

    public static ColecaoRestaurantes lerArquivo() throws Exception {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        c.leituraArquivo("/tmp/restaurantes.csv");
        return c;
    }
}

/**
 * No de uma Árvore Binária de Pesquisa que armazena caracteres.
 * Usado para manter os filhos de um No da Trie em ordem.
 */
class NoABP {
    public char caractere;
    public NoTrie nodeTrieFilho;
    public NoABP esq;
    public NoABP dir;

    public NoABP(char caractere, NoTrie nodeTrieFilho) {
        this.caractere = caractere;
        this.nodeTrieFilho = nodeTrieFilho;
        this.esq = null;
        this.dir = null;
    }
}

/**
 * No da Árvore Trie.
 * Cada No representa um ponto no caminho de uma palavra.
 * Os filhos são organizados em uma Árvore Binária de Pesquisa.
 */
class NoTrie {
    public char caractere;
    public NoABP raizFilhos;    // Raiz da ABP de filhos
    public Restaurante restaurante;  // null se não é final de palavra
    public boolean ehFimPalavra;

    public NoTrie(char caractere) {
        this.caractere = caractere;
        this.raizFilhos = null;
        this.restaurante = null;
        this.ehFimPalavra = false;
    }
}

/**
 * Árvore Trie com Árvore Binária.
 * Cada No da Trie tem seus filhos organizados em uma ABP.
 */
class ArvoreTrie {
    private NoTrie raiz;
    private long comparacoes;

    /**
     * Cria uma Trie vazia.
     */
    public ArvoreTrie() {
        this.raiz = new NoTrie('\0');  // No raiz com caractere nulo
        this.comparacoes = 0;
    }

    /**
     * Insere uma palavra na Trie, associando um Restaurante.
     */
    public void inserir(String palavra, Restaurante restaurante) {
        NoTrie nodeAtual = raiz;

        // Navega/cria caminho para cada caractere
        for (int i = 0; i < palavra.length(); i++) {
            char caractere = palavra.charAt(i);
            NoABP nodeFilho = encontrarOuCriarFilho(nodeAtual, caractere);
            comparacoes++;
            nodeAtual = nodeFilho.nodeTrieFilho;
        }

        // Marca como fim de palavra e armazena restaurante
        nodeAtual.ehFimPalavra = true;
        nodeAtual.restaurante = restaurante;
    }

    /**
     * Encontra ou cria um filho na ABP de um No da Trie.
     */
    private NoABP encontrarOuCriarFilho(NoTrie node, char caractere) {
        if (node.raizFilhos == null) {
            node.raizFilhos = new NoABP(caractere, new NoTrie(caractere));
            return node.raizFilhos;
        }

        NoABP atual = node.raizFilhos;
        while (true) {
            if (caractere == atual.caractere) {
                return atual;
            } else if (caractere < atual.caractere) {
                if (atual.esq == null) {
                    atual.esq = new NoABP(caractere, new NoTrie(caractere));
                    return atual.esq;
                }
                atual = atual.esq;
            } else {
                if (atual.dir == null) {
                    atual.dir = new NoABP(caractere, new NoTrie(caractere));
                    return atual.dir;
                }
                atual = atual.dir;
            }
        }
    }

    /**
     * Pesquisa uma palavra na Trie, imprimindo os caracteres visitados.
     * Retorna o Restaurante se encontrado, null caso contrário.
     */
    public Restaurante pesquisar(String palavra) {
        NoTrie nodeAtual = raiz;

        // Navega pela Trie, imprimindo caracteres
        for (int i = 0; i < palavra.length(); i++) {
            char caractere = palavra.charAt(i);
            NoABP nodeFilho = procurarFilho(nodeAtual, caractere);
            comparacoes++;

            if (nodeFilho == null) {
                // Caractere não encontrado, palavra não existe
                return null;
            }

            System.out.print(caractere + " ");
            nodeAtual = nodeFilho.nodeTrieFilho;
        }

        // Verifica se é final de palavra
        if (nodeAtual.ehFimPalavra) {
            return nodeAtual.restaurante;
        }

        return null;
    }

    /**
     * Procura um caractere na ABP de um No da Trie.
     * Retorna o NoABP se encontrado, null caso contrário.
     */
    private NoABP procurarFilho(NoTrie node, char caractere) {
        NoABP atual = node.raizFilhos;

        while (atual != null) {
            if (caractere == atual.caractere) {
                return atual;
            } else if (caractere < atual.caractere) {
                atual = atual.esq;
            } else {
                atual = atual.dir;
            }
        }

        return null;
    }

    /**
     * Retorna o número total de comparações realizadas.
     */
    public long getComparacoes() {
        return comparacoes;
    }

    /**
     * Exibe todas as palavras da Trie em ordem (caminhamento em ordem da ABP).
     */
    public void mostrarEmOrdem() {
        mostrarEmOrdemInterno(raiz.raizFilhos);
    }

    /**
     * Método auxiliar para caminhamento em ordem.
     */
    private void mostrarEmOrdemInterno(NoABP node) {
        if (node != null) {
            // Esquerda
            mostrarEmOrdemInterno(node.esq);

            // Raiz (se for fim de palavra)
            NoTrie trie = node.nodeTrieFilho;
            if (trie.ehFimPalavra && trie.restaurante != null) {
                System.out.println(trie.restaurante.formataRestaurante());
            }

            // Direita
            mostrarEmOrdemInterno(node.dir);
        }
    }
}

/**
 * Classe principal que executa o programa.
 * Lê restaurantes, insere na Trie, pesquisa e exibe resultados.
 */
public class Q10_ArvoreTrie {

    /**
     * Busca um restaurante na coleção pelo ID.
     */
    static Restaurante buscarPorId(ColecaoRestaurantes colecao, int id) {
        Restaurante[] restaurantes = colecao.getRestaurantes();
        int tamanho = colecao.getTamanho();
        for (int i = 0; i < tamanho; i++) {
            if (restaurantes[i].getId() == id) {
                return restaurantes[i];
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        java.util.Locale.setDefault(java.util.Locale.US);

        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerArquivo();
        ArvoreTrie trie = new ArvoreTrie();

        Scanner sc = new Scanner(System.in);

        // Parte 1: Lê IDs e insere restaurantes na Trie
        int id = sc.nextInt();
        while (id != -1) {
            Restaurante restaurante = buscarPorId(colecao, id);
            if (restaurante != null) {
                trie.inserir(restaurante.getNome(), restaurante);
            }
            id = sc.nextInt();
        }

        // Parte 2: Lê nomes e pesquisa na Trie
        long inicio = System.currentTimeMillis();
        sc.nextLine(); // Consome quebra de linha após -1

        String nome = sc.nextLine();
        while (!nome.equals("FIM")) {
            Restaurante encontrado = trie.pesquisar(nome);

            if (encontrado != null) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }

            nome = sc.nextLine();
        }
        sc.close();

        long fim = System.currentTimeMillis();
        double tempo = (fim - inicio) / 1000.0;

        // Exibe todos os restaurantes em ordem
        trie.mostrarEmOrdem();

        // Escreve log
        FileWriter log = new FileWriter("810688_arvore_trie_arvore.txt");
        log.write("810688\t" + trie.getComparacoes() + "\t" + tempo + "\n");
        log.close();
    }
}
