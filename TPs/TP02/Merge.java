// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 2
// Questão 07 ----- Ordenação por MergeSort em Java

import java.io.*;
import java.util.Scanner;

public class Merge {

    static long comparacoes = 0;
    static long movimentacoes = 0;

    public static void main(String[] args) throws Exception {

        long inicio = System.nanoTime();

        Scanner sc = new Scanner(System.in);
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();

        Restaurante[] base = new Restaurante[1000];
        int n = 0;

        int id;
        while ((id = sc.nextInt()) != -1) {
            for (int i = 0; i < colecao.getTamanho(); i++) {
                if (colecao.getRestaurantes()[i].getId() == id) {
                    base[n++] = colecao.getRestaurantes()[i];
                    break;
                }
            }
        }

        mergeSort(base, 0, n - 1);

        // Saída
        for (int i = 0; i < n; i++) {
            System.out.println(base[i].formatar());
        }

        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e9;

        FileWriter fw = new FileWriter("matricula_mergesort.txt");
        fw.write("123456\t" + comparacoes + "\t" + movimentacoes + "\t" + tempo);
        fw.close();

        sc.close();
    }

    // Merge
    public static void mergeSort(Restaurante[] array, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(array, esq, meio);
            mergeSort(array, meio + 1, dir);
            intercalar(array, esq, meio, dir);
        }
    }

    public static void intercalar(Restaurante[] array, int esq, int meio, int dir) {

        int n1 = meio - esq + 1;
        int n2 = dir - meio;

        Restaurante[] a1 = new Restaurante[n1];
        Restaurante[] a2 = new Restaurante[n2];

        for (int i = 0; i < n1; i++) {
            a1[i] = array[esq + i];
            movimentacoes++;
        }

        for (int j = 0; j < n2; j++) {
            a2[j] = array[meio + 1 + j];
            movimentacoes++;
        }

        int i = 0, j = 0, k = esq;

        while (i < n1 && j < n2) {
            comparacoes++;

            int cmpCidade = a1[i].getCidade().compareTo(a2[j].getCidade());

            if (cmpCidade < 0 ||
               (cmpCidade == 0 &&
                a1[i].getNome().compareTo(a2[j].getNome()) <= 0)) {

                array[k++] = a1[i++];
            } else {
                array[k++] = a2[j++];
            }
            movimentacoes++;
        }

        while (i < n1) {
            array[k++] = a1[i++];
            movimentacoes++;
        }

        while (j < n2) {
            array[k++] = a2[j++];
            movimentacoes++;
        }
    }
}

class Data {
    private int dia, mes, ano;

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
    private int hora, minuto;

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
    public String getCidade() { return cidade; }

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
