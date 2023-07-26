package dbfvm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import dbfvm.util.CarregadorArquivo;
import dbfvm.util.CarregadorDeDados;
import dbfvm.util.DataCalendario;
import dbfvm.util.GravadorArquivo;

public class InterfaceDeUsuario {
	private List<Pesquisador> listaDePesquisadores = new LinkedList<>();
	private List<Projeto> listaDeProjetos = new LinkedList<>();
	private List<Artigo> listaDeArtigos = new LinkedList<>();
	private List<Aluno> listaDeAlunos = new LinkedList<>();

	private Scanner leitor = new Scanner(System.in);
	private DataCalendario gerenciador_calendario = new DataCalendario();

	// ============================================================
	// Métodos para exibir pesquisadores:
	// ============================================================

	// Método para listar todos os pesquisadores cadastrados na "listaDePesquisadores": (Verificado Franco)
	public void imprimirPesquisadores() {
		if (listaDePesquisadores.isEmpty()) {
			System.out.println("ERRO: Não há pesquisadores cadastrados");
		} else {
			System.out.println(String.format("Pesquisadores cadastrados: [%d]", listaDePesquisadores.size()));
			for (Pesquisador pesquisador : listaDePesquisadores) {
				System.out.print(String.format("• \"%s\" - área: %s - instituição: %s", pesquisador.getNome(), pesquisador.getArea(), pesquisador.getUniversidade()).replace("null", "N/A"));
				//System.out.print(String.format("\thash: %s", pesquisador.hashCode())); // DEPURAÇÃO
				System.out.print('\n');
			}
		}
	}
	
	// Método para listar todos os alunos cadastrados na "listaDeAlunos": (Verificado Franco)
	public void imprimirAlunos() {
		if (listaDeAlunos.isEmpty()) {
			System.out.println("ERRO: Não há alunos cadastrados");
		} else {
			System.out.println(String.format("Alunos cadastrados: [%d]", listaDeAlunos.size()));
			for (Aluno aluno : listaDeAlunos) {
				System.out.print(String.format("• \"%s\" - área: %s - instituição: %s", aluno.getNome(), aluno.getArea(), aluno.getUniversidade()).replace("null", "N/A"));
				//System.out.print(String.format("\thash: %s", pesquisador.hashCode())); // DEPURAÇÃO
				System.out.print('\n');
			}
		}
	}
	
	// Método para listar todos os alunos de um projeto:
	public void imprimirAlunosDeUmProjeto() {
		imprimirProjetos();
		if(this.listaDeProjetos.isEmpty()) return;
		Projeto projetoEncontrado = procurarProjeto();
		if(projetoEncontrado == null)
			return;
		for (Aluno aluno : projetoEncontrado.getListaDeAlunos())
			System.out.println(String.format("• \"%s\" - área: %s - instituição: %s", aluno.getNome(), aluno.getArea(), aluno.getUniversidade()).replace("null", "N/A"));
		if(projetoEncontrado.getListaDeAlunos().isEmpty())
			System.out.println("Nenhum aluno cadastrado no projeto.");
		return;
	}
	
	// Método para listar todos pesquisadores cadastrados em uma universidade:
	public void imprimirPesquisadorUniversidade() {
		int result = imprimirUniversidades();
		if(result == -1) return;
		List<Pesquisador> pesquisadoresEncontrados = new LinkedList<>();
		System.out.println("Digite o nome da universidade:");
		String nomeUniversidade = leitor.nextLine();
		// Procurar por pesquisadores da universidade:
		for (Pesquisador pesquisador : listaDePesquisadores) {
			if(pesquisador.getUniversidade() == null) continue;
			if(pesquisador.getUniversidade().toLowerCase().equals(nomeUniversidade.toLowerCase())) {
				pesquisadoresEncontrados.add(pesquisador);
			}
		}
		// Checar se pesquisadores foram encontrados:
		if(pesquisadoresEncontrados.isEmpty()) {
			System.out.print("ERRO: Nenhum pesquisador encontrado.");
			System.out.print("Certifique-se de digitar o nome da universidade corretamente!\n");
			return;
		}
		System.out.println("Pesquisadores encontrados na universidade " + nomeUniversidade + ":");
		// Se pesquisadores foram encontrados, mostrar saída:
		for (Pesquisador pesquisador : pesquisadoresEncontrados) {
			System.out.print(String.format("• \"%s\" ", pesquisador.getNome()));
			if(pesquisador.getArea() != null)
				System.out.print(String.format("(%s)", pesquisador.getArea()));
			System.out.print('\n');
		}
		return;
	}
	// Método para listar todos pesquisadores de um projeto:
	public void imprimirPesquisadoresProjeto() {
		Projeto projeto = procurarProjeto();
		if(projeto == null) {
			return;
		}
		if(!gerenciador_calendario.checarProjetoFoiFinalizado(projeto)) {
			System.out.println("ERRO: O projeto indicado não foi finalizado!");
			return;
		}
		String msg = String.format("Pesquisadores do projeto \"%s\":", projeto.getTitulo());
		System.out.println(msg);
		for (Pesquisador pesquisador : projeto.getPesquisadores()) {
			System.out.println(String.format("• \"%s\"", pesquisador.getNome()));
		}
	}
	// Método para listar todos os pesquisadores responsáveis por um artigo:
	public void imprimirPesquisadoresArtigo() {
		Artigo artigo = procurarArtigo();
		if(artigo == null) {
			return;
		}
		String msg = String.format("Autores do artigo: \"%s\":", artigo.getTitulo());
		System.out.println(msg);
		for (Pesquisador pesquisador : artigo.getPesquisadores()) {
			System.out.println(String.format("• \"%s\"", pesquisador.getNome()));
		}
	}

