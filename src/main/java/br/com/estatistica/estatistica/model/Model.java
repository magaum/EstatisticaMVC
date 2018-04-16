package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
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
		
		ArrayList<Double> valores 	= convertStringToDouble(update.message().text());
		
		Double resultado, soma 		= 0.0;

		for (Double v : valores) {
			soma += v;
		}
		resultado = soma / valores.size();
		this.notifyObservers(update.message().chat().id(), "A média é igual a : " + resultado);
	}

	public void calculaModa(Update update) {
		int contador 				= 0;
		double maior 				= 0;
		double ocorrenciaMaior 		= 0;
		ArrayList<Double> modas 	= new ArrayList<>();
		ArrayList<Double> valores 	= convertStringToDouble(update.message().text());

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
			String resultado = "os valores da moda são : ";
			for (Double d : modas) {
				resultado += d + ", ";
			}
			this.notifyObservers(update.message().chat().id(), resultado);
		} else
			this.notifyObservers(update.message().chat().id(), "o valor da moda é igual a : " + maior);
	}

	public  void removerDadosMap(ArrayList<Double> modas) {
		if (modas.size() > 0) {
			for (int i = 0; i <= modas.size(); i++) {
				modas.remove(0);
			}
		}
	}
	
	public void calculaMediana(Update update) {
		ArrayList<Double> valores = convertStringToDouble(update.message().text());
		
		if(valores.size() % 2 == 0) {
			Double mediana = (((valores.get(valores.size()  / 2) - 1)) + 
					(valores.get(valores.size() / 2) )) / 2;  
			this.notifyObservers(update.message().chat().id(), "A mediana é igual a : " + mediana);
		}else {
			int mediana =  valores.get(valores.size() / 2).intValue(); 
			this.notifyObservers(update.message().chat().id(), "A mediana é igual a : " 
					+ mediana);
		}
	}

	public ArrayList<Double> convertStringToDouble(String valor ) {
		ArrayList<Double> valores 	= new ArrayList<>();
		String[] aux 				= valor.replaceAll(",", ".").split(";");
		
		for (String s : aux) {
			valores.add(Double.parseDouble(s));
		}
		return valores;
	}
}


