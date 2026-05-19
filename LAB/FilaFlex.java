//Algoritmos e Estruturas de Dados 2 ----- Laboratório
//Fila Flexível

import java.util.Scanner;

class Celula {

	int dado;
	Celula prox;

	public Celula(){
		this.prox = null;
	}

	public Celula(int dado) {
		this.dado = dado;
		this.prox = null;
	}

}

public class FilaFlex {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		FilaFlex fila = new FilaFlex();

		while(sc.hasNextLine()) {
			 
		}
	}
	private Celula cabeca;
	private Celula ultimo;

	public Fila() {
		cabeca = new Celula();
		ultimo = cabeca;
	}

	public void emfileirar(int x) {
		Celula nova = new Celula(x);
		ultimo.prox = nova;
		ultimo = nova;
	}

	public void desenfileirar() {
		if(cabeca.prox == null) {
			System.out.println(-1);
			return;
		}
		Celula removida = cabeca.prox;
		cabeca.prox = removida.prox;
		if(cabeca.prox == null) {
			ultimo = cabeca;
		}
		System.out.println(removida.dado);
	}

	public void mostrar() {
		if(cabeca.prox == null){
			System.out.println("V");
			return;
		} else {
			for(Celula i = cabeca.prox; i != null; i = i.prox) {			    
				System.out.print();
			}   
		}	

	}

	public void pesquisar(int y) {
		Celula atual = cabeca.prox;
		while(atual!= null) {
			if(atual.dado == y) {
				System.out.priintln("S");
				return;
			}
			atual = atual.prox;
		}
		System.out.println("N");
	}
}