	// ============================================================
	// Métodos para exibir projetos:
	// ============================================================

	// Método para listar todos os projetos cadastrados na "listaDeProjetos": (Verificado Franco)
	public void imprimirProjetos() {
		if (listaDeProjetos.isEmpty()) {
			System.out.println("ERRO: Não há projetos cadastrados");
		} else {
			System.out.println("Projetos cadastrados:");
			for (Projeto projeto : listaDeProjetos) {
				System.out.print(String.format("• \"%s\" - descrição: %s - início: %s, fim: %s - no. pesquisadores: %s\n", projeto.getTitulo(), projeto.getDescricao(), projeto.getDataInicio(), projeto.getDataFinal(), projeto.getPesquisadores().size()));
				for (Pesquisador pesquisador : projeto.getPesquisadores()) {
					System.out.print(String.format("\t• \"%s\" - área: %s - instituição: %s\n", pesquisador.getNome(), pesquisador.getArea(), pesquisador.getUniversidade()).replace("null", "N/A"));
				}
			}
		}
	}
	// Método para listar todos projetos de um pesquisador:
	public void imprimirProjetosPesquisador() {
		Pesquisador pesquisadorEncontrado = null;
		// Procurar pesquisador:
		pesquisadorEncontrado = procurarPesquisador();
		// Se o pesquisador não estiver cadastrado, sair:
		if(pesquisadorEncontrado == null) {
			System.out.print("ERRO: Pesquisador não encontrado!");
			System.out.println("Certifique-se de digitar o nome corretamente!");
			return;
		}
		// Se for encontrado, continuar:
		// Para cada projeto do vetor listaDeProjetos, ver se o pesquisador está nele:
		List<Projeto> projetosDoPesquisador = new LinkedList<>();
		for (Projeto projeto : listaDeProjetos) {
			Set<Pesquisador> pesquisadoresProjeto = projeto.getPesquisadores();
			// Iterar lista de pesquisadores, se o pesquisador estiver nela, incluir o projeto:
			for (Pesquisador pesquisador : pesquisadoresProjeto) {
				if(pesquisador.equals(pesquisadorEncontrado)) {
					projetosDoPesquisador.add(projeto);
				}
			}
		}
		String nomePesquisador = pesquisadorEncontrado.getNome();
		// Se nenhum projeto for encontrado, retornar:
		if(projetosDoPesquisador.isEmpty()) {
			System.out.print("ERRO: ");
			System.out.print("Nenhum projeto encontrado para o pesquisador " + "\"" + nomePesquisador + "\". ");
			System.out.println("Certifique-se de digitar o nome corretamente!");
			return;
		}
		// Se encontrar projetos, mostrar saída:
		System.out.println("Projetos do pesquisador " + nomePesquisador + ":");
		for (Projeto projeto : projetosDoPesquisador) {
			System.out.println(String.format("• %s", projeto.getTitulo()));
		}
		return;
	}
	// Método para listar todos os artigos cadastrados na "listaDeArtigos": (Verificado Franco)
	public void imprimirArtigos() {
		if (listaDeArtigos.isEmpty()) {
			System.out.println("ERRO: Não há artigos cadastrados");
		} else {
			System.out.println("Artigos cadastrados:");
			for (Artigo artigo : listaDeArtigos) {
				System.out.print(String.format("• \"%s\" - revista: %s (%d) - no. autores: %d\n", artigo.getTitulo(), artigo.getTituloRevista(), artigo.getAnoPublicacao(), artigo.getPesquisadores().size()));
				for (Pesquisador pesquisador : artigo.getPesquisadores()) {
					System.out.print(String.format("\t• \"%s\" - área: %s - instituição: %s\n", pesquisador.getNome(), pesquisador.getArea(), pesquisador.getUniversidade()));
				}
			}
		}
	}
	// Método para listar universidades.
	public int imprimirUniversidades() {
		Set<String> listaNomes = new HashSet<>();
		// Pegar nome da universidade de cada pesquisador:
		for (Pesquisador pesquisador : listaDePesquisadores) {
			if(pesquisador.getUniversidade() == null) continue;
			listaNomes.add(pesquisador.getUniversidade());
		}
		// Imprimir:
		System.out.println("Universidades cadastradas:");
		for (String string : listaNomes) {
			System.out.print(String.format("• %s\n", string));
		}
		if(listaNomes.isEmpty()) {
			System.out.println("• Nenhuma (!)");
			return -1;
		} else {
			return 1;
		}
	}

