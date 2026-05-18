//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 3
//Questão 06 ----- Pilha com Alocação Flexível em Java

import java.util.Scanner;

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
        int capacidadeLocal = stringToInt(campos[3]);
        double avaliacao = stringToDouble(campos[4]);
        String[] tiposCozinha = stringPartes(campos[5], ';');
        int faixaPreco = campos[6].length();
        if (campos[7].length() < 11) return null;
        Hora horarioAbertura = Hora.parseHora(campos[7].substring(0, 5));
        Hora horarioFechamento = Hora.parseHora(campos[7].substring(6, 11));
        Data dataAbertura = Data.parseData(campos[8]);
        boolean aberto = campos[9].charAt(0) == 't';

         Restaurante r = new Restaurante(id, nome, cidade, capacidadeLocal, avaliacao,
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

class No {
    Restaurante restaurante;
    No prox;
 
    No(Restaurante r) {
        this.restaurante = r;
        this.prox = null;
    }
}
 
class Pilha {
    private No topo;
    private int tamanho;
 
    public Pilha() {
        this.topo = null;
        this.tamanho = 0;
    }
 
    public int getTamanho() { return tamanho; }
    public boolean vazia() { return topo == null; }
 
    public void empilha(Restaurante r) {
        No novo = new No(r);
        novo.prox = topo;
        topo = novo;
        tamanho++;
    }
 
    public Restaurante desempilha() {
        Restaurante r = topo.restaurante;
        No tmp = topo;
        topo = topo.prox;
        tmp.prox = null;
        tamanho--;
        return r;
    }
 
    public void mostrar() {
        No atual = topo;
        while (atual != null) {
            System.out.println(atual.restaurante.formataRestaurante());
            atual = atual.prox;
        }
    }
}
 
public class Main {
 
    static Restaurante buscar(ColecaoRestaurantes c, int id) {
        int i = 0;
        while (i < c.getTamanho() && c.getRestaurantes()[i].getId() != id) {
            i++;
        }
        if (i < c.getTamanho()) return c.getRestaurantes()[i];
        return null;
    }
 
    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerArquivo();
        Pilha pilha = new Pilha();
 
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
 
        while (id != -1) {
            pilha.empilha(buscar(colecao, id));
            id = sc.nextInt();
        }
 
        int m = sc.nextInt();
        sc.nextLine(); 
 
        for (int i = 0; i < m; i++) {
            String linha = sc.nextLine();
            String cmd = "";
            int j = 0;
            while (j < linha.length() && linha.charAt(j) != ' ') {
                cmd += linha.charAt(j);
                j++;
            }
 
            if (cmd.compareTo("I") == 0) {
                int rid = 0;
                j++; 
                while (j < linha.length()) {
                    rid = rid * 10 + (linha.charAt(j) - '0');
                    j++;
                }
                pilha.empilha(buscar(colecao, rid));
 
            } else if (cmd.compareTo("R") == 0) {
                Restaurante r = pilha.desempilha();
                System.out.println("(R)" + r.getNome());
            }
        }
 
        pilha.mostrar();
 
        sc.close();
    }
}
