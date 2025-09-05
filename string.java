// Algoritmos e Estruturas de Dados II
// Trabalho Prático 2 - Questão 3 - Inversão String
// Marina Campidelli 


import java.util.*;

public class string {

     public static Scanner leitura = new Scanner (System.in);

     public static String inverterString (String p) {
          String invertida = " ";

          for (int i = 0; i < p.length(); i++) {
          invertida += p.charAt(p.length() - 1 - i);
          }
          return invertida;
     }

     public static void main(String[] args) {

          String palavra = leitura.nextLine();

          while (!palavra.equalsIgnoreCase("FIM")) {
               System.out.println(inverterString(palavra));
               palavra = leitura.nextLine();
          }
     }
}