	// ============================================================
	// Métodos de busca:
	// ============================================================

	// Método para procurar aluno: (copy-paste do Pesquisador)
	// AVISO: Pode retornar Null (!), colocar checagem onde chamar o método.
	public Aluno procurarAluno() {
		System.out.println("Qual o nome do aluno?");
		String nomeAluno = leitor.nextLine();
		List<Aluno> alunosEncontrados = new LinkedList<>();
		// Procurar pesquisadores com nome especificado na lista:
		for (Aluno aluno : listaDeAlunos) {
			if(aluno.getNome().toLowerCase().equals(nomeAluno.toLowerCase())) {
				alunosEncontrados.add(aluno);
			}
		}
		// Se não encontrar pesquisadores:
		if(alunosEncontrados.isEmpty()) {
			System.out.print("ERRO: Aluno não encontrado! ");
			System.out.println("Certifique-se de digitar o nome corretamente!");
			return null;
		}
		// Se só encontrar 1 pesquisador:
		if(alunosEncontrados.size() == 1) {
			return alunosEncontrados.get(0);
		}
		// Se tiver vários pesquisadores com mesmo nome, mas em faculdades ou áreas diferentes:
		int escolha = 0;
		while(escolha <= 0 || escolha > alunosEncontrados.size()) {
			System.out.println("Escolha um aluno, digitando o número:");
			for (int i = 0; i<alunosEncontrados.size(); i++) {
				Aluno alunoIndice = alunosEncontrados.get(i);
				System.out.print(String.format("%d. %s - Universidade: %s - Área: %s\n", i+1, alunoIndice.getNome(), alunoIndice.getUniversidade(), alunoIndice.getArea()));
			}
			escolha = leitor.nextInt();
		}
		return alunosEncontrados.get(escolha-1);
	}
	
