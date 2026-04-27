// Algoritmos e Estruturas de Dados 02 ----- Trabalho Prático 2
// Questão 9 ----- Ordenação por HeapSort em Java

import java.io.*;
import java.util.Scanner;

public class Heap {

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

        quicksort(base, 0, n - 1);

        for (int i = 0; i < n; i++) {
            System.out.println(base[i].formatar());
        }

        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e9;

        FileWriter fw = new FileWriter("matricula_quicksort.txt");
        fw.write("123456\t" + comparacoes + "\t" + movimentacoes + "\t" + tempo);
        fw.close();

        sc.close();
    }

    // QuickSort
    public static void quicksort(Restaurante[] array, int esq, int dir) {

        int i = esq, j = dir;
        Restaurante pivo = array[(esq + dir) / 2];

        while (i <= j) {

            while (comparar(array[i], pivo) < 0) {
                comparacoes++;
                i++;
            }
            comparacoes++;

            while (comparar(array[j], pivo) > 0) {
                comparacoes++;
                j--;
            }
            comparacoes++;

            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }

        if (esq < j) quicksort(array, esq, j);
        if (i < dir) quicksort(array, i, dir);
    }

    public static void swap(Restaurante[] array, int i, int j) {
        Restaurante tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
        movimentacoes += 3;
    }

        // Comparar
	public static int comparar(Restaurante a, Restaurante b) {

   	 // Ano
    	if (a.getData().getAno() != b.getData().getAno()) {
        	return a.getData().getAno() - b.getData().getAno();
    	}

    	// Mẽs
   	if (a.getData().getMes() != b.getData().getMes()) {
        	return a.getData().getMes() - b.getData().getMes();
    	}

    	// Dia
    	return a.getData().getDia() - b.getData().getDia();
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

	public int getDia() { return dia; }
	public int getMes() { return mes; }
	public int getAno() { return ano; }

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

    public Data getData() { return data; }

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
