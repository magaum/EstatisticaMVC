package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		Double maior 				= null;
		double ocorrenciasMaior 	= -1;
		int contagem 				= 1;
		Map<Double,Double> map = new HashMap<Double,Double>();
		
		
		valores.sort(null);
		for (int i = 1; i <= valores.size(); i++) {
			if (i < valores.size() && valores.get(i).equals(valores.get(i - 1))) {
				contagem++;
			
			} else if (contagem > ocorrenciasMaior) {
				map.remove(maior);
				maior = valores.get(i - 1);
				ocorrenciasMaior = contagem;
				
			} 
			
			if (contagem == ocorrenciasMaior) {
				map.put(valores.get(i - 1),valores.get(i - 1) );
				contagem = 1;
			} 
		}

		if(map.size() > 1) {
			String modas = "";
			System.out.println("Os valores das modas são: ");
			for (Double d : map.keySet()) {
				modas += String.valueOf(d) + "\n";
			}
			return "As modas são : \n" + modas;
		}
			return "A moda é igual a : " + maior ;
	}
}
