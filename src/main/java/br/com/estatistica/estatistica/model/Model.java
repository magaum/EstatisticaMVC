package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.view.Observer;

public class Model {

	private List<Observer> observers = new LinkedList<Observer>();
	private static Model uniqueInstance;

	ObjectContainer bancoProblemas = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "db/bancoProblemas.db4o");

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public boolean addHistorico(Historico historico) {
		try {
			bancoProblemas.store(historico);
			bancoProblemas.commit();
			return true;
		} catch (Exception e) {
			System.out.println("Erro ao salvar no banco: " + e);
			return false;
		}
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String data) {
		for (Observer observer : observers) {
			observer.update(chatId, data);
		}
	}

	public void calculaMedia(Update update) {

		long chatId = update.message().chat().id();
		String username = update.message().chat().username();
		ArrayList<Double> valores = convertStringToDouble(update.message().text(), update);
		Double resultado, soma = 0.0;
		Historico historico = new Historico(valores, chatId, username);

		if (valores != null) {
			for (Double v : valores) {
				soma += v;
			}
			resultado = soma / valores.size();
			if (!addHistorico(historico)) {
				System.out.println("Erro ao salvar no banco");
			}
			this.notifyObservers(chatId, "A média é igual a: " + resultado);
		}

	}

	public void calculaModa(Update update) {

		long chatId = update.message().chat().id();
		String username = update.message().chat().username();
		int contador = 0;
		double maior = 0;
		double ocorrenciaMaior = 0;
		ArrayList<Double> modas = new ArrayList<>();
		ArrayList<Double> valores = convertStringToDouble(update.message().text(), update);
		Collections.sort(valores);
		Historico historico = new Historico(valores, chatId, username);
		for (int i = 1; i <= valores.size(); i++) {
			if (i < valores.size() && valores.get(i).equals(valores.get(i - 1))) {
				contador++;
				continue;
			} else if (contador > 0) {
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

		if(!addHistorico(historico)) {
			System.out.println("Erro ao salvar no banco");
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
	}

	public void removerDadosMap(ArrayList<Double> modas) {
		if (modas.size() > 0) {
			for (int i = 0; i <= modas.size(); i++) {
				modas.remove(0);
			}

		}
	}

	public void calculaMediana(Update update) {
		long chatId = update.message().chat().id();
		String username = update.message().chat().username();
		ArrayList<Double> valores = convertStringToDouble(update.message().text(), update);
		Historico historico = new Historico(valores, chatId, username);
		if(!addHistorico(historico)) {
			System.out.println("Erro ao salvar no banco");
		}
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
