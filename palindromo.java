import java.util.Scanner;

public class palindromo {

    public static boolean isPalindromo(String str) {
        
        str = str.toLowerCase().replaceAll("[^a-z]", "");

        int i = 0;
        int j = str.length() - 1;

        while (i < j) {
            if (str.charAt(i) != str.charAt(j)) {
                return false; 
            }
            
            i++;
            j--;
        }
        return true; 
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();

            if (isPalindromo(linha)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
        }

        sc.close();
    }
}
