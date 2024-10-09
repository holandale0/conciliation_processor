package br.com.conciliation.processor.utils;

import java.util.stream.IntStream;

public class StringsComparisonUtil {

	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}

		if (str1 == null || str2 == null) {
			return false;
		}

		if (str1.isEmpty()) {
			return str2.isEmpty();
		}

		if (str1.equalsIgnoreCase(str2)) {
			return true;
		}

		return hasSomeSimilarity(str1, str2);
	}

	public static boolean hasSomeSimilarity(String str1, String str2) {
		// Remove espaços e converte ambas as strings para letras minúsculas
		String normalizedStr1 = str1.replaceAll("\\s+", "").toLowerCase();
		String normalizedStr2 = str2.replaceAll("\\s+", "").toLowerCase();

		// Verifica o comprimento das strings
		int maxLength = Math.max(normalizedStr1.length(), normalizedStr2.length());

		// Conta o número de caracteres que coincidem
		int matches = (int) IntStream.range(0, Math.min(normalizedStr1.length(), normalizedStr2.length()))
				.filter(i -> normalizedStr1.charAt(i) == normalizedStr2.charAt(i)).count();

		// Calcula o grau de semelhança
		double similarity = (double) matches / maxLength;

		// Retorna true se a similaridade for maior que 70%
		return similarity > 0.7;
	}

}
