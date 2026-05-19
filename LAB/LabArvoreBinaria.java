import java.util.Scanner;

// ==================== CLASSE NO ====================

class No {
    int elemento;
    No esq, dir;

    public No(int elemento) {
        this.elemento = elemento;
        this.esq = null;
        this.dir = null;
    }
}

// ==================== CLASSE ARVORE BINARIA ====================

class ArvoreBinaria {

    private No raiz;

    public ArvoreBinaria() {
        raiz = null;
    }

    // ==================== INSERIR ====================

    public void inserir(int x) {
        raiz = inserir(x, raiz);
    }

    private No inserir(int x, No i) {

        if (i == null) {
            i = new No(x);
        }
        else if (x < i.elemento) {
            i.esq = inserir(x, i.esq);
        }
        else if (x > i.elemento) {
            i.dir = inserir(x, i.dir);
        }

        return i;
    }

    // ==================== PESQUISAR ====================

    public boolean pesquisar(int x) {
        return pesquisar(x, raiz);
    }

    private boolean pesquisar(int x, No i) {

        boolean resp;

        if (i == null) {
            resp = false;
        }
        else {

            System.out.print(i.elemento + " ");

            if (x == i.elemento) {
                resp = true;
            }
            else if (x < i.elemento) {
                resp = pesquisar(x, i.esq);
            }
            else {
                resp = pesquisar(x, i.dir);
            }
        }

        return resp;
    }

    // ==================== PRE ORDEM ====================

    public void caminharPre() {

        if (raiz == null) {
            System.out.println("V");
        }
        else {
            caminharPre(raiz);
            System.out.println();
        }
    }

    private void caminharPre(No i) {

        if (i != null) {
            System.out.print(i.elemento + " ");
            caminharPre(i.esq);
            caminharPre(i.dir);
        }
    }

    // ==================== POS ORDEM ====================

    public void caminharPos() {

        if (raiz == null) {
            System.out.println("V");
        }
        else {
            caminharPos(raiz);
            System.out.println();
        }
    }

    private void caminharPos(No i) {

        if (i != null) {
            caminharPos(i.esq);
            caminharPos(i.dir);
            System.out.print(i.elemento + " ");
        }
    }

    // ==================== EM ORDEM ====================

    public void caminharEm() {

        if (raiz == null) {
            System.out.println("V");
        }
        else {
            caminharEm(raiz);
            System.out.println();
        }
    }

    private void caminharEm(No i) {

        if (i != null) {
            caminharEm(i.esq);
            System.out.print(i.elemento + " ");
            caminharEm(i.dir);
        }
    }
}

// ==================== MAIN ====================

public class LabArvoreBinaria {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArvoreBinaria ab = new ArvoreBinaria();

        while (sc.hasNext()) {

            String comando = sc.next();

            // Inserir
            if (comando.equals("I")) {

                int valor = sc.nextInt();
                ab.inserir(valor);
            }

            // Pesquisar
            else if (comando.equals("P")) {

                int valor = sc.nextInt();

                boolean resp = ab.pesquisar(valor);

                if (resp) {
                    System.out.println("S");
                }
                else {
                    System.out.println("N");
                }
            }

            // Pré-ordem
            else if (comando.equals("PRE")) {
                ab.caminharPre();
            }

            // Pós-ordem
            else if (comando.equals("POS")) {
                ab.caminharPos();
            }

            // Em-ordem
            else if (comando.equals("EM")) {
                ab.caminharEm();
            }
        }

        sc.close();
    }
}
