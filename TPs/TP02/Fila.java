// Algoritmos e Estruturas de Dados 2 ----- Trabalho prático 2
// Questão 13 ----- Fila Circular com Alocação Sequencial em Java

import java.io.*;
import java.util.*;

class Data {
    int dia, mes, ano;

    public Data(int d, int m, int a) {
        dia = d; mes = m; ano = a;
    }

    public static Data parseData(String s) {
        return new Data(
            Integer.parseInt(s.substring(8,10)),
            Integer.parseInt(s.substring(5,7)),
            Integer.parseInt(s.substring(0,4))
        );
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {
    int h, m;

    public Hora(int h, int m) {
        this.h = h; this.m = m;
    }

    public static Hora parseHora(String s) {
        return new Hora(
            Integer.parseInt(s.substring(0,2)),
            Integer.parseInt(s.substring(3,5))
        );
    }

    public String formatar() {
        return String.format("%02d:%02d", h, m);
    }
}

class Restaurante {

    int id, capacidade, faixaPreco;
    double avaliacao;
    String nome, cidade;
    String[] tipos;
    Hora abertura, fechamento;
    Data data;
    boolean aberto;

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getAno() { return data.ano; }

    public static Restaurante parse(String linha) {

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

        String tiposStr = "[" + String.join(",", tipos) + "]";
        String preco = "$".repeat(faixaPreco);

        return "[" + id + " ## " + nome + " ## " + cidade + " ## " + capacidade +
               " ## " + avaliacao + " ## " + tiposStr + " ## " + preco +
               " ## " + abertura.formatar() + "-" + fechamento.formatar() +
               " ## " + data.formatar() + " ## " + aberto + "]";
    }
}

class Colecao {

    Restaurante[] array = new Restaurante[10000];
    int n = 0;

    public void lerCSV() throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
        br.readLine();

        String linha;
        while ((linha = br.readLine()) != null) {
            array[n++] = Restaurante.parse(linha);
        }

        br.close();
    }

    public Restaurante buscar(int id) {
        for (int i = 0; i < n; i++) {
            if (array[i].getId() == id) return array[i];
        }
        return null;
    }
}

// Fila Circular
public class Fila {

    private Restaurante[] array;
    private int primeiro, ultimo;

    public Fila(int tamanho) {
        array = new Restaurante[tamanho + 1]; 
        primeiro = ultimo = 0;
    }

    private int size() {
        return (ultimo - primeiro + array.length) % array.length;
    }

    public void inserir(Restaurante x) {

        // Remover antes, caso esteje cheio
        if ((ultimo + 1) % array.length == primeiro) {
            remover();
        }

        array[ultimo] = x;
        ultimo = (ultimo + 1) % array.length;

        System.out.println("(I)" + media());
    }

    public Restaurante remover() {

        Restaurante r = array[primeiro];
        primeiro = (primeiro + 1) % array.length;

        System.out.println("(R)" + r.getNome());
        return r;
    }

    private int media() {

        int soma = 0, count = size();

        for (int i = primeiro; i != ultimo; i = (i + 1) % array.length) {
            soma += array[i].getAno();
        }

        return Math.round((float)soma / count);
    }

    public void mostrar() {
        for (int i = primeiro; i != ultimo; i = (i + 1) % array.length) {
            System.out.println(array[i].formatar());
        }
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Colecao c = new Colecao();
        c.lerCSV();

        Fila f = new Fila(5);

        int id;

        while ((id = sc.nextInt()) != -1) {
            f.inserir(c.buscar(id));
        }

        int m = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < m; i++) {

            String[] p = sc.nextLine().split(" ");

            if (p[0].equals("I")) {
                f.inserir(c.buscar(Integer.parseInt(p[1])));
            } else {
                f.remover();
            }
        }

        f.mostrar();
        sc.close();
    }
}
