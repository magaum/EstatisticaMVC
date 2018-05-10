package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.model.utils.ModelUtils;
import br.com.estatistica.estatistica.view.Observer;

public class Model {

	private List<Observer> observers = new LinkedList<Observer>();
	private static Model uniqueInstance;
	private ModelDAO db4o = new ModelDAO();
	long chatId;
	String username;
	ArrayList<Double> valores;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String data) {
		for (Observer observer : observers) {
			observer.update(chatId, data);
		}
	}

	public void getHistoric(Update update) {
		String nome = update.message().chat().firstName();
		List<Historic> userHistoric = ModelDAO.getHistoric(update);
		if (userHistoric != null) {
			String json = ModelUtils.formattedJson(userHistoric);
			this.notifyObservers(update.message().chat().id(), "Encontrei os valores para você " + nome + "\n" + json);
		} else {
			this.notifyObservers(update.message().chat().id(), "Não encontrei nada aqui " + nome + " desculpe :c");
		}
	}

	public void calculaMedia(Update update) {

		chatId = update.message().chat().id();
		valores = ModelUtils.convertStringToDouble(update.message().text(), update);
		Double resultado, soma = 0.0;
		Historic historic = new Historic(valores, chatId, "media");

		if (valores != null) {
			for (Double v : valores) {
				soma += v;
			}
			resultado = soma / valores.size();
			this.notifyObservers(chatId, "A média é igual a: " + resultado);
			if (!db4o.addHistoric(historic)) {
				System.out.println("Erro ao salvar no banco");
			}
		}

	}

	public void calculaModa(Update update) {

		chatId = update.message().chat().id();
		int contador = 0;
		double maior = 0;
		double ocorrenciaMaior = 0;
		ArrayList<Double> modas = new ArrayList<>();
		valores = ModelUtils.convertStringToDouble(update.message().text(), update);
		Collections.sort(valores);
		Historic historic = new Historic(valores, chatId, "moda");
		if (valores != null) {
			for (int i = 1; i <= valores.size(); i++) {
				if (i < valores.size() && valores.get(i).equals(valores.get(i - 1))) {
					contador++;
					continue;
				} else if (contador > 0) {
					if (contador > ocorrenciaMaior) {
						ModelUtils.removerDadosMap(modas);
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
				this.notifyObservers(chatId, resultado);
			} else if (modas.size() == 1) {
				this.notifyObservers(chatId, "o valor da moda é igual a : " + maior);
			} else {
				this.notifyObservers(chatId, "Não exite moda");
			}

			if (!db4o.addHistoric(historic)) {
				System.out.println("Erro ao salvar no banco");
			}
		}
	}

	public void calculaMediana(Update update) {
		long chatId = update.message().chat().id();
		ArrayList<Double> valores = ModelUtils.convertStringToDouble(update.message().text(), update);
		Historic historico = new Historic(valores, chatId, "mediana");
		if (valores != null) {
			if (valores.size() % 2 == 0) {
				Double mediana = (((valores.get(valores.size() / 2) - 1)) + (valores.get(valores.size() / 2))) / 2;
				this.notifyObservers(update.message().chat().id(), "A mediana é igual a: " + mediana);
			} else {
				int mediana = valores.get(valores.size() / 2).intValue();
				this.notifyObservers(update.message().chat().id(), "A mediana é igual a: " + mediana);
			}
			if (!db4o.addHistoric(historico)) {
				System.out.println("Erro ao salvar no banco");
			}
		}
	}

}
