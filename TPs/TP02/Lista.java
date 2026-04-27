// Algoritmos e Estruturas de Dados ----- Trabalho Prático 2
// Questão 11 ----- Lista com Alocação Sequencial em Java

import java.io.*;
import java.util.*;

class Data {
    int dia, mes, ano;

    public Data(int d, int m, int a) {
        dia = d; mes = m; ano = a;
    }

    public static Data parseData(String s) {
        int ano = Integer.parseInt(s.substring(0, 4));
        int mes = Integer.parseInt(s.substring(5, 7));
        int dia = Integer.parseInt(s.substring(8, 10));
        return new Data(dia, mes, ano);
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {
    int hora, minuto;

    public Hora(int h, int m) {
        hora = h; minuto = m;
    }

    public static Hora parseHora(String s) {
        int h = Integer.parseInt(s.substring(0, 2));
        int m = Integer.parseInt(s.substring(3, 5));
        return new Hora(h, m);
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, minuto);
    }
}

class Restaurante {

    private int id;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tipos;
    private int faixaPreco;
    private Hora abertura, fechamento;
    private Data data;
    private boolean aberto;

    public int getId() { return id; }
    public String getNome() { return nome; }

    public static Restaurante parseRestaurante(String linha) {

        String[] c = linha.split(",");

        Restaurante r = new Restaurante();

        r.id = Integer.parseInt(c[0]);
        r.nome = c[1];
        r.cidade = c[2];
        r.capacidade = Integer.parseInt(c[3]);
        r.avaliacao = Double.parseDouble(c[4]);

        r.tipos = c[5].split(";");
        r.faixaPreco = c[6].length();

        String[] h = c[7].split("-");
        r.abertura = Hora.parseHora(h[0]);
        r.fechamento = Hora.parseHora(h[1]);

        r.data = Data.parseData(c[8]);
        r.aberto = c[9].equals("true");

        return r;
    }

    public String formatar() {

        String tiposStr = "[";
        for (int i = 0; i < tipos.length; i++) {
            tiposStr += tipos[i];
            if (i < tipos.length - 1) tiposStr += ",";
        }
        tiposStr += "]";

        String preco = "";
        for (int i = 0; i < faixaPreco; i++) preco += "$";

        return "["
                + id + " ## "
                + nome + " ## "
                + cidade + " ## "
                + capacidade + " ## "
                + avaliacao + " ## "
                + tiposStr + " ## "
                + preco + " ## "
                + abertura.formatar() + "-" + fechamento.formatar() + " ## "
                + data.formatar() + " ## "
                + aberto
                + "]";
    }
}

class ColecaoRestaurantes {

    private int tamanho;
    private Restaurante[] array;

    public int getTamanho() { return tamanho; }
    public Restaurante[] getRestaurantes() { return array; }

    public void lerCsv(String path) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(path));

        array = new Restaurante[10000];
        tamanho = 0;

        br.readLine(); 

        String linha;
        while ((linha = br.readLine()) != null) {
            array[tamanho++] = Restaurante.parseRestaurante(linha);
        }

        br.close();
    }

    public static ColecaoRestaurantes lerCsv() throws Exception {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        c.lerCsv("/tmp/restaurantes.csv");
        return c;
    }
}

public class Lista {

    private Restaurante[] array;
    private int n;

    public Lista() {
        array = new Restaurante[1000];
        n = 0;
    }

    // Inserir
    public void inserirInicio(Restaurante x) throws Exception {
        for (int i = n; i > 0; i--) array[i] = array[i - 1];
        array[0] = x;
        n++;
    }

    public void inserirFim(Restaurante x) throws Exception {
        array[n++] = x;
    }

    public void inserir(Restaurante x, int pos) throws Exception {
        for (int i = n; i > pos; i--) array[i] = array[i - 1];
        array[pos] = x;
        n++;
    }

    // Remover
    public Restaurante removerInicio() throws Exception {
        Restaurante resp = array[0];
        for (int i = 0; i < n - 1; i++) array[i] = array[i + 1];
        n--;
        return resp;
    }

    public Restaurante removerFim() throws Exception {
        return array[--n];
    }

    public Restaurante remover(int pos) throws Exception {
        Restaurante resp = array[pos];
        for (int i = pos; i < n - 1; i++) array[i] = array[i + 1];
        n--;
        return resp;
    }

    // Mostrar
    public void mostrar() {
        for (int i = 0; i < n; i++) {
            System.out.println(array[i].formatar());
        }
    }

    // Buscar
    public static Restaurante buscar(ColecaoRestaurantes c, int id) {
        for (int i = 0; i < c.getTamanho(); i++) {
            if (c.getRestaurantes()[i].getId() == id) {
                return c.getRestaurantes()[i];
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
        Lista lista = new Lista();

        int id;

        while ((id = sc.nextInt()) != -1) {
            lista.inserirFim(buscar(colecao, id));
        }

        int m = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < m; i++) {

            String linha = sc.nextLine();
            String[] p = linha.split(" ");

            switch (p[0]) {

                case "II":
                    lista.inserirInicio(buscar(colecao, Integer.parseInt(p[1])));
                    break;

                case "IF":
                    lista.inserirFim(buscar(colecao, Integer.parseInt(p[1])));
                    break;

                case "I*":
                    lista.inserir(buscar(colecao, Integer.parseInt(p[2])), Integer.parseInt(p[1]));
                    break;

                case "RI":
                    System.out.println("(R) " + lista.removerInicio().getNome());
                    break;

                case "RF":
                    System.out.println("(R) " + lista.removerFim().getNome());
                    break;

                case "R*":
                    System.out.println("(R) " + lista.remover(Integer.parseInt(p[1])).getNome());
                    break;
            }
        }

        lista.mostrar();

        sc.close();
    }
}
