package dbfvm;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import dbfvm.util.NormalizadorStrings;

public class Pesquisador {
	protected String nome;
	protected String area;
	protected String universidade;

	public Pesquisador(String nome, String area, String universidade) {
		this.nome = nome;
		this.area = area;
		this.universidade = universidade;
	}

	public Pesquisador() {
		this.cadastrarPesquisador();
	}
	public Pesquisador(String nome) {
		this.nome = nome;
	}

	public void cadastrarPesquisador() {
		@SuppressWarnings("resource")
		Scanner leitor = new Scanner(System.in);
		if(this instanceof Aluno)
			System.out.println("Cadastrando pesquisador aluno:");
		else
			System.out.println("Cadastrando pesquisador.");
	    // Dados do pesquisador:
	    System.out.println("Qual o nome do pesquisador?");
	    nome = leitor.nextLine();
	    System.out.println("Qual a área do pesquisador?");
	    area = leitor.nextLine();
	    System.out.println("Qual a universidade do pesquisador?");
	    universidade = leitor.nextLine();
	    System.out.println("Pesquisador cadastrado com sucesso!");
	}

	public List<Projeto> getProjetosParticipantes(List<Projeto> listaDeProjetos) {
	     List<Projeto> projetosDeCadaPesquisador = new LinkedList<>();
	     for (Projeto projeto : listaDeProjetos) {
	         Set<Pesquisador> pesquisadoresDoProjeto = projeto.getPesquisadores();
	         for (Pesquisador pesquisador : pesquisadoresDoProjeto) {
	             if (pesquisador.equals(this)) {
	            	 projetosDeCadaPesquisador.add(projeto);
	             }
	         }
	     }
	     return projetosDeCadaPesquisador;
	 }

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getUniversidade() {
		return this.universidade;
	}

	public void setUniversidade(String universidade) {
		this.universidade = universidade;
	}

	@Override
	public boolean equals(Object obj) {
		//if(this.hashCode() == obj.hashCode()) return true;
		// Se não é "Pesquisador":
		if(!(obj instanceof Pesquisador outroPesquisador)) return false;
		// Se o nome em minúsculas e sem acentos não for igual:
		NormalizadorStrings n = new NormalizadorStrings();
		if(!n.normalizar(this.nome).equals(n.normalizar(outroPesquisador.nome)))
			return false;
		// Se a área e universidades forem diferentes:
		if((this.area != null && outroPesquisador.area != null) &&
		!this.area.equals(outroPesquisador.area)
		) {
			return false;
		}
		if((this.universidade != null && outroPesquisador.universidade != null) &&
		!this.universidade.equals(outroPesquisador.universidade)) {
			return false;
		}
		return this.hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		NormalizadorStrings n = new NormalizadorStrings();
		return (n.normalizar(nome) + area + universidade).hashCode();
	}
	
	
}