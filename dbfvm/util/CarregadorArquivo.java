package dbfvm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dbfvm.Aluno;
import dbfvm.Artigo;
import dbfvm.Pesquisador;
import dbfvm.Projeto;
import dbfvm.exceptions.InvalidArticleException;
import dbfvm.exceptions.InvalidDateException;

// regex nome do artigo e nome: (?<= \. ).*?(?=, v\. )
// dá uma string com o nome do artigo e o nome da revista, separados por "."

public class CarregadorArquivo {

	List<Projeto> listaProjetos_tmp = new LinkedList<>();
	List<Artigo> listaArtigos_tmp = new LinkedList<>();
	Set<Pesquisador> setPesquisadores_tmp = new HashSet<>();
	Set<Aluno> setAlunos_tmp = new HashSet<>();
	Scanner leitor = new Scanner(System.in);

	// Converter "nome de citação" para "nome normal":
	// ex: "DA SILVA, MARCELO BENETTI CORRÊA" -> "Marcelo Benetti Corrêa Da Silva"
	private String converterNomeCitacao(String sobrenomeNome) {
		String auxString;
		try {
			String[] nomes;
			// separar pela vírgula e deixar minúsculo:
			nomes = sobrenomeNome.toLowerCase().split(", ");
			// juntar na ordem certa:
			auxString = nomes[1] + " " + nomes[0];
			// separar por espaço:
			nomes = auxString.split(" ");
			// deixar iniciais maiúsculas;
			auxString = "";
			for (int i=0; i<nomes.length; i++) {
				nomes[i] = Character.toUpperCase(nomes[i].charAt(0)) + nomes[i].substring(1);
				auxString += nomes[i];
				if(i != nomes.length-1)
					 auxString += " ";
			}
		} catch (ArrayIndexOutOfBoundsException e) { // ex: "NOTARI DL" no 2º artigo do Projeto 3.
			auxString = "";
			List<String> nomes = Arrays.asList(sobrenomeNome.split(" "));
			Collections.reverse(nomes);
			for (int i = 0; i<nomes.size(); i++) {
				auxString += nomes.get(i);
				if(i!=nomes.size()-1)
					auxString += " ";
			}
		}
		// retornar:
		return auxString;
	}

	// A partir de uma linha "Artigo:", separar o título do artigo e da revista:
	private Artigo lerLinhaArtigo(String line) throws InvalidArticleException{
		Artigo artigoGerado = new Artigo();
		List<String> titulosArtigo = new ArrayList<>();
		String nomeRevista;
		String aux;
		// Regex para título do artigo e revista:
		try {
			// Regex:
			Matcher matcherRegex_artigo = Pattern.compile("(?<=\s\\.\s).*?(?=,\sv\\.\s)").matcher(line);
			matcherRegex_artigo.find();
			aux = matcherRegex_artigo.group(0);
			titulosArtigo = Arrays.asList(aux.split("/"));
			// Separar nome da revista do último elemento:
			int titulosSize = titulosArtigo.size() - 1;
			String[] tmp = (titulosArtigo.get(titulosSize)).split("\\.");
			titulosArtigo.set(titulosSize, titulosArtigo.get(titulosSize).split("\\.")[0]); // tirar o nome da revista do 2º título (INGLÊS)

			nomeRevista = tmp[1];
			// Printar títulos e nome da revista:
			artigoGerado.setTitulo(titulosArtigo.get(0).stripIndent() + '.');
			artigoGerado.setTituloRevista(nomeRevista.stripIndent());
		} catch (IllegalStateException e) {
			throw new InvalidArticleException("Erro na leitura do artigo.\nDETALHES TÉCNICOS: O Regex não pôde ser aplicado. O Regex espera \" . \" antes do título, e \"v. \" após o nome da revista para fazer a filtagrem.");
		}
		// Regex para nome dos autores:
		try {
			Matcher macherRegex_pesquisadores = Pattern.compile("(?<=Artigo\\:).*(?=\s\\.\s)").matcher(line);
			macherRegex_pesquisadores.find();
			aux = macherRegex_pesquisadores.group(0);
			String[] nomesPesquisadores = aux.split(";");
			for (String string : nomesPesquisadores) {
				Pesquisador pesquisador = new Pesquisador(converterNomeCitacao(string.stripIndent()), null, null);
				artigoGerado.cadastrarPesquisador(pesquisador);
				this.setPesquisadores_tmp.add(pesquisador);
			}
		} catch (IllegalStateException e) {
			throw new InvalidArticleException("Erro na leitura do artigo.\nDETALHES TÉCNICOS: Nenhum pesquisador encontrado. Formatação inválida na linha.");
		}
		// Regex para pegar o ano do artigo:
		try {
			Matcher matcherRegex_anoArtigo = Pattern.compile("(?<=p.\\s(.*),\\s).*?(?=\\.)").matcher(line);
			matcherRegex_anoArtigo.find();
			artigoGerado.setAnoPublicacao(Integer.parseInt(matcherRegex_anoArtigo.group(0)));
		} catch (IllegalStateException e) {
			throw new InvalidArticleException("Erro na leitura do artigo.\nDETALHES TÉCNICOS: Nenhum pesquisador encontrado. Formatação inválida na linha.");
		} catch (NumberFormatException error) {
			throw new InvalidArticleException("Erro na leitura do artigo.\nDETALHES TÉCNICOS: O ano do artigo não é legível em forma de número.");
		}
		return artigoGerado;
	}

