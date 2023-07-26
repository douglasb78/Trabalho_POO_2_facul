package dbfvm.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import dbfvm.Aluno;
import dbfvm.Artigo;
import dbfvm.Pesquisador;
import dbfvm.Projeto;

public class GravadorArquivo {
	
	public GravadorArquivo(String caminhoArquivo, List<Projeto> listaProjetos) throws IOException {
		FileWriter fr;
		// Tentar criar arquivo:
		try {
			fr = new FileWriter(caminhoArquivo);
		} catch (IOException err) {
			System.out.println(String.format("ERRO NA CRIAÇÃO DO ARQUIVO %s!", caminhoArquivo));
			throw err;
		}
		// Se não houver dados para gravar:
		if(listaProjetos.isEmpty()) {
			System.out.println("Não há dados escolhidos para serem gravados.");
			return;
		}
		// Tentar gravar:
		try(BufferedWriter bw = new BufferedWriter(fr)){
			int i = 1;
			for (Projeto projeto : listaProjetos) {
				// Informações básicas projeto:
				bw.write("#Projeto"+i+'\n');
				bw.newLine();
				bw.write(String.format("Periodo: %s - %s\n", projeto.getDataInicio().split("/")[2], projeto.getDataFinal().split("/")[2]));
				bw.newLine();
				bw.write("Título: " + projeto.getTitulo() + '\n');
				bw.newLine();
				bw.write("Descrição: " + projeto.getDescricao());
				bw.newLine();
				String situacao = projeto.getConcluido() ? "Concluído\n" : "Em andamento\n";
				bw.write("Situação: " + situacao + ";\n");
				bw.newLine();
				bw.write("Natureza: Pesquisa.\n");
				bw.newLine();
				// Alunos
				String alunos = "Alunos envolvidos: ";
				for (Aluno aluno : projeto.getListaDeAlunos()) {
					alunos += aluno.getNome();
					alunos += ", ";
				}
				alunos = alunos.substring(0,alunos.length() - 2);
				alunos += '\n';
				bw.write(alunos);
				// Integrantes + coordenador
				bw.newLine();
				String integrantes = "";
				for (Pesquisador pesquisador : projeto.getPesquisadores()) {
					integrantes += pesquisador.getNome() + " - Integrante";
					integrantes += " / ";
				}
				if(projeto.getCoordenador() != null) {
					integrantes += projeto.getCoordenador().getNome() + " - Coordenador.";
				}
				integrantes += '\n';
				bw.write("Integrantes: "+integrantes);
				bw.newLine();
				// Artigos:
				for (Artigo artigo : projeto.getListaDeArtigos()) {
					String linhaArtigo = "Artigo: ";
					for (Pesquisador autor : artigo.getPesquisadores()) {
						linhaArtigo += autor.getNome();
						linhaArtigo += "; ";
					}
					linhaArtigo = linhaArtigo.substring(0,linhaArtigo.length() - 2);
					linhaArtigo += ". ";
					linhaArtigo += artigo.getTitulo();
					linhaArtigo += artigo.getTituloRevista();
					linhaArtigo += ", v. ahead-of-print, p. 1-15, ";
					linhaArtigo += artigo.getAnoPublicacao();
					linhaArtigo += '.';
					linhaArtigo += '\n';
					bw.write(linhaArtigo);
					bw.newLine();
				}
				bw.newLine();
				i++;
			}
			bw.flush();
			bw.close();
		}
	}
}
