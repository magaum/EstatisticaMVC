package br.com.estatistica.estatistica.model.utils;

import java.util.ArrayList;

import br.com.estatistica.estatistica.model.Log;

public class ModelUtils {

	public static void removeMapData(ArrayList<Double> mode) {
		if (mode.size() > 0) {
			for (int i = 0; i <= mode.size(); i++) {
				mode.remove(0);
			}
		}
	}

	public static ArrayList<Double> messageToDouble(String value) {
		ArrayList<Double> values = new ArrayList<Double>();
		try {
			String[] numbers = value.replaceAll(",", ".").split(";");
			for (String number : numbers) {
				values.add(Double.parseDouble(number));
			}
		} catch (Exception error) {
			Log.logErrorWriter("Valor inválido no input: " + error);
			values = null;
		}
		return values;
	}
}