	// Método para procurar pesquisador:
	// AVISO: Pode retornar Null (!), colocar checagem onde chamar o método.
	public Pesquisador procurarPesquisador() {
		System.out.println("Qual o nome do pesquisador?");
		String nomePesquisador = leitor.nextLine();
		List<Pesquisador> pesquisadoresEncontrados = new LinkedList<>();
		// Procurar pesquisadores com nome especificado na lista:
		for (Pesquisador pesquisador : listaDePesquisadores) {
			if(pesquisador.getNome().toLowerCase().equals(nomePesquisador.toLowerCase())) {
				pesquisadoresEncontrados.add(pesquisador);
			}
		}
		// Se não encontrar pesquisadores:
		if(pesquisadoresEncontrados.isEmpty()) {
			System.out.print("ERRO: Pesquisador não encontrado! ");
			System.out.println("Certifique-se de digitar o nome corretamente!");
			return null;
		}
		// Se só encontrar 1 pesquisador:
		if(pesquisadoresEncontrados.size() == 1) {
			return pesquisadoresEncontrados.get(0);
		}
		// Se tiver vários pesquisadores com mesmo nome, mas em faculdades ou áreas diferentes:
		int escolha = 0;
		while(escolha <= 0 || escolha > pesquisadoresEncontrados.size()) {
			System.out.println("Escolha um pesquisador, digitando o número:");
			for (int i = 0; i<pesquisadoresEncontrados.size(); i++) {
				Pesquisador pesquisadorIndice = pesquisadoresEncontrados.get(i);
				System.out.print(String.format("%d. %s - Universidade: %s - Área: %s\n", i+1, pesquisadorIndice.getNome(), pesquisadorIndice.getUniversidade(), pesquisadorIndice.getArea()));
			}
			escolha = leitor.nextInt();
		}
		return pesquisadoresEncontrados.get(escolha-1);
	}
	// Método para procurar por um projeto na "listaDeProjetos":
	// AVISO: Pode retornar Null (!), colocar checagem onde chamar o método.
	public Projeto procurarProjeto() {
		System.out.println("Qual o título do projeto?");
		String titulo = leitor.nextLine();
		Projeto projetoEncontrado = null;
		// Procurar projeto:
		for (Projeto projeto : listaDeProjetos) {
			if(projeto.getTitulo().equals(titulo))
				projetoEncontrado = projeto;
		}
		// Se não for encontrado:
		if(projetoEncontrado == null) {
			System.out.print("ERRO: Projeto não encontrado.");
			System.out.println("Certifique-se de digitar o nome corretamente!");
			return null;
		}
		// Retornar projeto:
		return projetoEncontrado;
	}
	// Método para procurar por um artigo na "listaDeArtigos":
	// AVISO: Pode retornar Null (!), colocar checagem onde chamar o método.
	public Artigo procurarArtigo() {
		System.out.println("Qual o título do artigo?");
		String titulo = leitor.nextLine();
		Artigo artigoEncontrado = null;
		// Procurar projeto:
		for (Artigo artigo : listaDeArtigos) {
			if(artigo.getTitulo().equals(titulo))
				artigoEncontrado = artigo;
		}
		// Se não for encontrado:
		if(artigoEncontrado == null) {
			System.out.print("ERRO: Artigo não encontrado.");
			System.out.println("Certifique-se de digitar o nome corretamente!");
			return null;
		}
		// Retornar projeto:
		return artigoEncontrado;
	}

	// Método para exibir um diálogo de confirmação:
	public boolean perguntarSaida(String message) {
		String escolha = "";
		while(!(escolha.contains("sair") || escolha.contains("continuar"))) {
			System.out.print(String.format("Para cancelar %s , digite \"sair\".\n", message));
			System.out.print("Para continuar, digite \"continuar\".\n");
			escolha = leitor.nextLine().toLowerCase();
			if(!(escolha.contains("sair") || escolha.contains("continuar"))) {
				System.out.print("ERRO: Digite \"sair\" ou \"continuar\".\n");
			}
		}
		return escolha.contains("sair");
	}
	// ============================================================
	// Métodos de cadastro:
	// ============================================================
	public int cadastrarAluno() {
		Aluno aux = new Aluno();
		// Checar se o pesquisador já foi cadastrado:
		for (Aluno aluno : listaDeAlunos) {
			if(aluno.equals(aux)) {
				System.out.println("ERRO: O aluno já foi cadastrado!");
				return -1;
			}
		}
		// Se o pesquisador ainda não foi cadastrado:
		this.listaDeAlunos.add(aux);
		return (listaDeAlunos.size()-1);
	}
	// Método para cadastrar pesquisador, incrementando "listaDePesquisadores":
	public int cadastrarPesquisador() {
		Pesquisador aux = new Pesquisador();
		// Checar se o pesquisador já foi cadastrado:
		for (Pesquisador pesquisador : listaDePesquisadores) {
			if(pesquisador.equals(aux)) {
				System.out.println("ERRO: O pesquisador já foi cadastrado!");
				return -1;
			}
		}
		// Se o pesquisador ainda não foi cadastrado:
		this.listaDePesquisadores.add(aux);
		return (listaDePesquisadores.size()-1);
	}
	// Método para cadastrar projeto, incrementando o vetor "listaDeProjetos":
	public int cadastrarProjeto() {
		Projeto aux = new Projeto();
		aux.criarProjeto();
		// Checar se o projeto já foi cadastrado:
		for (Projeto projeto : listaDeProjetos) {
			if(projeto.equals(aux)) {
				System.out.println("ERRO: O projeto já foi cadastrado!");
				return -1;
			}
		}
		// Se o projeto ainda não foi cadastrado:
		listaDeProjetos.add(aux);
		return (listaDeProjetos.size()-1);
	}
	// Método para cadastrar artigo, incrementando "listaDeArtigos":
	public int cadastrarArtigo() {
		Artigo aux = new Artigo();
		aux.criarArtigo();
		// Checar se o pesquisador já foi cadastrado:
		for (Artigo artigo : listaDeArtigos) {
			if(artigo.equals(aux)) {
				System.out.println("ERRO: O artigo já foi cadastrado!");
				return -1;
			}
		}
		// Se o artigo ainda não foi cadastrado:
		listaDeArtigos.add(aux);
		return (listaDeArtigos.size()-1);
	}
	
