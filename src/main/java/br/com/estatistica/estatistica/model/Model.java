package br.com.estatistica.estatistica.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.log.Logs;
import br.com.estatistica.estatistica.model.utils.ModelUtils;
import br.com.estatistica.estatistica.view.Observer;

public class Model {

	private List<Observer> observers = new LinkedList<Observer>();
	private static Model uniqueInstance;
	private ModelDAO db4o = new ModelDAO();
	long chatId;
	String username;
	ArrayList<Double> values;

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

	public void sendPhotoToObservers(long chatId) {
		for (Observer observer : observers) {
			observer.sendImage(chatId);
		}
	}

	public void sendFileToObservers(long chatId) {
		for (Observer observer : observers) {
			observer.sendDocument(chatId);
		}
	}

	public void getHistoric(Update update) {
		String name = update.message().chat().firstName();
		long chatId = update.message().chat().id();
		if (ModelUtils.createPdf(update)) {
			this.sendFileToObservers(chatId);
		} else {
			this.notifyObservers(update.message().chat().id(),
					"Não encontrei nada aqui " + name + " desculpe \uD83D\uDE1E");
		}
	}

	public void computeMean(Update update) {

		chatId = update.message().chat().id();
		values = ModelUtils.convertStringToDouble(update.message().text());
		Double result, sum = 0.0;
		if (values != null) {
			for (Double v : values) {
				sum += v;
			}
			result = sum / values.size();
			this.notifyObservers(chatId, "A média é igual a: " + result);
			Historic historic = new Historic(values, chatId, "media", result, new File("boxPlots/" + chatId + ".png"));

			if (!db4o.addHistoric(historic)) {
				Logs.logErrorWriter("Erro ao salvar no banco");
			}
		} else {
			this.notifyObservers(chatId, update.message().chat().firstName()
					+ ", você digitou algo errado, não consegui calcular os valores \uD83D\uDE1E escolhe ou digita moda, media ou mediana para eu tentar de novo?");
		}

	}

	public void computeMode(Update update) {

		chatId = update.message().chat().id();
		int counter = 0;
		double bigger = 0;
		double biggerOccurrence = 0;
		ArrayList<Double> modes = new ArrayList<>();
		values = ModelUtils.convertStringToDouble(update.message().text());
		if (values != null) {
			Historic historic = new Historic(values, chatId, "mediana", new File("boxPlots/" + chatId + ".png"));
			Collections.sort(values);
			for (int i = 1; i <= values.size(); i++) {
				if (i < values.size() && values.get(i).equals(values.get(i - 1))) {
					counter++;
					continue;
				} else if (counter > 0) {
					if (counter > biggerOccurrence) {
						ModelUtils.removerDadosMap(modes);
						bigger = values.get(i - 1);
						biggerOccurrence = counter;
						modes.add(bigger);
					} else if (counter == biggerOccurrence) {
						modes.add(values.get(i - 1));
					}
				}
				counter = 0;
			}
			if (modes.size() > 1) {
				String result = "Os valores da moda são: ";
				for (Double mode : modes) {
					result += mode + ", ";
				}
				historic.setResultArr(modes);
				this.notifyObservers(chatId, result);
			} else if (modes.size() == 1) {
				this.notifyObservers(chatId, "Calculei a moda, o valor da é igual a: " + bigger);
				historic.setResult(bigger);
			} else {
				this.notifyObservers(chatId, "Não exite moda nesses valores");
			}

			if (!db4o.addHistoric(historic)) {
				Logs.logErrorWriter("Erro ao salvar no banco");
			}
		} else {
			this.notifyObservers(chatId, update.message().chat().firstName()
					+ ", você digitou algo errado, não consegui calcular os valores \uD83D\uDE1E escolhe ou digita moda, media ou mediana para eu tentar de novo?");
		}
	}

	public void computeMedian(Update update) {
		long chatId = update.message().chat().id();
		ArrayList<Double> values = ModelUtils.convertStringToDouble(update.message().text());
		Historic historic = new Historic(values, chatId, "mediana", new File("boxPlots/" + chatId + ".png"));
		if (values != null) {
			if (values.size() % 2 == 0) {
				Double median = (((values.get(values.size() / 2) - 1)) + (values.get(values.size() / 2))) / 2;
				this.notifyObservers(update.message().chat().id(), "A mediana é igual a: " + median);
				historic.setResult(median);
			} else {
				int median = values.get(values.size() / 2).intValue();
				this.notifyObservers(update.message().chat().id(), "A mediana é igual a: " + median);
				historic.setResult((double) median);
			}
			if (!db4o.addHistoric(historic)) {
				Logs.logErrorWriter("Erro ao salvar no banco");
			}
		} else {
			this.notifyObservers(chatId, update.message().chat().firstName()
					+ ", você digitou algo errado, não consegui calcular os valores \uD83D\uDE1E escolhe ou digita moda, media ou mediana para eu tentar de novo?");
		}
	}

	public void generateBoxPlot(Update update, String operation) throws IOException {
		long chatId = update.message().chat().id();
		DefaultBoxAndWhiskerCategoryDataset boxPlot = new DefaultBoxAndWhiskerCategoryDataset();
		List<Double> values = ModelUtils.convertStringToDouble(update.message().text());
		boxPlot.add(values, "", "Valores");
		JFreeChart jFreeChart = ChartFactory.createBoxAndWhiskerChart("Box Plot", operation, "Coluna", boxPlot, false);
		ChartUtilities.saveChartAsPNG(new File("boxPlots/" + chatId + ".png"), jFreeChart, 800, 800);
		this.sendPhotoToObservers(chatId);
	}

}
