import java.util.Scanner;

public class cesar {

    public static String cifraCesar(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                c = (char) ('A' + (c - 'A' + 3) % 26);
            }

            else if (c >= 'a' && c <= 'z') {
                c = (char) ('a' + (c - 'a' + 3) % 26);
            }

            resultado.append(c);
        }
        return resultado.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            System.out.println(cifraCesar(linha));
        }

        sc.close();
    }
}

