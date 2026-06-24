//Algoritmos e Estruturas de Dados 2 ----- Trabalho Prático 04
//Quetão 8 ----- Árvore Trie com Hash em Java

import java.util.*;
import java.io.*;

public class trieHash {
     private static void arqLog(String matricula, double tempo, long comp) {
          try (FileWriter arq = new FileWriter(matricula + "_arvore_trie_hash.txt")) {
               arq.write(matricula + "\t" + comp + "\t" + tempo + "\n");
          } catch (IOException e) {
               System.err.println("Erro no arquivo");
               e.printStackTrace();
          }
     }

     public static void main(String[] args) throws Exception {
          colecaoRestaurantes CR = new colecaoRestaurantes();
          CR.lerCsv();
          Scanner leitura = new Scanner(System.in);
          String linha = "";
          Trie trie = new Trie();
          int id = 0;

          while (leitura.hasNextLine() && id != -1) {
               linha = leitura.nextLine().trim();
               id = Restaurante.strParseInt(linha);
               for (int i = 0; i < CR.getTamanho(); i++) {
                    if (CR.getRestaurante(i).getId() == id) {
                         trie.inserir(CR.getRestaurante(i));
                    }
               }
          }

          long inicio = System.currentTimeMillis();
          String nomeRest = leitura.nextLine();
          while (nomeRest.compareTo("FIM") != 0) {
               if (trie.pesquisar(nomeRest)) {
                    System.out.println("SIM " + trie.encontrado.formatar());
               } else System.out.println("NAO");
               nomeRest = leitura.nextLine();
          }
          
          long fim = System.currentTimeMillis();

          double tempo = (double) (fim - inicio);
          arqLog("810688", tempo, trie.comp);
          leitura.close();
     }
}


class Trie {
     No raiz;
     long comp;
     Restaurante encontrado;

     public Trie() {
          this.raiz = new No('\0');
          this.comp = 0;
          this.encontrado = null;
     }

     public void inserir(Restaurante r) throws Exception {
          inserir(r, raiz, 0);
     }

     private void inserir(Restaurante r, No no, int i) throws Exception {
          char letra = r.getNome().charAt(i);
          int pos = no.hash(letra);
          if (no.prox[pos] == null) no.prox[pos] = new No(letra);
          if (i == r.getNome().length() - 1) {
               no.prox[pos].folha = true;
               no.prox[pos].restaurante = r;
          } else {
               inserir(r, no.prox[pos], i + 1);
          }
     }

     public boolean pesquisar(String s) throws Exception {
          return pesquisar(s, raiz, 0);
     }

     private boolean pesquisar(String s, No no, int i) throws Exception {
          boolean resp; 
          if (no.prox[no.hash(s.charAt(i))] == null) {
               this.comp++;
               resp = false;
          } else if (i == s.length() - 1) {
               this.comp++; 
               System.out.print(s.charAt(i) + " ");
               this.encontrado = no.prox[no.hash(s.charAt(i))].restaurante;
               resp = (no.prox[no.hash(s.charAt(i))].folha == true);
          } else if (i < s.length() - 1) {
               System.out.print(s.charAt(i) + " ");
               resp = pesquisar(s, no.prox[no.hash(s.charAt(i))], i + 1);
          }
          else throw new Exception ("Erro ao pesquisar");
          return resp;
     }

     public void mostrar() {
          mostrar("", raiz);
     }

     private void mostrar(String s, No no) {
          if (no.folha == true) System.out.println((s + no.elemento) + " ");

          for (int i = 0; i < no.prox.length; i++) {
               if(no.prox[i] != null) {
                    mostrar(s + no.elemento, no.prox[i]);
               }
          }
     }
}


class No {
     char elemento;
     int tam = 255;
     No[] prox;
     boolean folha;
     Restaurante restaurante;

     public No(char elemento) {
          this.elemento = elemento;
          this.prox = new No[tam];
          for(int i = 0; i < tam; i++) this.prox[i] = null;
          this.folha = false;
          this.restaurante = null;
     }

     public int hash(char x) {
          return (int)x % tam;
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
