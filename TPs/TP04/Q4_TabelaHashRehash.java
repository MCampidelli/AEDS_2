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
 * Célula da tabela hash que pode conter um restaurante ou estar vazia.
 */
class CelulaHash {
    public Restaurante restaurante;
    public boolean ocupada;

    public CelulaHash() {
        this.restaurante = null;
        this.ocupada = false;
    }
}

/**
 * Tabela Hash Direta com Rehash.
 * Quando há colisão, aplica uma segunda função hash.
 * Função 1: (ASCII nome) mod 83
 * Função 2: (ASCII nome + 1) mod 83
 */
class TabelaHashRehash {
    private static final int TAMANHO = 83;
    private CelulaHash[] tabela;
    private long comparacoes;

    /**
     * Inicializa a tabela hash vazia.
     */
    public TabelaHashRehash() {
        this.tabela = new CelulaHash[TAMANHO];
        this.comparacoes = 0;
        for (int i = 0; i < TAMANHO; i++) {
            this.tabela[i] = new CelulaHash();
        }
    }

    /**
     * Calcula a soma dos códigos ASCII de uma string.
     */
    private int calcularASCII(String nome) {
        int soma = 0;
        for (int i = 0; i < nome.length(); i++) {
            soma += (int) nome.charAt(i);
        }
        return soma;
    }

    /**
     * Primeira função hash: (ASCII nome) mod TAMANHO
     */
    private int hash1(String nome) {
        int ascii = calcularASCII(nome);
        return ascii % TAMANHO;
    }

    /**
     * Segunda função hash: (ASCII nome + 1) mod TAMANHO
     */
    private int hash2(String nome) {
        int ascii = calcularASCII(nome);
        return (ascii + 1) % TAMANHO;
    }

    /**
     * Insere um restaurante na tabela usando rehash.
     * Se a primeira posição estiver ocupada, tenta a segunda.
     */
    public void inserir(Restaurante restaurante) {
        int pos1 = hash1(restaurante.getNome());
        comparacoes++;

        if (!tabela[pos1].ocupada) {
            tabela[pos1].restaurante = restaurante;
            tabela[pos1].ocupada = true;
        } else {
            int pos2 = hash2(restaurante.getNome());
            comparacoes++;

            if (!tabela[pos2].ocupada) {
                tabela[pos2].restaurante = restaurante;
                tabela[pos2].ocupada = true;
            }
            // Se ambas estão ocupadas, não consegue inserir
        }
    }

    /**
     * Pesquisa um restaurante pelo nome.
     * Retorna a posição (0-82) e incrementa comparações.
     * Retorna -1 se não encontrado.
     */
    public int pesquisar(String nome) {
        int pos1 = hash1(nome);
        comparacoes++;

        if (tabela[pos1].ocupada && tabela[pos1].restaurante.getNome().equals(nome)) {
            return pos1;
        }

        int pos2 = hash2(nome);
        comparacoes++;

        if (tabela[pos2].ocupada && tabela[pos2].restaurante.getNome().equals(nome)) {
            return pos2;
        }

        return -1;
    }

    /**
     * Retorna o número total de comparações realizadas.
     */
    public long getComparacoes() {
        return comparacoes;
    }
}

/**
 * Classe principal que executa o programa.
 * Lê restaurantes da coleção, insere na tabela hash, depois pesquisa.
 */
public class Q4_TabelaHashRehash {

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
        TabelaHashRehash tabela = new TabelaHashRehash();

        Scanner sc = new Scanner(System.in);

        // Parte 1: Lê IDs e insere restaurantes na tabela
        int id = sc.nextInt();
        while (id != -1) {
            Restaurante restaurante = buscarPorId(colecao, id);
            if (restaurante != null) {
                tabela.inserir(restaurante);
            }
            id = sc.nextInt();
        }

        // Parte 2: Lê nomes e pesquisa na tabela
        long inicio = System.currentTimeMillis();
        sc.nextLine(); // Consome a quebra de linha após o -1

        String nome = sc.nextLine();
        while (!nome.equals("FIM")) {
            int posicao = tabela.pesquisar(nome);

            if (posicao != -1) {
                // Busca o restaurante na coleção para recuperar os dados completos
                Restaurante restaurante = buscarPorId(colecao, posicao);
                // Na verdade, precisa buscar pelo nome na coleção
                Restaurante[] restaurantes = colecao.getRestaurantes();
                Restaurante encontrado = null;
                for (int i = 0; i < colecao.getTamanho(); i++) {
                    if (restaurantes[i].getNome().equals(nome)) {
                        encontrado = restaurantes[i];
                        break;
                    }
                }
                if (encontrado != null) {
                    System.out.println(posicao + " " + encontrado.formataRestaurante());
                }
            } else {
                System.out.println(-1);
            }

            nome = sc.nextLine();
        }
        sc.close();

        long fim = System.currentTimeMillis();
        double tempo = (fim - inicio) / 1000.0;

        // Escreve log
        FileWriter log = new FileWriter("810688_hash_rehash.txt");
        log.write("810688\t" + tabela.getComparacoes() + "\t" + tempo + "\n");
        log.close();
    }
}
