package dbfvm.util;

import java.time.Year;
import java.util.Scanner;

import dbfvm.Projeto;
import dbfvm.exceptions.InvalidDateException;

public class DataCalendario {
	private Scanner leitor = new Scanner(System.in);

	public boolean checarProjetoFoiFinalizado(Projeto projeto) {
		String dataDeHoje = "";
		System.out.println("Qual a data de hoje?");
		boolean bool_formato = false;
		while(!bool_formato) {
			dataDeHoje = leitor.nextLine();
			bool_formato = checarFormato(dataDeHoje);
		}
		boolean terminou = !(checarDataPeriodo(dataDeHoje, projeto.getDataFinal()));
		//System.out.println("Terminou: " + terminou);
		//projeto.getDataInicio()
		return terminou;
	}

	public String[] criarDatasProjeto() {
		String[] datas = new String[2];
		String dataInicio = "", dataFim = "";
		boolean bool_formato, bool_data;
		bool_formato = false;
		// Data de início, checando se é valida:
		while(!bool_formato) {
			System.out.println("Qual a data de início do projeto?");
			dataInicio = leitor.nextLine();
			bool_formato = checarFormato(dataInicio);
		}
		// Data de fim, checando se é valida e acontece depois do início:
		bool_formato = false;
		bool_data = false;
		while(!bool_formato || !bool_data) {
			System.out.println("Qual a data de fim do projeto?");
			dataFim = leitor.nextLine();
			bool_formato = checarFormato(dataInicio);
			if(bool_formato) {
				bool_data = checarDataPeriodo(dataInicio, dataFim);
				if(!bool_data) {
					System.out.println("ERRO: Data inválida! Certifique-se de digitar o dia e mês corretamente.");
				}
			}
		}
		//dataFim e dataInicio estarão no formato certo.
		//dataFim acontecerá depois que dataInicio
		datas[0] = dataInicio;
		datas[1] = dataFim;
		return datas;
	}

	// Método para checar se a data final está depois da data de início
	// AVISO: Assume que as datas estão no formato certo.
	public boolean checarDataPeriodo(String dataInicio, String dataFim) {
		int diaI, mesI, anoI;
		int diaF, mesF, anoF;
		String[] dataInicio_comp;
		String[] dataFim_comp;
		// Separar valores da data de início:
		dataInicio_comp = dataInicio.split("/");
		diaI = Integer.parseInt(dataInicio_comp[0]);
		mesI = Integer.parseInt(dataInicio_comp[1]);
		anoI = Integer.parseInt(dataInicio_comp[2]);
		// Separar valores da data final:
		dataFim_comp = dataFim.split("/");
		diaF = Integer.parseInt(dataFim_comp[0]);
		mesF = Integer.parseInt(dataFim_comp[1]);
		anoF = Integer.parseInt(dataFim_comp[2]);
		// Checar se é maior:
		// (diaF >= dia I = projeto pode iniciar e acabar no mesmo dia.)
		if(anoF > anoI || (anoF == anoI && mesF > mesI) || (anoF == anoI && mesF == mesI && diaF >= diaI)){
			return true;
		}
		return false;
	}
	// Método para checar se a data está no formato DD/MM/YYYY e os números estão nos ranges certos:
	public boolean checarFormato(String data) {
		String[] data_comparacao = data.split("/");
		int dia, mes, ano;
		boolean bissexto, formato = false;
		// Ver se a data está no formato certo:
		if(data_comparacao.length != 3) {
			System.out.println("ERRO: Formato inválido! Certifique-se de digitar a data corretamente.");
			return false;
		}
		// Tentar converter a data em "String" para "int":
		try {
			dia = Integer.parseInt(data_comparacao[0]);
			mes = Integer.parseInt(data_comparacao[1]);
			ano = Integer.parseInt(data_comparacao[2]);
		} catch (NumberFormatException error) {
			System.out.println("ERRO: Formato inválido! Certifique-se de digitar a data corretamente.");
			return false;
		}
		// Checagem de ano bissexto, para usar nos "ranges" logo depois:
		bissexto = (ano % 400 == 0)||((ano % 4 == 0) && (ano % 100 != 0));
		// Checar se os números estão nos ranges certo:
		if(mes == 2) {
			if(bissexto) {
				formato = (dia >= 1 && dia <= 29);
			} else {
				formato = (dia >= 1 && dia <= 28);
			}
		} else if(mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
			formato = (dia >= 1 && dia <= 31);
		} else if(mes == 4 || mes == 6 || mes == 9 || mes == 10) {
			formato = (dia >= 1 && dia <= 30);
		}
		// Retornar se a data é válida:
		if(!formato)
			System.out.println("ERRO: Data inválida! Certifique-se de digitar o dia e mês corretamente.");
		return formato;
	}

	public String[] criarDatasComAnos(String dataI, String dataF) throws InvalidDateException {
		String[] datas = new String[2];
		int anoI, anoF;
		// Ano início do projeto:
		try {
			anoI = Integer.parseInt(dataI);
		} catch (NumberFormatException err) {
			throw new InvalidDateException("ERRO: Data inválida! O ano de início deve ser um número válido, de 4 dígitos.");
		}
		// Ano final do projeto:
		// Se não encontrar, e a palavra for "atual": (ano atual + 1)
		// (Para o programa identificar que o projeto ainda não acabou.)
		try {
			anoF = Integer.parseInt(dataF);
		} catch (NumberFormatException err) {
			if(dataF.toLowerCase().contains("atual")) {
				anoF = Year.now().getValue();
			} else {
				throw new InvalidDateException("ERRO: Data inválida! O ano final deve ser um número válido, de 4 dígitos, ou a palavra \"atual\" se o projeto ainda não foi finalizado.");
			}
		}
		datas[0] = String.format("01/01/%d", anoI);
		datas[1] = String.format("31/12/%d", anoF);
		return datas;
	}

	// testes:
	//public static void main(String[] args) {
		//DataCalendario p = new DataCalendario();
		// ## testes - anoF > anoI:
		//boolean terminou = !(p.checarDataPeriodo("01/01/2023", "08/05/2023"));
		//System.out.println("Terminou: " + terminou);
		// ## testes - criarDatasComAnos():
		//String teste[] = null;
		//try {
		//	teste = p.criarDatasComAnos("2012", "atual");
		//} catch (InvalidDateException e) {
		//	e.printStackTrace();
		//}
		//System.out.println(teste[0] + " " + teste[1]);
	//}
}