	// Método para cadastrar artigo em projeto, uma citação:
	public void cadastrarProjetoArtigo() {
		Artigo artigoEncontrado = null;
		Projeto projetoEncontrado = null;
		
		boolean sair = false;
		imprimirArtigos();
		imprimirProjetos();
		// Buscar artigo:
		while(artigoEncontrado == null) {
			artigoEncontrado = procurarArtigo();
			if(artigoEncontrado == null) {
				sair = perguntarSaida("a citação de artigo");
				if(sair) {
					System.out.println("A citação de artigo foi cancelada.");
					return;
				}
			}
		}
		// Buscar projeto
		while(projetoEncontrado == null) {
			projetoEncontrado = procurarProjeto();
			if(projetoEncontrado == null) {
				sair = perguntarSaida("a citação de artigo");
				if(sair) {
					System.out.println("A citação de artigo foi cancelada.");
					return;
				}
			}
		}
		projetoEncontrado.cadastrarArtigo(artigoEncontrado);
		System.out.println(String.format("O artigo \"%s\" foi cadastrado no projeto \"%s\" como citação.", artigoEncontrado.getTitulo(), projetoEncontrado.getTitulo()));
	}
	// Métodos para cadastrar pesquisadores em um projeto:
	public void cadastrarProjetoPesquisador() {
		imprimirProjetos();
		imprimirPesquisadores();
		Projeto projeto = null;
		int participantes = -1;
		System.out.println("Quantos pesquisadores você deseja cadastrar no projeto?");
		while(participantes == -1) {
			try {
				participantes = leitor.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("ERRO: Digite um número válido!");
				participantes = -1;
				leitor.next(); // consumir new line
			}
		}
		int aux = participantes;
		leitor.nextLine();
		// Busca projeto:
		projeto = procurarProjeto();
		if(projeto == null) {
			return;
		}
		// Cadastro participantes:
		while (participantes > 0) {
			Pesquisador pesquisadorEncontrado = null;
			// Procurar pesquisador:
			while(pesquisadorEncontrado == null) {
				pesquisadorEncontrado = procurarPesquisador();
				// Se o pesquisador não estiver cadastrado, pedir o nome novamente:
				if(pesquisadorEncontrado == null) {
					boolean sair = perguntarSaida("o cadastro");
					if(sair) {
						System.out.print(String.format("%d pesquisadores foram cadastrado(s).\n", aux-participantes));
						return;
					}
				}
			}
			int result = projeto.cadastrarPesquisador(pesquisadorEncontrado);
			// acabei precisando fazer isso:
			int indexProjeto = listaDeProjetos.indexOf(projeto);
			listaDeProjetos.set(indexProjeto, projeto);
			if(result != -1) {
				System.out.print(String.format("Pesquisador \"%s\" cadastrado no projeto \"%s\".\n", pesquisadorEncontrado.getNome(), projeto.getTitulo()));
				participantes--;
			}
		}
	}
	// Métodos para cadastrar pesquisadores em um artigo:
	public void cadastrarArtigoPesquisador() {
		imprimirPesquisadores();
		imprimirArtigos();
		System.out.println("Quantos pesquisadores você deseja cadastrar no artigo?");
		int participantes = -1;
		while(participantes <= -1) {
			try {
				participantes = leitor.nextInt();
			} catch(InputMismatchException e) {
				participantes = -1;
				System.out.println("ERRO: Digite um número válido!");
				leitor.next(); // consumir new line
			}
		}
		leitor.nextLine();
		// Buscar artigo:
		Artigo artigo = procurarArtigo();
		if(artigo == null) {
			return;
		}
		// Cadastrar pesquisador no projeto:
		while (participantes > 0) {
			Pesquisador pesquisadorEncontrado = null;
			// Procurar pesquisador:
			pesquisadorEncontrado = procurarPesquisador();
			// Se o pesquisador não estiver cadastrado, sair:
			if(pesquisadorEncontrado == null) {
				return;
			}
			artigo.cadastrarPesquisador(pesquisadorEncontrado);
			participantes--;
		}
	}
	
