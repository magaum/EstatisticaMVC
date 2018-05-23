package br.com.estatistica.estatistica.model.utils;

import java.util.ArrayList;

import br.com.estatistica.estatistica.log.Log;

public class ModelUtils {

	public static void removerDadosMap(ArrayList<Double> modas) {
		if (modas.size() > 0) {
			for (int i = 0; i <= modas.size(); i++) {
				modas.remove(0);
			}
		}
	}

	public static ArrayList<Double> messageToDouble(String valor) {
		ArrayList<Double> valores = new ArrayList<Double>();
		String[] numeros = valor.replaceAll(",", ".").split(";");
		try {
			for (String numero : numeros) {
				valores.add(Double.parseDouble(numero));
			}
		} catch (Exception error) {
			Log.logErrorWriter("Valor inválido no input: " + error);
			valores = null;
		}
		return valores;
	}
}