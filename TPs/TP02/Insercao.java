// Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 02
// Questão 4 ----- Ordenação por Inserção em Java

import java.io.*;
import java.util.Scanner;

public class Insercao {

    static long comparacoes = 0;
    static long movimentacoes = 0;

    public static void main(String[] args) throws Exception {

        long inicio = System.nanoTime();

        Scanner sc = new Scanner(System.in);
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();

        Restaurante[] selecionados = new Restaurante[1000];
        int n = 0;

        int id;
        while ((id = sc.nextInt()) != -1) {

            for (int i = 0; i < colecao.getTamanho(); i++) {
                if (colecao.getRestaurantes()[i].getId() == id) {
                    selecionados[n++] = colecao.getRestaurantes()[i];
                    break;
                }
            }
        }

        insercao(selecionados, n);

        for (int i = 0; i < n; i++) {
            System.out.println(selecionados[i].formatar());
        }

        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e9;

        FileWriter fw = new FileWriter("matricula_insercao.txt");
        fw.write("123456\t" + comparacoes + "\t" + movimentacoes + "\t" + tempo);
        fw.close();

        sc.close();
    }

    // Inserção
    public static void insercao(Restaurante[] array, int n) {

        for (int i = 1; i < n; i++) {

            Restaurante tmp = array[i];
            movimentacoes++;

            int j = i - 1;

            while (j >= 0) {
                comparacoes++;

                if (array[j].getCidade().compareTo(tmp.getCidade()) > 0) {
                    array[j + 1] = array[j];
                    movimentacoes++;
                    j--;
                } else {
                    break;
                }
            }

            array[j + 1] = tmp;
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
