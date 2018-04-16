package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Teste {

	public static void main(String[] args) {
<<<<<<< HEAD:src/test/java/br/com/estatistica/estatistica/model/Teste.java
		ArrayList<Double> valores = new ArrayList<Double>();
		
=======
		ArrayList<Double> valores = new ArrayList<>();

>>>>>>> 81fa64dc8195db9e1214329fac57a301e69c34cb:src/main/java/br/com/estatistica/estatistica/model/Teste.java
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

		calcular(valores);
	}

	public static void calculaModa(ArrayList<Double> valores) {
		Double maior = null;
		double ocorrenciasMaior = -1;
		int contagem = 1;
		Map<Double, Double> map = new HashMap<Double, Double>();

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

	public static void calcular(ArrayList<Double> valores) {
		int contador 			= 0;
		Double maior 			= null;
		double ocorrenciaMaior 	= 0.0;
		ArrayList<Double> modas = new ArrayList<>();

		valores.sort(null);

		for (int i = 1; i <= valores.size(); i++) {
			if (i < valores.size() && valores.get(i).equals(valores.get(i - 1))) {
				contador++;
				continue;
			} else {
				if (contador > ocorrenciaMaior) {
					removerDadosMap(modas);
					maior = valores.get(i - 1);
					ocorrenciaMaior = contador;
					modas.add(maior);
				} else if (contador == ocorrenciaMaior) {
					modas.add(valores.get(i - 1));
				}
			}
			contador = 0;
		}

		if (modas.size() > 1) {
			System.out.println("os valores do map são : ");
			for (Double d : modas) {
				System.out.print(d + ", ");
			}
		} else
			System.out.println("o valor da moda é igual a : " + maior);
	}

	public static void removerDadosMap(ArrayList<Double> modas) {
		if (modas.size() > 0) {
			for (int i = 0; i <= modas.size(); i++) {
				modas.remove(0);
			}
		}
	}

}
