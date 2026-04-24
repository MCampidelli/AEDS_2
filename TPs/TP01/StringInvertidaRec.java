//Algoritmos e Estruturas de Dados 02 ----- Trabalho Prático 01
//Questão 11 - Inversão de String (RECURSIVO)

import java.util.Scanner;

public class StringInvertidaRec {

    static String inverter(String s) {
        return inverter(s, 0);
    }

    static String inverter(String s, int i) {
        String resp;
        if (i == s.length()) {
            resp = "";
        } else {
            resp = inverter(s, i + 1) + s.charAt(i);
        }
        return resp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            if (linha.equals("FIM")) { break; }
            System.out.println(inverter(linha));
        }

        sc.close();
    }
}