	private void lerArquivo(String caminhoArquivo) throws FileNotFoundException {
		FileReader fr;
		File f = new File(caminhoArquivo);
		//System.out.println(f.getAbsolutePath());

		// Tentar abrir o arquivo:
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException err) {
			System.out.println(String.format("ERRO NA ABERTURA DO ARQUIVO %s! (%s)", caminhoArquivo, f.getAbsolutePath()));
			throw err;
		}
		// Tentar ler o arquivo:
		try (BufferedReader br = new BufferedReader(fr)){
			Projeto p = null;
			String line;
			int numProjeto = 0;
			// Leitura:
			while((line = br.readLine()) != null) {
				// Se a linha começa jogo da velha, criar um objeto "Projeto" novo. Se já existir um, salvar na lista antes:
				// Dessa forma, a ordem das linhas não importa. Se tiver atributos faltando, é ignorado.
				if(line.length() > 0 && line.charAt(0) == '#') {
					// Adicionar projeto de antes (se existir):
					if(p!=null) {
						listaProjetos_tmp.add(p);
					}
					// Criar objeto do projeto:
					p = new Projeto();
					// Pegar número do projeto para usar nas mensagens:
					try {
						numProjeto = Integer.parseInt(line.replaceAll("[^0-9.]", ""));
					} catch (NumberFormatException e) {
						numProjeto = 0;
					}
				}
				// Pular leitura em caso de erro:
				if(p == null) {
					continue;
				}
				// Pegar título:
				if(line.contains("Título: ")) {
					p.setTitulo(line.replace("Título: ", ""));
				}
				// Pegar data inicial:
				if(line.contains("Periodo")) {
					String aux = line.replace("Periodo: ", "");
					String[] strDatas = aux.replace(" ", "").split("-");
					//System.out.println(strDatas[0] + " " + strDatas[1]);
					try {
						p.setDataAnos(strDatas[0], strDatas[1]);
					} catch (InvalidDateException e) {
						// Marcar para ignorar a linha se a data estiver inválida:
						System.out.print("[PROJETO " + numProjeto + "] ");
						System.out.print("ERRO NA LEITURA, O PROJETO SERÁ IGNORADO. ");
						System.out.print("(" + e.getMessage() + ")\n");
						p = null;
					}
				}
				// Pegar descrição:
				if(line.contains("Descrição:")) {
					p.setDescricao(line.replace("Descrição: ", ""));
				}
				// Pegar se foi concluído:
				if(line.contains("Situação: Concluído")) {
					p.setConcluido(true);
				}
				// Pegar integrantes e coordenadores:
				if(line.contains("Integrantes:")) {
					Set<String> coordenadores = new HashSet<>();
					Set<String> integrantes = Arrays.asList(line.replace("Integrantes: ", "").split(" / ")).stream().collect(Collectors.toSet());
					// Tratamento de string nos nomes:
					for (String integrante : integrantes) {
						if(integrante.toLowerCase().contains("coordenador")){
							coordenadores.add(integrante);
						}
					}
					integrantes.removeAll(coordenadores);
					// Criar pesquisadores e adicionar em cada projeto:
					for (String string : integrantes) {
						string = string.replace(" - Integrante", "").replace(".", "");
						if(string.equals(string.toUpperCase()) && string.contains(", "))
							string = converterNomeCitacao(string);
						Pesquisador integrante = new Pesquisador(string, null, null);
						p.cadastrarPesquisador(integrante);
						this.setPesquisadores_tmp.add(integrante);
					}
					for (String string : coordenadores) {
						string = string.replace(" - Coordenador", "").replace(".", "");
						if(string.equals(string.toUpperCase()) && string.contains(", "))
							string = converterNomeCitacao(string);
						Pesquisador coordenador = new Pesquisador(string, null, null);
						p.cadastrarCoordenador(coordenador);
						this.setPesquisadores_tmp.add(coordenador);
					}
				}
				// Pegar alunos envolvidos:
				if(line.contains("Alunos envolvidos:")) {
					List<String> alunos = Arrays.asList(line.replace("Alunos envolvidos: ", "").split(", "));
					for (String string : alunos) {
						Aluno aluno = new Aluno(string, null, null);
						p.cadastrarAluno(aluno);
						this.setAlunos_tmp.add(aluno);
					}
				}
				// Pegar artigos citados:
				if(line.contains("Artigo:")) {
					try {
						Artigo tmpArtigo = lerLinhaArtigo(line);
						p.cadastrarArtigo(tmpArtigo);
						this.listaArtigos_tmp.add(tmpArtigo);
					} catch (InvalidArticleException e) {
						System.out.print("[PROJETO " + numProjeto + "] ");
						System.out.print("ERRO NA LEITURA DE ARTIGO, O PROJETO SERÁ IGNORADO. ");
						System.out.println("LINHA: " + line);
						System.out.print("(" + e.getMessage() + ")\n");
						p = null;
					}
				}
				//System.out.println(line);
			}
			// Adicionar o último, que cai fora do while:
			if(p!=null) {listaProjetos_tmp.add(p);}
			// Fechar leitor:
			// Só fechar o Buffered Reader já basta
			// "Closes the stream and releases any system resources associated with it."
			br.close();

		} catch (IOException err) {
			System.out.println("ERRO NA LEITURA DO ARQUIVO!");
			return;
		}
	}



	public List<Projeto> getListaProjetos() {
		return this.listaProjetos_tmp;
	}

	public List<Artigo> getListaArtigos() {
	    return this.listaArtigos_tmp;
	}
	
	public Set<Aluno> getSetAlunos() {
	    return this.setAlunos_tmp;
	}

	public Set<Pesquisador> getSetPesquisadores() {
	    return this.setPesquisadores_tmp;
	}

	public void limparListaProjetos() {
		this.listaProjetos_tmp.clear();
	}

	public CarregadorArquivo(String nomeDoArquivo) throws FileNotFoundException {
		try {
			lerArquivo(nomeDoArquivo);
		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		CarregadorArquivo p;
		try {
			p = new CarregadorArquivo("C:\\Users\\dbiazus1\\Lattes.txt");
			Artigo teste = p.lerLinhaArtigo("Artigo: NOTARI, DANIEL LUIS; BATTISTELO, R. ; MOLIN, L. W. ; de Avila e Silva, Scheila ; Fachinelli, A. C. . APLICAÇÃO WEB PARA INDICADORES DE CIDADES DO CONHECIMENTO. REVISTA BRASILEIRA DE GESTÃO E INOVAÇÃO, v. 7, p. 95-118, 2020.");
			System.out.println(teste.getTitulo() + '\n' + teste.getTituloRevista());
			for (Pesquisador pesquisador : teste.getPesquisadores()) {
				System.out.println("• " + pesquisador.getNome());
			}
			System.out.println("BREAKPOINT");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArticleException e) {
			e.printStackTrace();
		}
	}
}
