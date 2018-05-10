package br.com.estatistica.estatistica.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.log.Logs;
import br.com.estatistica.estatistica.model.Historic;

public class ModelUtils {

	public static void removerDadosMap(ArrayList<Double> modas) {
		if (modas.size() > 0) {
			for (int i = 0; i <= modas.size(); i++) {
				modas.remove(0);
			}
		}
	}

	public static ArrayList<Double> convertStringToDouble(String valor, Update update) {
		ArrayList<Double> valores = new ArrayList<Double>();
		String[] numeros = valor.replaceAll(",", ".").split(";");
		try {
			for (String numero : numeros) {
				valores.add(Double.parseDouble(numero));
			}
		} catch (Exception error) {
			Logs.logErrorWriter("Valor inválido no input: "+error);
			valores = null;
		}
		return valores;
	}
	
	public static String formattedJson(List<Historic> userHistoric) {
		Collections.sort(userHistoric);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(userHistoric);
		return json;
	}
}