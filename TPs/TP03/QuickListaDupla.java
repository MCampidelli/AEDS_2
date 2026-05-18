//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 3
//Questão 11 ----- Ordenação por Quicksort com Lista Dupla Flexı́vel em Java

import java.util.*;
import java.io.*;

public class QuickListaDupla {

     private static void swap(Celula a, Celula b) {
          Restaurante tmp = a.restaurante;
          a.restaurante = b.restaurante;
          b.restaurante = tmp;
     }

     private static void quickRec(Celula esq, Celula dir, long[] conta) {
          Celula i = esq, j = dir;
          Restaurante pivo = esq.restaurante; 
          
          Celula meio = esq;
          Celula tmp = esq;
          while (tmp != dir && tmp.prox != dir) {
               meio = meio.prox;
               tmp = tmp.prox.prox;
          }
          pivo = meio.restaurante;

          while (i != j) {
               conta[0]++;
               while (i != j &&
                    (i.restaurante.getAvaliacao() < pivo.getAvaliacao() ||
                    (i.restaurante.getAvaliacao() == pivo.getAvaliacao() &&
                     i.restaurante.getNome().compareTo(pivo.getNome()) < 0))) {
                    i = i.prox;
                    conta[0]++;
               }

               conta[0]++;
               while (i != j &&
                    (j.restaurante.getAvaliacao() > pivo.getAvaliacao() ||
                    (j.restaurante.getAvaliacao() == pivo.getAvaliacao() &&
                     j.restaurante.getNome().compareTo(pivo.getNome()) > 0))) {
                    j = j.ant;
                    conta[0]++;
               }

               if (i != j) {
                    swap(i, j);
                    conta[1] += 3;
               }
          }

          if (esq != i && esq != i.prox) quickRec(esq, i.ant, conta);
          if (dir != i && dir != i.ant) quickRec(i.prox, dir, conta);
     }

     private static void quicksort(Lista l, long[] conta) {
          if (l.primeiro.prox == null || l.primeiro.prox == l.ultimo) return;
          quickRec(l.primeiro.prox, l.ultimo, conta);
     }

     private static void arqLog(String matricula, double tempo, long comp, long mov) {
          try (FileWriter arq = new FileWriter(matricula + "810688_quicksort_flexivel.txt")) {
               arq.write(matricula + "\t" + comp + "\t" + mov + "\t" + tempo + "\n");
          } catch (IOException e) {
               System.err.println("Erro no arquivo");
               e.printStackTrace();
          }
     }

     public static void main(String[] args) {
          colecaoRestaurantes CR = new colecaoRestaurantes();
          CR.lerCsv();
          Scanner leitura = new Scanner(System.in);
          String linha = "";
          int id = 0;
          Lista lista = new Lista();
          boolean lendo = true;

          while (leitura.hasNextLine() && lendo) {
               linha = leitura.nextLine().trim();
               id = Restaurante.strParseInt(linha);

               if (id != -1) {
                    for (int i = 0; i < CR.getTamanho(); i++) {
                         if (CR.getRestaurante(i).getId() == id) {
                              lista.inserirFim(CR.getRestaurante(i));
                         }
                    }
               } else {
                    lendo = false;
               }
          }
          leitura.close();

          long[] contadores = {0, 0};
          long inicio = System.currentTimeMillis();
          quicksort(lista, contadores);
          long fim = System.currentTimeMillis();

          for (Celula atual = lista.primeiro.prox; atual != null; atual = atual.prox) {
               System.out.println(atual.restaurante.formatar());
          }

          double tempo = (double) (fim - inicio);
          arqLog("810688", tempo, contadores[0], contadores[1]);
     }
}


class Lista {
     Celula primeiro, ultimo;

     public Lista() {
          this.primeiro = new Celula(null); 
          this.ultimo = primeiro;
     }

     public void inserirFim(Restaurante r) {
          ultimo.prox = new Celula(r);
          ultimo.prox.ant = ultimo;
          ultimo = ultimo.prox;
     }

     public int tamanho() {
          int tamanho = 0;
          for (Celula i = primeiro; i != ultimo; i = i.prox, tamanho++);
          return tamanho;
     }
}