	// ============================================================
	// Métodos de alteração de dados:
	// ============================================================
	
	public void trocarUniversidadePesquisador() {
		imprimirPesquisadores();
		Pesquisador pesquisadorEncontrado = procurarPesquisador();
		if(pesquisadorEncontrado == null)
			return;
		String nomeUniversidade = "";
		while(nomeUniversidade.equals("")) {
			System.out.println("Digite o nome da universidade do pesquisador: ");
			nomeUniversidade = leitor.nextLine();
			if(nomeUniversidade.isBlank()) {
				nomeUniversidade = "";
				leitor.next(); // consumir new line
				System.out.println("ERRO: Digite um nome válido.");
			}
		}
		pesquisadorEncontrado.setUniversidade(nomeUniversidade);
	}
	
	public void trocarAreaPesquisador() {
		imprimirPesquisadores();
		Pesquisador pesquisadorEncontrado = procurarPesquisador();
		if(pesquisadorEncontrado == null)
			return;
		String areaPesquisador = "";
		while(areaPesquisador.equals("")) {
			System.out.println("Digite a área do pesquisador: ");
			areaPesquisador = leitor.nextLine();
			if(areaPesquisador.isBlank()) {
				areaPesquisador = "";
				leitor.next(); // consumir new line
				System.out.println("ERRO: Digite um nome de área válido.");
			}
		}
		pesquisadorEncontrado.setArea(areaPesquisador);
	}
	
	public void trocarUniversidadeAluno() {
		imprimirAlunos();
		Aluno alunoEncontrado = procurarAluno();
		if(alunoEncontrado == null)
			return;
		String nomeUniversidade = "";
		while(nomeUniversidade.equals("")) {
			System.out.println("Digite o nome da universidade do aluno: ");
			nomeUniversidade = leitor.nextLine();
			if(nomeUniversidade.isBlank()) {
				nomeUniversidade = "";
				leitor.next(); // consumir new line
				System.out.println("ERRO: Digite um nome válido.");
			}
		}
		alunoEncontrado.setUniversidade(nomeUniversidade);
	}
	
	public void trocarAreaAluno() {
		imprimirAlunos();
		Aluno alunoEncontrado = procurarAluno();
		if(alunoEncontrado == null)
			return;
		String areaAluno = "";
		while(areaAluno.equals("")) {
			System.out.println("Digite a área do aluno: ");
			areaAluno = leitor.nextLine();
			if(areaAluno.isBlank()) {
				areaAluno = "";
				leitor.next(); // consumir new line
				System.out.println("ERRO: Digite um nome de área válido.");
			}
		}
		alunoEncontrado.setArea(areaAluno);
	}

	// ============================================================
	// Métodos para carregar dados:
	// ============================================================

	// Método para carregar dados salvos de forma fixa no programa: (Trabalho 1)
	public void carregarDadosSalvos() {
		CarregadorDeDados carregador = new CarregadorDeDados();
		carregador.criarDados();
		String escolha = "";
		while(!(escolha.toLowerCase().equals("sim")||escolha.toLowerCase().equals("não"))) {
			System.out.println("Atrelar os pesquisadores salvos aos artigos e projetos salvos?\nDigite \"sim\" ou \"não\":");
			escolha = leitor.nextLine();
			switch(escolha.toLowerCase()) {
				case "sim":
					carregador.AtrelarPesquisadores();
					break;
				case "não":
					break;
				default:
					System.out.println("ERRO: Opção inválida!");
			}
		}
		this.listaDePesquisadores.clear();
		this.listaDeProjetos.clear();
		this.listaDeArtigos.clear();
		this.listaDePesquisadores.addAll(carregador.getPesquisadoresSalvos());
		this.listaDeProjetos.addAll(carregador.getProjetosSalvos());
		this.listaDeArtigos.addAll(carregador.getArtigosSalvos());
	}

