package br.com.estatistica.estatistica;

import java.io.FileInputStream;
import java.util.Properties;

import com.pengrad.telegrambot.TelegramBot;

import br.com.estatistica.estatistica.model.AppModel;
import br.com.estatistica.estatistica.view.AppView;

public class App {

	public static AppModel model;

	public static void main(String[] args) {
		model = AppModel.getInstance();
		AppView view = new AppView(model,initializeView());
		model.registerObserver(view);
		view.receiveUsersMessages();
	}

	public static TelegramBot initializeView() {
		TelegramBot bot;
		Properties properties = new Properties();
		try {
			properties.load(
					new FileInputStream("src\\main\\java\\br\\com\\estatistica\\estatistica\\view\\token.properties"));

		} catch (Exception e) {
			
		}
		bot = new TelegramBot(properties.getProperty("token"));
		return bot;
	}
}
