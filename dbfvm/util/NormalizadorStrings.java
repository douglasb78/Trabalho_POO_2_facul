package dbfvm.util;

import java.text.Normalizer;

public class NormalizadorStrings {

	public String normalizar(String string) {
		// Deixa a string no formato "unicode normalizado" para fins de comparação:
		// "André Capões Caiçara ąęłżźćńś Æøæß" -> "Andre Capoes Caicara aezzcns" 
		string = Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").replaceAll("[^a-zA-Z ]+", "");
		return string;
	}
}
