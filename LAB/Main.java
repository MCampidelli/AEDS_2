//Algoritmos e Estruturas de Dados 2 ----- Laboratório
//Exercício Concurso de Levantamento de Peso em Java

import java.util.Scanner;

class Atleta {
    String nome;
    int peso;

    Atleta(String nome, int peso) {
        this.nome = nome;
        this.peso = peso;
    }
}

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Atleta[] atletas = new Atleta[100];
        int n = 0;

        // Lê até o fim da entrada
        while (sc.hasNext()) {
            String nome = sc.next();
            int peso = sc.nextInt();

            atletas[n] = new Atleta(nome, peso);
            n++;
        }

        // Ordenação
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {

                // Ordem decrescente de peso
                if (atletas[i].peso < atletas[j].peso) {

                    Atleta temp = atletas[i];
                    atletas[i] = atletas[j];
                    atletas[j] = temp;

                }
                // Empate no peso -> ordem alfabética
                else if (atletas[i].peso == atletas[j].peso) {

                    if (atletas[i].nome.compareTo(atletas[j].nome) > 0) {

                        Atleta temp = atletas[i];
                        atletas[i] = atletas[j];
                        atletas[j] = temp;
                    }
                }
            }
        }

        // Impressão
        for (int i = 0; i < n; i++) {
            System.out.println(atletas[i].nome + " " + atletas[i].peso);
        }

        sc.close();
    }
}