class Celula {
     Restaurante restaurante;
     Celula prox, ant;

     public Celula(Restaurante r) {
          this.restaurante = r;
          this.prox = this.ant = null;
     }
}


class Data {
     private int ano;
     private int mes;
     private int dia;

     public Data(int ano, int mes, int dia) {
          this.ano = ano;
          this.mes = mes;
          this.dia = dia;
     }

     public int getDia() { return dia; }
     public int getMes() { return mes; }
     public int getAno() { return ano; }

     public static Data parseData(String s) {
          Scanner partes = new Scanner(s);
          partes.useDelimiter("-");
          int ano = partes.nextInt();
          int mes = partes.nextInt();
          int dia = partes.nextInt();
          partes.close();
          return new Data(ano, mes, dia);
     }

     public String formatar() {
          return String.format("%02d/%02d/%04d", dia, mes, ano);
     }
}


class Hora {
     private int hora;
     private int minuto;

     public Hora(int hora, int minuto) {
          this.hora = hora;
          this.minuto = minuto;
     }

     public int getHora() { return hora; }
     public int getMinuto() { return minuto; }

     public static Hora parseHora(String s) {
          Scanner partes = new Scanner(s);
          partes.useDelimiter(":");
          int hora = partes.nextInt();
          int minuto = partes.nextInt();
          partes.close();
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
     private String[] tipos_cozinha;
     private int faixaPreco;
     private Hora horarioAbertura;
     private Hora horarioFechamento;
     private Data dataAbertura;
     private boolean aberto;

     public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao,
          String[] tipos_cozinha, int faixaPreco, Hora horarioAbertura, Hora horarioFechamento,
          Data dataAbertura, boolean aberto) {
          this.id = id;
          this.nome = nome;
          this.cidade = cidade;
          this.capacidade = capacidade;
          this.avaliacao = avaliacao;
          this.tipos_cozinha = tipos_cozinha;
          this.faixaPreco = faixaPreco;
          this.horarioAbertura = horarioAbertura;
          this.horarioFechamento = horarioFechamento;
          this.dataAbertura = dataAbertura;
          this.aberto = aberto;
     }

     public int getId() { return id; }
     public String getNome() { return nome; }
     public String getCidade() { return cidade; }
     public int getCapacidade() { return capacidade; }
     public double getAvaliacao() { return avaliacao; }
     public String[] getTiposCozinha() { return tipos_cozinha; }
     public int getFaixaPreco() { return faixaPreco; }
     public Hora getHorarioAbertura() { return horarioAbertura; }
     public Hora getHorarioFechamento() { return horarioFechamento; }
     public Data getDataAbertura() { return dataAbertura; }
     public boolean getAberto() { return aberto; }

