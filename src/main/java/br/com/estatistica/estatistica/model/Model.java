package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.view.Observer;

public class Model {

	private List<Observer> observers = new LinkedList<Observer>();
	private static Model uniqueInstance;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String studentsData) {
		for (Observer observer : observers) {
			observer.update(chatId, studentsData);
		}
	}

	public void calculaMedia(Update update) {

		ArrayList<Double> valores = convertStringToDouble(update.message().text(), update);

		Double resultado, soma = 0.0;

		if (valores != null) {
			for (Double v : valores) {
				soma += v;
			}
			resultado = soma / valores.size();
			this.notifyObservers(update.message().chat().id(), "A média é igual a: " + resultado);
		}
	}

	public void calculaModa(Update update) {
		double ocorrenciasMaior = 0;
		double contagem = 0;
		Map<Double, Double> map = new HashMap<Double, Double>();

		ArrayList<Double> valores = convertStringToDouble(update.message().text(), update);
		if (valores != null) {
			Collections.sort(valores);
			for (int i = 1; i < valores.size(); i++) {
				if (valores.get(i).equals(valores.get(i - 1))) {
					contagem++;
				}
				else if(contagem > 1) {
					if(ocorrenciasMaior == 0) {
						map.put(valores.get(i),valores.get(i));
					}
					else if (contagem == ocorrenciasMaior) {
						map.put(valores.get(i),valores.get(i));
					}else if (contagem > ocorrenciasMaior) {
						map.clear();
						map.put(valores.get(i),valores.get(i));
						ocorrenciasMaior = contagem;
					}
					contagem = 0;
				}
			}

			if (map.size() > 1) {
				String modas = "";
				for (Double d : map.keySet()) {
					modas += String.valueOf(d) + "\n";
				}
				this.notifyObservers(update.message().chat().id(), "As modas são: \n" + modas);
			} else {
				this.notifyObservers(update.message().chat().id(), "Não existe moda");
			}
		}
	}

	public void calculaMediana(Update update) {
		ArrayList<Double> valores = convertStringToDouble(update.message().text(), update);
		if (valores != null) {
			if (valores.size() % 2 == 0) {
				Double mediana = (((valores.get(valores.size() / 2) - 1)) + (valores.get(valores.size() / 2))) / 2;
				this.notifyObservers(update.message().chat().id(), "A mediana é igual a: " + mediana);
			} else {
				int mediana = valores.get(valores.size() / 2).intValue();
				this.notifyObservers(update.message().chat().id(), "A mediana é igual a: " + mediana);
			}
		}
	}

	public ArrayList<Double> convertStringToDouble(String valor, Update update) {
		ArrayList<Double> valores = new ArrayList<Double>();
		String[] aux = valor.replaceAll(",", ".").split(";");

		try {
			for (String s : aux) {
				valores.add(Double.parseDouble(s));
			}
		} catch (Exception error) {
			notifyObservers(update.message().chat().id(),
					"Entrada inválida, por favor, digite os valores de entrada separados por ponto e virgula ;");
			valores = null;
		}
		return valores;
	}
}
