//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 3
//Questão 08 ----- Lista Dupla com Alocação Flexível em Java

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

class Data {

    int ano;
    int mes;
    int dia;

    public Data(int ano,
                int mes,
                int dia) {

        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public static Data parseData(String s) {

        String[] partes =
                s.split("-");

        return new Data(
                Integer.parseInt(partes[0]),
                Integer.parseInt(partes[1]),
                Integer.parseInt(partes[2]));
    }

    public String formatar() {

        return String.format(
                "%02d/%02d/%04d",
                dia,
                mes,
                ano);
    }
}

class Hora {

    int hora;
    int minuto;

    public Hora(int hora,
                int minuto) {

        this.hora = hora;
        this.minuto = minuto;
    }

    public static Hora parseHora(String s) {

        String[] partes =
                s.split(":");

        return new Hora(
                Integer.parseInt(partes[0]),
                Integer.parseInt(partes[1]));
    }

    public String formatar() {

        return String.format(
                "%02d:%02d",
                hora,
                minuto);
    }
}

class Restaurante {

    int id;

    String nome;

    String cidade;

    int capacidade;

    double avaliacao;

    String[] tiposCozinha;

    int quantidadeTipos;

    int faixaPreco;

    Hora horarioAbertura;

    Hora horarioFechamento;

    Data dataAbertura;

    boolean aberto;

    public static Restaurante parseRestaurante(
            String linha) {

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
                campos[9].toLowerCase()
                        .contains("true");

        return restaurante;
    }

    public static String[] separarCsv(
            String linha) {

        String[] campos =
                new String[10];

        String atual = "";

        boolean aspas = false;

        int campo = 0;

        for (int i = 0;
             i < linha.length();
             i++) {

            char c =
                    linha.charAt(i);

            if (c == '"') {

                aspas = !aspas;
            }
            else if (c == ',' &&
                    !aspas) {

                campos[campo++] =
                        atual;

                atual = "";
            }
            else {

                atual += c;
            }
        }

        campos[campo] = atual;

        return campos;
    }

