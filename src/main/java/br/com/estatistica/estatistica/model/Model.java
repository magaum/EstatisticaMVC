package br.com.estatistica.estatistica.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.model.utils.ModelUtils;
import br.com.estatistica.estatistica.view.Observer;

public class Model {

	private List<Observer> observers = new LinkedList<Observer>();
	private static Model model;
	private ModelDAO db4o = new ModelDAO();
	private long chatId;
	private ArrayList<Double> values;

	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String data, boolean isImage, boolean isDocument, File file) {
		for (Observer observer : observers) {
			observer.update(chatId, data, isImage, isDocument, file);
		}
	}

	public void getHistoric(Update update) {
		String name = update.message().chat().firstName();
		long chatId = update.message().chat().id();
		File pdf = Pdf.createPdf(update);
		if (pdf == null) {
			this.notifyObservers(chatId, "Não encontrei nada aqui " + name + " desculpe \uD83D\uDE1E", false, false,
					pdf);
		} else {
			this.notifyObservers(chatId, "Encontrei seu histórico " + name, false, true, pdf);
		}
	}

	public void computeMean(Update update) {

		chatId = update.message().chat().id();
		values = ModelUtils.messageToDouble(update.message().text());
		Double result;
		Double sum = 0.0;
		if (values != null) {
			for (Double v : values) {
				sum += v;
			}
			result = sum / values.size();
			File file = BoxPlot.generateBoxPlot(update, "Media");
			this.notifyObservers(chatId, "A média é igual a: " + result, true, false, file);
			Historic historic = new Historic(values, chatId, "Media", result, file);

			if (!db4o.addHistoric(historic)) {
				Log.logErrorWriter("Erro ao salvar no banco");
				Log.logErrorWriter("Classe: "+this.getClass().getSimpleName());
			}
		} else {
			this.notifyObservers(chatId, update.message().chat().firstName()
					+ ", você digitou algo errado, não consegui calcular os valores \uD83D\uDE1E escolhe ou digita moda, media ou mediana para eu tentar de novo?",
					false, false, null);
		}

	}

	public void computeMode(Update update) {

		chatId = update.message().chat().id();
		int counter = 0;
		double bigger = 0;
		double biggerOccurrence = 0;
		String result = "";
		ArrayList<Double> modes = new ArrayList<>();
		values = ModelUtils.messageToDouble(update.message().text());
		if (values != null) {
			Historic historic = new Historic(values, chatId, "Moda",
					new File("files/imgs/boxPlots/" + chatId + ".png"));
			Collections.sort(values);
			for (int i = 1; i <= values.size(); i++) {
				if (i < values.size() && values.get(i).equals(values.get(i - 1))) {
					counter++;
					continue;
				} else if (counter > 0) {
					if (counter > biggerOccurrence) {
						ModelUtils.removeMapData(modes);
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
				result = "Os valores da moda são: ";
				for (Double mode : modes) {
					result += mode;
					if (mode != modes.get(modes.size() - 1))
						result += ", ";
				}
				historic.setResultArr(modes);
			} else if (modes.size() == 1) {
				result = "Calculei a moda, o resultado é: " + bigger;
				historic.setResult(bigger);
			} else {
				this.notifyObservers(chatId, "Não exite moda nesses valores", false, false, null);
				historic.setModeNull();
			}
			File file = BoxPlot.generateBoxPlot(update, "Moda");
			this.notifyObservers(chatId, result, true, false, file);
			historic.setBoxPlot(file);

			if (!db4o.addHistoric(historic)) {
				Log.logErrorWriter("Erro ao salvar no banco");
				Log.logErrorWriter("Classe: "+this.getClass().getSimpleName());
			}
		} else {
			this.notifyObservers(chatId, update.message().chat().firstName()
					+ ", você digitou algo errado, não consegui calcular os valores \uD83D\uDE1E escolhe ou digita moda, media ou mediana para eu tentar de novo?",
					false, false, null);
		}
	}

	public void computeMedian(Update update) {
		long chatId = update.message().chat().id();
		ArrayList<Double> values = ModelUtils.messageToDouble(update.message().text());
		String result = "A mediana é igual a: ";
		File file = BoxPlot.generateBoxPlot(update, "Mediana");
		Historic historic = new Historic(values, chatId, "Mediana", file);
		if (values != null) {
			if (values.size() % 2 == 0) {
				Double median = (((values.get(values.size() / 2) - 1)) + (values.get(values.size() / 2))) / 2;
				result += median;
				historic.setResult(median);
			} else {
				int median = values.get(values.size() / 2).intValue();
				result += median;
				historic.setResult((double) median);
			}
			this.notifyObservers(update.message().chat().id(), result, true, false, file);
			if (!db4o.addHistoric(historic)) {
				Log.logErrorWriter("Erro ao salvar no banco");
				Log.logErrorWriter("Classe: "+this.getClass().getSimpleName());
			}
		} else {
			this.notifyObservers(chatId, update.message().chat().firstName()
					+ ", você digitou algo errado, não consegui calcular os valores \uD83D\uDE1E escolhe ou digita moda, media ou mediana para eu tentar de novo?",
					false, false, null);
		}
	}

}
