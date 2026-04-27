//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 02
//Questão 01 ----- Modelagem em Java

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();

        int id;

        while ((id = sc.nextInt()) != -1) {

            for (int i = 0; i < colecao.getTamanho(); i++) {
                if (colecao.getRestaurantes()[i].getId() == id) {
                    System.out.println(
                        colecao.getRestaurantes()[i].formatar()
                    );
                    break;
                }
            }
        }

        sc.close();
    }
}

class Data {

    private int dia;
    private int mes;
    private int ano;

    public Data(int dia, int mes, int ano) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
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

    public int getAno() {
        return ano;
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
        int hora = Integer.parseInt(s.substring(0, 2));
        int minuto = Integer.parseInt(s.substring(3, 5));
        return new Hora(hora, minuto);
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
    private String[] tiposCozinha;
    private int faixaPreco;
    private Hora abertura;
    private Hora fechamento;
    private Data dataAbertura;
    private boolean aberto;

    public int getId() { return id; }
    public String getNome() { return nome; }

    public static Restaurante parseRestaurante(String linha) {

        String[] campos = linha.split(",");

        Restaurante r = new Restaurante();

        r.id = Integer.parseInt(campos[0]);
        r.nome = campos[1];
        r.cidade = campos[2];
        r.capacidade = Integer.parseInt(campos[3]);
        r.avaliacao = Double.parseDouble(campos[4]);

        r.tiposCozinha = campos[5].split(";");

        // faixa preço ($$$ → 3)
        r.faixaPreco = campos[6].length();

        // horário
        String[] partesHora = campos[7].split("-");
        r.abertura = Hora.parseHora(partesHora[0]);
        r.fechamento = Hora.parseHora(partesHora[1]);

        // data
        r.dataAbertura = Data.parseData(campos[8]);
        r.aberto = campos[9].equals("true");

        return r;
    }

    public String formatar() {

        String tipos = "[";
        for (int i = 0; i < tiposCozinha.length; i++) {
            tipos += tiposCozinha[i];
            if (i < tiposCozinha.length - 1) tipos += ",";
        }
        tipos += "]";

        String preco = "";
        for (int i = 0; i < faixaPreco; i++) preco += "$";

        return "["
                + id + " ## "
                + nome + " ## "
                + cidade + " ## "
                + capacidade + " ## "
                + avaliacao + " ## "
                + tipos + " ## "
                + preco + " ## "
                + abertura.formatar() + "-" + fechamento.formatar() + " ## "
                + dataAbertura.formatar() + " ## "
                + aberto
                + "]";
    }
}

class ColecaoRestaurantes {

    private int tamanho;
    private Restaurante[] restaurantes;

    public int getTamanho() {
        return tamanho;
    }

    public Restaurante[] getRestaurantes() {
        return restaurantes;
    }

    public void lerCsv(String path) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(path));

        restaurantes = new Restaurante[10000];
        tamanho = 0;

        br.readLine(); 

        String linha;
        while ((linha = br.readLine()) != null) {
            restaurantes[tamanho++] = Restaurante.parseRestaurante(linha);
        }

        br.close();
    }

    public static ColecaoRestaurantes lerCsv() throws Exception {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        c.lerCsv("/tmp/restaurantes.csv");
        return c;
    }
}