	// Método para carregar dados salvos em um arquivo externo: (Trabalho 2)
	public void carregarDadosSalvosArquivo() {
		CarregadorArquivo ca;
		String caminhoArquivo = Paths.get("").toAbsolutePath().toString() + "\\" + "Lattes.txt";
		// Leitura:
		while(true) {
			try {
				ca = new CarregadorArquivo(caminhoArquivo);
				String escolha = "";
				while(!escolha.toLowerCase().equals("s") && !escolha.toLowerCase().equals("a")) {
					System.out.println("Você deseja substituir os dados atuais ou adicionar?");
					System.out.println("A - Adicionar\t S - Substituir");
					escolha = leitor.nextLine();
					if(!escolha.toLowerCase().equals("s") && !escolha.toLowerCase().equals("a")) {
						System.out.println("ERRO: Opção inválida! Digite \"A\" para adicionar, ou \"S\" para substituir.");
					}
				}
				if(escolha.toLowerCase().equals("s")) {
					this.listaDeProjetos.clear();
					this.listaDeArtigos.clear();
					this.listaDePesquisadores.clear();
					this.listaDeAlunos.clear();
				}
				this.listaDeProjetos.addAll(ca.getListaProjetos());
				this.listaDeArtigos.addAll(ca.getListaArtigos());
				this.listaDePesquisadores.addAll(ca.getSetPesquisadores());
				this.listaDeAlunos.addAll(ca.getSetAlunos());
				if(escolha.toLowerCase().equals("s")) {
					System.out.println("Todos os dados do arquivo foram carregados em substituição.");
				} else {
					System.out.println("Todos os dados do arquivo foram adicionados.");
				}
				break;
			} catch (FileNotFoundException e) {
				String escolha = "";
				System.out.println("ERRO: Arquivo não encontrado!");
				while(!escolha.contains("nao") && !escolha.contains("sim")) {
					System.out.println("Você gostaria de digitar uma outra localização para procurar pelo arquivo?");
					System.out.println("Digite \"sim\", ou \"não\" para sair:");
					escolha = leitor.nextLine();
					escolha = escolha.toLowerCase().replace("ã", "a");
				}
				if(escolha.contains("nao")){
					return;
				} else if(escolha.contains("sim")) {
					System.out.println("Digite o caminho:");
					caminhoArquivo = leitor.nextLine();
				}
			}
		}
		System.out.println("Tentando remover duplicatas...");
		// Tentar remover duplicatas transformando em hashset:
		Set<Pesquisador> semDuplicataPesquisador = new HashSet<>(this.listaDePesquisadores);
		this.listaDePesquisadores.clear();
		this.listaDePesquisadores.addAll(semDuplicataPesquisador);
		Set<Aluno> semDuplicataAlunos = new HashSet<>(this.listaDeAlunos);
		this.listaDeAlunos.clear();
		this.listaDeAlunos.addAll(semDuplicataAlunos);
		// Tentar remover duplicatas usando stream().distinct()
		//List<Pesquisador> semDuplicataLista = this.listaDePesquisadores.stream().distinct().collect(Collectors.toList());
		//this.listaDePesquisadores.clear();
		//this.listaDePesquisadores.addAll(semDuplicataLista);
	}
	
	// Gravador de dados:
	public void gravarDados() {
		try {
			GravadorArquivo ga = new GravadorArquivo(Paths.get("").toAbsolutePath().toString() + "\\" + "LattesSaida.txt", this.listaDeProjetos);
		} catch (IOException err) {
			System.out.println("Erro na gravação. Nenhuma operação foi realizada.");
		}
	}

	// ============================================================
	// Construtor, exibição de menu:
	// ============================================================

