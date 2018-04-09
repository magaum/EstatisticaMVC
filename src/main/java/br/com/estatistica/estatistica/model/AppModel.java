package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.view.Observer;

public class AppModel {

	private static AppModel instanceModel;
	private List<Observer> observers = new LinkedList<Observer>();
	
	public static AppModel getInstance(){
		if(instanceModel == null){
			instanceModel = new AppModel();
		}
		return instanceModel;
	}
	
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	
	public String calculaMedia(ArrayList<Double> valores) {
		Double resultado, soma = null;

		for (Double v : valores) {
			soma += v;
		}
		resultado = soma / valores.size();
		return "A média é igual a : " + resultado;
	}

	public String calculaModa(Update update) {
		ArrayList<Double> valores = null;
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
