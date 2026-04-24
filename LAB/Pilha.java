import java.util.*;

class Pilha {

	private int[] array;
	private int n; // quantidade de elementos

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		Pilha pilha = null;
		char op;
		int elemento;

		while(sc.hasNext()) {
			op = sc.next().charAt(0);
			if(op == 'C'){
				elemento = sc.nextInt();
				pilha = new Pilha(elemento);
			} else if(op == 'E') {
				elemento = sc.nextInt();
				pilha.empilhar(elemento);
			} else if(op == 'D') {
				System.out.println(pilha.desempilhar());
			} else if(op == 'M') {
				pilha.mostrar();
			} else if(op == 'P') {
				elemento = sc.nextInt();
				if(pilha.pesquisar(elemento)) {
					System.out.println('S');
				} else {
					System.out.println('N');
				}	
			}	
		}
		sc.close();	
	}	


	// Constroi a pilha com capacidade máxima igual a tamanho
	public Pilha(int tamanho){
		array = new int[tamanho];
		n = 0;
	}

	//Empilha x à pilha
	public void empilhar(int elemento) throws Exception {
		if(n >= array.length){
			throw new Exception("Erro");
		}
		array[n++] = elemento;
			
	}

	//Desempilha da pilha e retorna o elemento desempilhado
	public int desempilhar() throws Exception  {
		return array[--n];
	}

	//Imprime os elementos da pilha partindo do topo
	public void mostrar(){
		
		for(int i = n-1; i >= 0; i--) {
			System.out.print(array[i] + " ");
			if(n == 0){
				System.out.println('V');
				return;
			}
		}
		System.out.println("");	
	}

	//Retorna true caso o elemento x esteja na pilha e false caso contrário
	public boolean pesquisar(int elemento){
		boolean resp = false;
		for(int i = 0; i < n; i++) {
			if(array[i] == elemento) {
				resp = true;
				i = n;
			}
		}
		return resp;
	}	
}	