	public InterfaceDeUsuario() {
		String escolha = "";
		while (!escolha.equals("sair")) {
			escolha = "";
			String msg = "";
			msg += "Olá, seja bem vindo\n";
			msg += "Opções disponíveis:\n";
			msg += "\t• 1. \"Cadastrar Pesquisador\"\n";
			msg += "\t• 2. \"Cadastrar Projeto\"\n";
			msg += "\t• 3. \"Cadastrar Pesquisadores em Projeto\"\n";
			msg += "\t• 4. \"Cadastrar Artigo\"\n";
			msg += "\t• 5. \"Cadastrar Pesquisadores em Artigo\"\n";
			msg += "\t• 6. \"Cadastrar Artigo em Projeto (citação)\"\n";
			msg += "Opções de carregar dados disponíveis:\n";
			msg += "\t• 7. \"Carregar Dados Salvos no Programa v1 (versão antiga)\"\n";
			msg += "\t• 8. \"Carregar Dados Salvos no Programa v2 (versão arquivo)\"\n";
			msg += "Leituras disponíveis:\n";
			msg += "\t• 9. \"Listar pesquisadores de uma universidade\"\n";
			msg += "\t• 10. \"Listar autores de um artigo\"\n";
			msg += "\t• 11. \"Listar projetos de um pesquisador\"\n";
			msg += "\t• 12. \"Listar pesquisadores de um projeto finalizado\"\n";
			msg += "\t• 13. \"Listar todos os projetos\"\n";
			msg += "\t• 14. \"Listar todos os pesquisadores\"\n";
			msg += "\t• 15. \"Listar todos os artigos\"\n";
			msg += "\t• 16. \"Listar todos as universidades (em que há pesquisadores cadastrados)\"\n";
			msg += "\t• 17. \"Listar todos os alunos\"\n";
			msg += "\t• 18. \"Listar todos alunos (envolvidos em um projeto)\"\n";
			msg += "Opções de alteração de dados -- Pesquisador:\n";
			msg += "\t• 19. \"Definir ou trocar a universidade de um pesquisador\"\n";
			msg += "\t• 20. \"Definir ou trocar a área de um pesquisador\"\n";
			msg += "Opções de alteração de dados -- Aluno:\n";
			msg += "\t• 21. \"Definir ou trocar a universidade de um aluno\"\n";
			msg += "\t• 22. \"Definir ou trocar a área de um aluno\"\n";
			msg += "Opções de gravação de dados:\n";
			msg += "\t• 23. \"Gravar dados\"\n";
			msg += "Digite o número da opção, ou \"sair\" para encerrar o programa.\n";
			System.out.println(msg);
			escolha = leitor.nextLine();
			switch(escolha.toLowerCase()) {
				case "sair":
					break;
				case "cadastrar aluno", "0":
					cadastrarAluno();
					break;
				case "cadastrar pesquisador", "1":
					cadastrarPesquisador();
					break;
				case "cadastrar projeto", "2":
					cadastrarProjeto();
					break;
				case "cadastrar pesquisadores em projeto", "3":
					cadastrarProjetoPesquisador();
					break;
				case "cadastrar artigo", "4":
					cadastrarArtigo();
					break;
				case "cadastrar pesquisadores em artigo", "5":
					cadastrarArtigoPesquisador();
					break;
				case "cadastrar artigo em projeto (citação)", "6":
					cadastrarProjetoArtigo();
					break;
				case "carregar dados salvos no programa v1", "7":
					carregarDadosSalvos();
					break;
				case "carregar dados salvos no programa v2", "8":
					carregarDadosSalvosArquivo();
					break;
				case "listar pesquisadores de uma universidade", "9":
					imprimirPesquisadorUniversidade();
					break;
				case "listar autores de um artigo", "10":
					imprimirPesquisadoresArtigo();
					break;
				case "listar projetos de um pesquisador", "11":
					imprimirProjetosPesquisador();
					break;
				case "listar pesquisadores de um projeto finalizado", "12":
					imprimirPesquisadoresProjeto();
					break;
				case "listar todos os projetos", "13":
					imprimirProjetos();
					break;
				case "listar todos os pesquisadores", "14":
					imprimirPesquisadores();
					break;
				case "listar todos os artigos", "15":
					imprimirArtigos();
					break;
				case "listar todos os universidades (em que há pesquisadores cadastrados)", "16":
					imprimirUniversidades();
					break;
				case "listar todos alunos", "17":
					imprimirAlunos();
					break;
				case "listar todos alunos (envolvidos em um projeto)", "18":
					imprimirAlunosDeUmProjeto();
					break;
				case "definir ou trocar a universidade de um pesquisador", "19":
					trocarUniversidadePesquisador();
					break;
				case "definir ou trocar a área de um pesquisador", "20":
					trocarAreaPesquisador();
					break;
				case "definir ou trocar a universidade de um aluno", "21":
					trocarUniversidadeAluno();
					break;
				case "definir ou trocar a área de um aluno", "22":
					trocarAreaAluno();
					break;
				case "gravar dados", "23":
					gravarDados();
					break;
				default:
					System.out.println("ERRO: Opção inválida!");
					System.out.println("BREAKPOINT!");
			}
		}
	}

	public static void main(String[] args) {
		InterfaceDeUsuario p = new InterfaceDeUsuario();
		p.leitor.close();
	}
}