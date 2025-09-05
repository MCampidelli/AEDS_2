import java.util.Scanner;
import java.util.Random;

public class alteracao {
    
    public static String substitui(String s, Random gerador) {
        
        char primeira = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
        char segunda = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));

        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            
            char atual = s.charAt(i);

            if (atual == primeira) {
                resultado.append(segunda); 
            } else {
                resultado.append(atual); 
            }
        }

        return resultado.toString();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random gerador = new Random();
        gerador.setSeed(4); 

        while (true) {

            String linha = sc.nextLine();

            if (linha.equals("FIM")) {
                break; 
            }
            System.out.println(substitui(linha, gerador));
        }

        sc.close();
    }
}

