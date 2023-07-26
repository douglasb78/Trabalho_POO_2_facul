package dbfvm;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Artigo {
	private String titulo;
	private int anoPublicacao;
	private String tituloRevista;
	private List<Pesquisador> listaDePesquisadores = new LinkedList<>();


	public Artigo(String titulo, int anoPublicacao, String tituloRevista) {
		this.titulo = titulo;
		this.anoPublicacao = anoPublicacao;
		this.tituloRevista = tituloRevista;
	}

	public Artigo(String titulo) {
		this.titulo = titulo;
	}

	public Artigo() {
	}

	public void criarArtigo() {
		@SuppressWarnings("resource")
		Scanner leitor = new Scanner(System.in);
		System.out.println("Você escolheu cadastrar um artigo.");
	    // Dados do Artigo:
	    System.out.println("Qual o título do artigo?");
	    titulo = leitor.nextLine();
	    System.out.println("Qual o ano de publicação do artigo?");
	    anoPublicacao = 0;
	    while(anoPublicacao == 0) {
		    try {
		    	anoPublicacao = leitor.nextInt();
		    } catch (InputMismatchException e) {
				System.out.println("ERRO: Digite um número válido!");
				anoPublicacao = 0;
				leitor.next(); // consumir new line
		    }
		}
	    leitor.nextLine(); // Consumir o new line.
	    System.out.println("Qual o título da revista?");
	    tituloRevista = leitor.nextLine();
	    System.out.println("Artigo cadastrado com sucesso!");
	}

	// Cadastro de pesquisadores, copy-paste do Projeto.java:
	public int cadastrarPesquisador() {
		Pesquisador aux = new Pesquisador();
		// Checar se o pesquisador já foi cadastrado:
		for (Pesquisador pesquisador : listaDePesquisadores) {
			if(pesquisador.equals(aux)) {
				System.out.println("ERRO: O pesquisador já foi cadastrado no artigo!");
				return -1;
			}
		}
		// Se o pesquisador ainda não foi cadastrado:
		listaDePesquisadores.add(aux);
		return (listaDePesquisadores.size()-1);
	}

	public int cadastrarPesquisador(Pesquisador pesquisador) {
		for (Pesquisador cadastrado: this.listaDePesquisadores) {
			if(pesquisador.equals(cadastrado))
				return -1;
		}
		this.listaDePesquisadores.add(pesquisador);
		return 1;
    }

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getAnoPublicacao() {
		return this.anoPublicacao;
	}

	public void setAnoPublicacao(int anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public String getTituloRevista() {
		return this.tituloRevista;
	}

	public void setTituloRevista(String tituloRevista) {
		this.tituloRevista = tituloRevista;
	}

	public List<Pesquisador> getPesquisadores() {
		return this.listaDePesquisadores;
	}

	public void setPesquisadores(List<Pesquisador> pesquisadores) {
		this.listaDePesquisadores = pesquisadores;
	}

	@Override
	public String toString() {
		return "Artigo [titulo=" + titulo + ", anoPublicacao=" + anoPublicacao + ", tituloRevista=" + tituloRevista
				+ ", listaDePesquisadores=" + listaDePesquisadores + "]";
	}

	
}