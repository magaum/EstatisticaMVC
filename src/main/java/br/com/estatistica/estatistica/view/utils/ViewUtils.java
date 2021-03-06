package br.com.estatistica.estatistica.view.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import br.com.estatistica.estatistica.model.Log;

public class ViewUtils {

	public static String getTelegramToken() {
		Properties prop = new Properties();
		String token;
		try {
			prop.load(new FileInputStream("src/main/resources/token.properties"));
			token = prop.getProperty("token");
		} catch (IOException e) {
			Log.logFatalWriter("TELEGRAM TOKEN ERROR: " + e);
			token = null;
		}
		return token;
	}
}
