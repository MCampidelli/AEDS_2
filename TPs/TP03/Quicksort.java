//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 3
//Questão 03 ----- Ordenação PARCIAL por Quicksort em Java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

class Data {
    private int ano;
    private int mes;
    private int dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public static Data parseData(String s) {

        String[] partes = s.split("-");

        int ano = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int dia = Integer.parseInt(partes[2]);

        return new Data(ano, mes, dia);
    }

    public String formatar() {

        return String.format("%02d/%02d/%04d",
                dia,
                mes,
                ano);
    }
}

class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public static Hora parseHora(String s) {

        String[] partes = s.split(":");

        int hora = Integer.parseInt(partes[0]);
        int minuto = Integer.parseInt(partes[1]);

        return new Hora(hora, minuto);
    }

    public String formatar() {

        return String.format("%02d:%02d",
                hora,
                minuto);
    }
}

class Restaurante {

    private int id;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;

    private String[] tiposCozinha;
    private int quantidadeTipos;

    private int faixaPreco;

    private Hora horarioAbertura;
    private Hora horarioFechamento;

    private Data dataAbertura;

    private boolean aberto;

    public Restaurante() {
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public static Restaurante parseRestaurante(String linha) {

        Restaurante restaurante =
                new Restaurante();

        String[] campos =
                separarCsv(linha);

        restaurante.id =
                Integer.parseInt(campos[0]);

        restaurante.nome =
                campos[1];

        restaurante.cidade =
                campos[2];

        restaurante.capacidade =
                Integer.parseInt(campos[3]);

        restaurante.avaliacao =
                Double.parseDouble(campos[4]);

        restaurante.tiposCozinha =
                campos[5].split(";");

        restaurante.quantidadeTipos =
                restaurante.tiposCozinha.length;

        restaurante.faixaPreco =
                campos[6].length();

        String[] horarios =
                campos[7].split("-");

        restaurante.horarioAbertura =
                Hora.parseHora(horarios[0]);

        restaurante.horarioFechamento =
                Hora.parseHora(horarios[1]);

        restaurante.dataAbertura =
                Data.parseData(campos[8]);

        restaurante.aberto =
                campos[9].equals("true");

        return restaurante;
    }

    private static String[] separarCsv(String linha) {

        String[] campos =
                new String[10];

        int indiceCampo = 0;

        String atual = "";

        boolean aspas = false;

        for (int i = 0;
             i < linha.length();
             i++) {

            char c = linha.charAt(i);

            if (c == '"') {

                aspas = !aspas;
            }
            else if (c == ',' && !aspas) {

                campos[indiceCampo++] =
                        atual;

                atual = "";
            }
            else {

                atual += c;
            }
        }

        campos[indiceCampo] = atual;

        return campos;
    }

    public String formatar() {

        String tipos = "[";

        for (int i = 0;
             i < quantidadeTipos;
             i++) {

            tipos += tiposCozinha[i];

            if (i < quantidadeTipos - 1) {

                tipos += ",";
            }
        }

        tipos += "]";

        String preco = "";

        for (int i = 0;
             i < faixaPreco;
             i++) {

            preco += "$";
        }

        return "[" +
                id + " ## " +
                nome + " ## " +
                cidade + " ## " +
                capacidade + " ## " +
                avaliacao + " ## " +
                tipos + " ## " +
                preco + " ## " +
                horarioAbertura.formatar() +
                "-" +
                horarioFechamento.formatar() +
                " ## " +
                dataAbertura.formatar() +
                " ## " +
                aberto +
                "]";
    }
}

class ColecaoRestaurantes {

    private Restaurante[] restaurantes;

    private int tamanho;

    public ColecaoRestaurantes() {

        restaurantes =
                new Restaurante[10000];

        tamanho = 0;
    }

    public void lerCsv(String path)
            throws Exception {

        BufferedReader br =
                new BufferedReader(
                        new FileReader(path));

        String linha =
                br.readLine();

        while ((linha = br.readLine())
                != null) {

            restaurantes[tamanho++] =
                    Restaurante.parseRestaurante(
                            linha);
        }

        br.close();
    }

    public Restaurante buscarPorId(int id) {

        Restaurante resp = null;

        for (int i = 0;
             i < tamanho;
             i++) {

            if (restaurantes[i]
                    .getId() == id) {

                resp = restaurantes[i];

                i = tamanho;
            }
        }

        return resp;
    }
}

public class Quicksort {

    static Restaurante[] array;

    static int n;

    static int k = 10;

    static long comparacoes = 0;

    static long movimentacoes = 0;

    public static int comparar(
            Restaurante a,
            Restaurante b) {

        comparacoes++;

        int resp =
                Double.compare(
                        a.getAvaliacao(),
                        b.getAvaliacao());

        if (resp == 0) {

            comparacoes++;

            resp =
                    a.getNome()
                            .compareTo(
                                    b.getNome());
        }

        return resp;
    }

    public static void swap(int i, int j) {

        Restaurante temp =
                array[i];

        array[i] = array[j];

        array[j] = temp;

        movimentacoes += 3;
    }

    public static void quicksortParcial(
            int esq,
            int dir) {

        int i = esq;
        int j = dir;

        Restaurante pivo =
                array[(esq + dir) / 2];

        while (i <= j) {

            while (comparar(
                    array[i],
                    pivo) < 0) {

                i++;
            }

            while (comparar(
                    array[j],
                    pivo) > 0) {

                j--;
            }

            if (i <= j) {

                swap(i, j);

                i++;
                j--;
            }
        }

        if (esq < j) {

            quicksortParcial(
                    esq,
                    j);
        }

        if (i < k && i < dir) {

            quicksortParcial(
                    i,
                    dir);
        }
    }

    public static void gerarLog(
            long tempo)
            throws Exception {

        PrintWriter pw =
                new PrintWriter(
                        new FileWriter(
                                "810688_quicksort_parcial.txt"));

        pw.println(
                "810688\t" +
                        comparacoes +
                        "\t" +
                        movimentacoes +
                        "\t" +
                        tempo);

        pw.close();
    }

    public static void main(String[] args)
            throws Exception {

        long inicio =
                System.currentTimeMillis();

        Scanner sc =
                new Scanner(System.in);

        ColecaoRestaurantes colecao =
                new ColecaoRestaurantes();

        colecao.lerCsv(
                "/tmp/restaurantes.csv");

        array =
                new Restaurante[1000];

        n = 0;

        int id =
                sc.nextInt();

        while (id != -1) {

            array[n++] =
                    colecao.buscarPorId(id);

            id =
                    sc.nextInt();
        }

        if (k > n) {

            k = n;
        }

        quicksortParcial(
                0,
                n - 1);

        for (int i = 0; i < n; i++) {

            System.out.println(
                    array[i].formatar());
        }

        long fim =
                System.currentTimeMillis();

        gerarLog(fim - inicio);

        sc.close();
    }
}
