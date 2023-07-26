package dbfvm.util;

import java.util.LinkedList;
import java.util.List;

import dbfvm.Artigo;
import dbfvm.Pesquisador;
import dbfvm.Projeto;

public class CarregadorDeDados {
	List<Pesquisador> pesquisadoresSalvos = new LinkedList<>();
	List<Projeto> projetosSalvos = new LinkedList<>();
	List<Artigo> artigosSalvos = new LinkedList<>();

	public void criarDados() {
		// Criar pesquisadores:
		pesquisadoresSalvos.add(new Pesquisador("Gabriel", "Análise e Desenvolvimento de Software", "UCS"));
		pesquisadoresSalvos.add(new Pesquisador("Douglas", "Ciência da Computação", "UCS"));
		pesquisadoresSalvos.add(new Pesquisador("Felipe", "Engenharia de Software", "UCS"));
		pesquisadoresSalvos.add(new Pesquisador("Gustavo", "Ciência da Computação", "FSG"));
		pesquisadoresSalvos.add(new Pesquisador("Júlio", "Sistemas Digitais", "FSG"));
		// Criar projetos: (Só o último é não finalizado.)
		projetosSalvos.add(new Projeto("Projeto 1", "Descrição Genérica 1", "01/01/2020", "31/01/2020"));
		projetosSalvos.add(new Projeto("Projeto 2", "Descrição Genérica 2", "01/02/2020", "31/02/2020"));
		projetosSalvos.add(new Projeto("Projeto 3", "Descrição Genérica 3", "01/03/2020", "31/03/2020"));
		projetosSalvos.add(new Projeto("Projeto 4", "Descrição Genérica 4", "01/04/2020", "31/04/2020"));
		projetosSalvos.add(new Projeto("Projeto 5", "Descrição Genérica 5", "01/05/2020", "31/05/2024"));
		// Criar artigos:
		artigosSalvos.add(new Artigo("Artigo 1", 2003, "Revista Genérica 1"));
		artigosSalvos.add(new Artigo("Artigo 2", 2004, "Revista Genérica 2"));
		artigosSalvos.add(new Artigo("Artigo 3", 2005, "Revista Genérica 3"));
		artigosSalvos.add(new Artigo("Artigo 4", 2006, "Revista Genérica 4"));
		artigosSalvos.add(new Artigo("Artigo 5", 2007, "Revista Genérica 5"));
	}

	public void AtrelarPesquisadores() {
		// TODO: testar se está funcionando como deveria:
		if(artigosSalvos == null || pesquisadoresSalvos == null || projetosSalvos == null) {
			return;
		}
		// Atrelar pesquisadores aos projetos: (com uma quantidade igual de projetos)
		// (Gabriel, Douglas) (Gabriel, Felipe) (Douglas, Gustavo) (Felipe, Júlio) (Gustavo, Júlio)
		Projeto auxProjeto = null;
		int[] indexPesquisadoresProjeto = {0, 1, 0, 2, 1, 3, 2, 4, 3, 4};
		int j = 0;
		for(int i=0; i<projetosSalvos.size(); i++) {
			auxProjeto = projetosSalvos.get(i);
			auxProjeto.cadastrarPesquisador(pesquisadoresSalvos.get(indexPesquisadoresProjeto[j]));
			auxProjeto.cadastrarPesquisador(pesquisadoresSalvos.get(indexPesquisadoresProjeto[j+1]));
			j+=2;
		}
		// Atrelar pesquisadoers aos artigos: (como autores)
		// (Gabriel, Gustavo) (Gabriel, Júlio) (Gustavo, Douglas) (Júlio, Felipe) (Douglas, Felipe)
		Artigo auxArtigo = null;
		int[] indexPesquisadoresArtigo = {0, 3, 0, 4, 3, 1, 4, 2, 1, 2};
		j=0;
		for(int i=0; i<artigosSalvos.size(); i++) {
			auxArtigo = artigosSalvos.get(i);
			auxArtigo.cadastrarPesquisador(pesquisadoresSalvos.get(indexPesquisadoresArtigo[j]));
			auxArtigo.cadastrarPesquisador(pesquisadoresSalvos.get(indexPesquisadoresArtigo[j+1]));
			j+=2;
		}
	}

	public List<Pesquisador> getPesquisadoresSalvos() {
		return pesquisadoresSalvos;
	}

	public List<Projeto> getProjetosSalvos() {
		return projetosSalvos;
	}

	public List<Artigo> getArtigosSalvos() {
		return artigosSalvos;
	}
}