     public static Restaurante parseRestaurante(String linhaCSV) {
          int[] pos = {0};
          int id = strParseInt(campos(linhaCSV, pos));
          String nome = campos(linhaCSV, pos);
          String cidade = campos(linhaCSV, pos);
          int capacidade = strParseInt(campos(linhaCSV, pos));
          double avaliacao = strParseDouble(campos(linhaCSV, pos));
          String[] tipos_cozinha = split(campos(linhaCSV, pos));
          int faixaPreco = campos(linhaCSV, pos).length();

          String horarioStr = campos(linhaCSV, pos);
          int tracinho = -1;
          for (int i = 0; i < horarioStr.length() && tracinho == -1; i++)
               if (horarioStr.charAt(i) == '-') tracinho = i;

          Hora horarioAbertura = Hora.parseHora(subcampos(horarioStr, 0, tracinho));
          Hora horarioFechamento = Hora.parseHora(subcampos(horarioStr, tracinho + 1, horarioStr.length()));
          Data dataAbertura = Data.parseData(campos(linhaCSV, pos));
          boolean aberto = campos(linhaCSV, pos).compareTo("true") == 0;

          return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipos_cozinha,
               faixaPreco, horarioAbertura, horarioFechamento, dataAbertura, aberto);
     }

     public String formatar() {
          String tiposC = "[";
          for (int i = 0; i < tipos_cozinha.length; i++) {
               if (i > 0) tiposC += ",";
               tiposC += tipos_cozinha[i];
          }
          tiposC += "]";

          String preco = "";
          for (int i = 0; i < faixaPreco; i++) preco += "$";

          return "[" + id + " ## " + nome + " ## " + cidade + " ## " + capacidade + " ## "
               + String.format("%.1f", avaliacao) + " ## " + tiposC + " ## " + preco + " ## "
               + horarioAbertura.formatar() + "-" + horarioFechamento.formatar() + " ## "
               + dataAbertura.formatar() + " ## " + (aberto ? "true" : "false") + "]";
     }

     private static String[] split(String s) {
          int count = 1;
          for (int i = 0; i < s.length(); i++)
               if (s.charAt(i) == ';') count++;

          String[] result = new String[count];
          int idx = 0, inicio = 0;
          for (int i = 0; i < s.length(); i++) {
               if (s.charAt(i) == ';') {
                    result[idx++] = subcampos(s, inicio, i);
                    inicio = i + 1;
               }
          }
          result[idx] = subcampos(s, inicio, s.length());
          return result;
     }

     protected static int strParseInt(String s) {
          if (s.length() == 0) return 0;
          boolean numNeg = false;
          int inicio = 0, result = 0;
          if (s.charAt(0) == '-') { numNeg = true; inicio = 1; }
          for (int i = inicio; i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9'; i++)
               result = result * 10 + (s.charAt(i) - '0');
          return numNeg ? -result : result;
     }

     private static double strParseDouble(String s) {
          if (s.length() == 0) return 0.0;
          boolean numNeg = false;
          int inicio = 0;
          if (s.charAt(0) == '-') { numNeg = true; inicio = 1; }
          int inteiro = 0;
          boolean decimal = false;
          double parteDecimal = 0.0, divisor = 10.0;
          for (int i = inicio; i < s.length(); i++) {
               char c = s.charAt(i);
               if (c == '.' || c == ',') decimal = true;
               else if (c >= '0' && c <= '9') {
                    if (!decimal) inteiro = inteiro * 10 + (c - '0');
                    else { parteDecimal += (c - '0') / divisor; divisor *= 10.0; }
               }
          }
          return numNeg ? -(inteiro + parteDecimal) : (inteiro + parteDecimal);
     }

     private static String campos(String linha, int[] pos) {
          int inicio = pos[0];
          while (pos[0] < linha.length() && linha.charAt(pos[0]) != ',') pos[0]++;
          String campo = linha.length() == 0 ? "" : subcampos(linha, inicio, pos[0]);
          if (pos[0] < linha.length()) pos[0]++;
          return campo;
     }

     private static String subcampos(String s, int inicio, int fim) {
          char[] campo = new char[fim - inicio];
          for (int i = inicio; i < fim; i++) campo[i - inicio] = s.charAt(i);
          return new String(campo);
     }
}


class colecaoRestaurantes {
     private int tamanho;
     private Restaurante[] restaurantes;

     public colecaoRestaurantes() {
          this.tamanho = 0;
          this.restaurantes = new Restaurante[0];
     }

     public int getTamanho() { return tamanho; }
     public Restaurante getRestaurante(int i) { return restaurantes[i]; }

     public void lerCSV(String path) {
          try {
               Scanner leitura = new Scanner(new FileInputStream(path));
               leitura.nextLine();
               int count = 0;
               String linha = "";
               while (leitura.hasNextLine()) {
                    linha = leitura.nextLine().trim();
                    if (linha.length() > 0) count++;
               }
               leitura.close();

               restaurantes = new Restaurante[count];
               tamanho = count;

               leitura = new Scanner(new FileInputStream(path));
               leitura.nextLine();
               int i = 0;
               while (leitura.hasNextLine()) {
                    linha = leitura.nextLine().trim();
                    if (linha.length() > 0) restaurantes[i++] = Restaurante.parseRestaurante(linha);
               }
               leitura.close();
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }
     }

     public void lerCsv() { this.lerCSV("/tmp/restaurantes.csv"); }
}
