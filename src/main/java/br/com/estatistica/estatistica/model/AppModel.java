package br.com.estatistica.estatistica.model;

import java.util.ArrayList;

public class AppModel {

	public String calculaMedia(ArrayList<Double> valores) {
		Double resultado, soma = null;

		for (Double v : valores) {
			soma += v;
		}
		resultado = soma / valores.size();
		return "A média é igual a : " + resultado;
	}

	public String calculaModa(ArrayList<Double> valores) {
		Double maior = null;
		double ocorrenciasMaior = -1;
		int contagem = 1;
		
		valores.sort(null);
		for (int i = 1; i <= valores.size(); i++) {
			if (i < valores.size() && valores.get(i).equals(valores.get(i - contagem))) {
				contagem++;
			} else if (contagem > ocorrenciasMaior) {
				maior = valores.get(i - 1);
				ocorrenciasMaior = contagem;
			} 
		}
			return "A moda é o elemento que mais aparece na amostra, " + "logo o número que mais se repetiu foi o : "
					+ maior + "\ntotalizando " + ocorrenciasMaior + " ocorencias na amostra.";
	}
}