    public String formatar() {

        String tipos = "[";

        for (int i = 0;
             i < quantidadeTipos;
             i++) {

            tipos += tiposCozinha[i];

            if (i <
                    quantidadeTipos - 1) {

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

class CelulaDupla {

    Restaurante restaurante;

    CelulaDupla prox;

    CelulaDupla ant;

    public CelulaDupla(
            Restaurante restaurante) {

        this.restaurante =
                restaurante;

        this.prox = null;

        this.ant = null;
    }
}

class ListaDupla {

    private CelulaDupla primeiro;

    private CelulaDupla ultimo;

    private int tamanho;

    public ListaDupla() {

        primeiro =
                new CelulaDupla(null);

        ultimo = primeiro;

        tamanho = 0;
    }

    public void inserirInicio(
            Restaurante restaurante) {

        CelulaDupla temp =
                new CelulaDupla(
                        restaurante);

        temp.prox =
                primeiro.prox;

        temp.ant =
                primeiro;

        if (primeiro.prox != null) {

            primeiro.prox.ant =
                    temp;
        }

        primeiro.prox = temp;

        if (primeiro == ultimo) {

            ultimo = temp;
        }

        tamanho++;
    }

    public void inserirFim(
            Restaurante restaurante) {

        CelulaDupla temp =
                new CelulaDupla(
                        restaurante);

        ultimo.prox = temp;

        temp.ant = ultimo;

        ultimo = temp;

        tamanho++;
    }

    public void inserir(
            Restaurante restaurante,
            int pos)
            throws Exception {

        if (pos < 0 ||
                pos > tamanho) {

            throw new Exception(
                    "Erro!");
        }

        else if (pos == 0) {

            inserirInicio(
                    restaurante);
        }

        else if (pos == tamanho) {

            inserirFim(
                    restaurante);
        }

        else {

            CelulaDupla i =
                    primeiro;

            for (int j = 0;
                 j < pos;
                 j++, i = i.prox);

            CelulaDupla temp =
                    new CelulaDupla(
                            restaurante);

            temp.prox = i.prox;

            temp.ant = i;

            i.prox.ant = temp;

            i.prox = temp;

            tamanho++;
        }
    }

    public Restaurante removerInicio()
            throws Exception {

        if (primeiro == ultimo) {

            throw new Exception(
                    "Erro!");
        }

        CelulaDupla temp =
                primeiro.prox;

        Restaurante resp =
                temp.restaurante;

        primeiro.prox =
                temp.prox;

        if (temp.prox != null) {

            temp.prox.ant =
                    primeiro;
        }

        if (temp == ultimo) {

            ultimo = primeiro;
        }

        tamanho--;

        return resp;
    }

    public Restaurante removerFim()
            throws Exception {

        if (primeiro == ultimo) {

            throw new Exception(
                    "Erro!");
        }

        Restaurante resp =
                ultimo.restaurante;

        ultimo =
                ultimo.ant;

        ultimo.prox = null;

        tamanho--;

        return resp;
    }

    public Restaurante remover(
            int pos)
            throws Exception {

        Restaurante resp;

        if (pos < 0 ||
                pos >= tamanho) {

            throw new Exception(
                    "Erro!");
        }

        else if (pos == 0) {

            resp =
                    removerInicio();
        }

        else if (pos ==
                tamanho - 1) {

            resp =
                    removerFim();
        }

        else {

            CelulaDupla i =
                    primeiro.prox;

            for (int j = 0;
                 j < pos;
                 j++, i = i.prox);

            i.ant.prox =
                    i.prox;

            i.prox.ant =
                    i.ant;

            resp =
                    i.restaurante;

            tamanho--;
        }

        return resp;
    }

    public void mostrar() {

        CelulaDupla i =
                primeiro.prox;

        while (i != null) {

            System.out.println(
                    i.restaurante
                            .formatar());

            i = i.prox;
        }
    }
}

class ColecaoRestaurantes {

    Restaurante[] restaurantes;

    int tamanho;

    public ColecaoRestaurantes() {

        restaurantes =
                new Restaurante[10000];

        tamanho = 0;
    }

    public void lerCsv(
            String path)
            throws Exception {

        BufferedReader br =
                new BufferedReader(
                        new FileReader(path));

        String linha =
                br.readLine();

        while ((linha =
                br.readLine()) != null) {

            restaurantes[tamanho++] =
                    Restaurante
                            .parseRestaurante(
                                    linha);
        }

        br.close();
    }

    public Restaurante buscarPorId(
            int id) {

        Restaurante resp = null;

        for (int i = 0;
             i < tamanho;
             i++) {

            if (restaurantes[i].id ==
                    id) {

                resp =
                        restaurantes[i];

                i = tamanho;
            }
        }

        return resp;
    }
}

public class ListaDuplaFlex {

    public static void main(
            String[] args)
            throws Exception {

        Scanner sc =
                new Scanner(System.in);

        ColecaoRestaurantes colecao =
                new ColecaoRestaurantes();

        colecao.lerCsv(
                "/tmp/restaurantes.csv");

        ListaDupla lista =
                new ListaDupla();

        int id =
                sc.nextInt();

        while (id != -1) {

            lista.inserirFim(
                    colecao.buscarPorId(
                            id));

            id =
                    sc.nextInt();
        }

        int n =
                sc.nextInt();

        sc.nextLine();

        for (int i = 0;
             i < n;
             i++) {

            String linha =
                    sc.nextLine();

            String[] partes =
                    linha.split(" ");

            if (partes[0]
                    .equals("II")) {

                int valor =
                        Integer.parseInt(
                                partes[1]);

                lista.inserirInicio(
                        colecao.buscarPorId(
                                valor));
            }

            else if (partes[0]
                    .equals("IF")) {

                int valor =
                        Integer.parseInt(
                                partes[1]);

                lista.inserirFim(
                        colecao.buscarPorId(
                                valor));
            }

            else if (partes[0]
                    .equals("I*")) {

                int pos =
                        Integer.parseInt(
                                partes[1]);

                int valor =
                        Integer.parseInt(
                                partes[2]);

                lista.inserir(
                        colecao.buscarPorId(
                                valor),
                        pos);
            }

            else if (partes[0]
                    .equals("RI")) {

                Restaurante removido =
                        lista.removerInicio();

                System.out.println(
                        "(R)" +
                                removido.nome);
            }

            else if (partes[0]
                    .equals("RF")) {

                Restaurante removido =
                        lista.removerFim();

                System.out.println(
                        "(R)" +
                                removido.nome);
            }

            else if (partes[0]
                    .equals("R*")) {

                int pos =
                        Integer.parseInt(
                                partes[1]);

                Restaurante removido =
                        lista.remover(pos);

                System.out.println(
                        "(R)" +
                                removido.nome);
            }
        }

        lista.mostrar();

        sc.close();
    }
}
