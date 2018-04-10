package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Teste {

	public static void main(String[] args) {
		ArrayList<Double> valores = new ArrayList<>();
		
		valores.add(1.0);
		valores.add(1.0);
		valores.add(1.0);
		
		valores.add(3.0);
		valores.add(3.0);
		valores.add(3.0);
		
		valores.add(5.0);
		valores.add(5.0);
		valores.add(5.0);
		valores.add(5.0);
	
		calculaModa(valores);
	}


	public static void calculaModa(ArrayList<Double> valores) {
		Double maior 			= null;
		double ocorrenciasMaior = -1;
		int contagem 			= 1;
		Map<Double, Double> map = new HashMap<Double, Double>();

		valores.sort(null);
		for (int i = 1; i <= valores.size(); i++) {
			if (i < valores.size() && valores.get(i).equals(valores.get(i - 1))) {
				contagem++;

			} else if (contagem > ocorrenciasMaior) {
				map.remove(maior);
				maior 				= valores.get(i - 1);
				ocorrenciasMaior 	= contagem;
			}

			if (contagem == ocorrenciasMaior) {
				map.put(valores.get(i - 1), valores.get(i - 1));
				contagem = 1;
			}
		}

		if (map.size() > 1) {
			String modas = "";
			for (Double d : map.keySet()) {
				modas += String.valueOf(d) + "\n";
			}
			System.out.println("As modas são : \n" + modas);
		} else {
			System.out.println("A moda é igual a : " + maior);
		}
	}


}
