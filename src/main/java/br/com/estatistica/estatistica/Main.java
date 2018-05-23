package br.com.estatistica.estatistica;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import br.com.estatistica.estatistica.model.Log;
import br.com.estatistica.estatistica.model.Model;
import br.com.estatistica.estatistica.view.View;

public class Main {

	private static Model model;

	public static void main(String[] args) {

		model = Model.getInstance();
		View view = new View(getTelegramToken(), model);
		model.registerObserver(view); // connection Model -> View
		view.receiveUsersMessages();
	}

	public static String getTelegramToken() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("src/main/resources/token.properties"));
			return prop.getProperty("token");
		} catch (IOException e) {
			Log.logFatalWriter("TELEGRAM TOKEN ERROR: " + e);
			return null;
		}
	}

}
