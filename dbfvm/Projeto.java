package dbfvm;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Scanner;

import dbfvm.exceptions.InvalidDateException;
import dbfvm.util.DataCalendario;

public class Projeto {
	private String titulo;
	private String descricao;
	private String dataInicio;
	private String dataFinal;
	private boolean concluido = false;
	private Set<Pesquisador> listaDePesquisadores = new HashSet<>();
	private Set<Pesquisador> listaDeCoordenadores = new HashSet<>();
	private List<Artigo> listaDeArtigos = new LinkedList<>();
	private List<Aluno> listaDeAlunos = new LinkedList<>();
	//private List<Artigo> artigosCitados = new LinkedList<Artigo>();

	private DataCalendario gerenciador_calendario = new DataCalendario();
	private Scanner leitor = new Scanner(System.in);

	public Projeto(String titulo, String descricao, String dataInicio, String dataFinal, Set<Pesquisador> listaDePesquisadores) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
		this.listaDePesquisadores = listaDePesquisadores;
	}

	public Projeto(String titulo, String descricao, String dataInicio, String dataFinal) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
	}

	public Projeto() {}

	public void criarProjeto() {
		System.out.println("Você escolheu cadastrar um projeto");
		System.out.println("Qual o nome do projeto?");
		String titulo = leitor.nextLine();
		System.out.println("Qual a intenção e método do projeto?");
		String descricao = leitor.nextLine();
		String datas[] = gerenciador_calendario.criarDatasProjeto();
		// Passar dados para os atributos:
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = datas[0];
		this.dataFinal = datas[1];
		System.out.println("Projeto cadastrado com sucesso!");
	}

	public int cadastrarPesquisador() {
		Pesquisador aux = new Pesquisador();
		// Checar se o pesquisador já foi cadastrado:
		for (Pesquisador pesquisador : listaDePesquisadores) {
			if(pesquisador.equals(aux)) {
				System.out.println("ERRO: O pesquisador já foi cadastrado no projeto!");
				return -1;
			}
		}
		// Se o pesquisador ainda não foi cadastrado:
		listaDePesquisadores.add(aux);
		return (listaDePesquisadores.size()-1);
	}

	public int cadastrarPesquisador(Pesquisador pesquisador) {
		// Checar se o pesquisador já foi cadastrado:
		for (Pesquisador pesquisadorLista : listaDePesquisadores) {
			if(pesquisadorLista.equals(pesquisador)) {
				System.out.println("ERRO: O pesquisador já foi cadastrado no projeto!");
				return -1;
			}
		}
		// Se o pesquisador ainda não foi cadastrado:
		listaDePesquisadores.add(pesquisador);
		return (listaDePesquisadores.size()-1);
	}

	public void cadastrarCoordenador(Pesquisador p) {
		for (Pesquisador pesquisador : listaDeCoordenadores) {
			if(pesquisador.equals(p)) {
				System.out.println("ERRO: Coordenador já cadastrado!");
				return;
			}
		}
		this.listaDeCoordenadores.add(p);
	}

	public void cadastrarAluno(Aluno a) {
		for (Aluno aluno : listaDeAlunos) {
			if(aluno.equals(a)) {
				System.out.println("ERRO: Aluno já cadastrado!");
				return;
			}
		}
		this.listaDeAlunos.add(a);
	}

	public void cadastrarArtigo(Artigo artigo) {
		this.listaDeArtigos.add(artigo);
	}
	
	public boolean getConcluido() {
		return this.concluido;
	}

	public String getTitulo() {
		return titulo;
	}

	public Set<Pesquisador> getPesquisadores() {
		return listaDePesquisadores;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Artigo> getListaDeArtigos() {
		return listaDeArtigos;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public List<Aluno> getListaDeAlunos() {
		return listaDeAlunos;
	}

	public Pesquisador getCoordenador() {
		if(listaDeCoordenadores.isEmpty())
			return null;
		return listaDeCoordenadores.stream().collect(Collectors.toList()).get(0);
	}

	public void setDataAnos(String dataI, String dataF) throws InvalidDateException {
		String[] datas;
		try {
			datas = new DataCalendario().criarDatasComAnos(dataI, dataF);
			dataInicio = datas[0];
			dataFinal = datas[1];
		} catch (InvalidDateException e) {
			throw e;
		}
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
}