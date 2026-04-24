import java.util.*;

public class cesar {
     public static Scanner leitura = new Scanner (System.in);

     public static String ciframento (String s) {
          String cifrado = "";
          int chave = 3;
          char letra;

          for (int i = 0; i < s.length(); i++) {
               letra = (char) (s.charAt(i) + chave);
               cifrado += letra;
          }

          return cifrado;
     }

     public static boolean isFim (String s) {
          return (s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M')? true : false;
     }

     public static void main (String[] args) {
          String palavra = leitura.nextLine();

          while (!isFim(palavra)) {
               System.out.println(ciframento(palavra));
               palavra = leitura.nextLine();
          }
          leitura.close();
     }
}
