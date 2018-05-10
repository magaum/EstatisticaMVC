package br.com.estatistica.estatistica.view;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import br.com.estatistica.estatistica.controller.ActionController;
import br.com.estatistica.estatistica.controller.ExerciseControllerMedia;
import br.com.estatistica.estatistica.controller.ExerciseControllerMediana;
import br.com.estatistica.estatistica.controller.ExerciseControllerModa;
import br.com.estatistica.estatistica.controller.HistoricController;
import br.com.estatistica.estatistica.model.Model;

public class View implements Observer {

	private Model model;
	int queue = 0;
	boolean searchBehaviour;

	TelegramBot bot; // TelegramBotAdapter.build(token);

	// Object that receives messages
	GetUpdatesResponse updatesResponse;
	// Object that send responses
	SendResponse sendResponse;
	// Object that manage chat actions like "typing action"
	BaseResponse baseResponse;

	ActionController actionController; // Strategy Pattern -- connection View -> Controller

	public View(String token, Model model) {
		this.model = model;
		bot = new TelegramBot(token);
	}

	public void setController(ActionController actionController) { // Strategy Pattern
		this.actionController = actionController;
	}

	public void receiveUsersMessages() {
		bot.setUpdatesListener(new UpdatesListener() {
			public int process(List<Update> updates) {
				try {
					execute(updates);
				} catch (Exception e) {
					System.out.println("Erro ao processar mensagens: " + e);
				}
				return UpdatesListener.CONFIRMED_UPDATES_ALL;
			}
		});
	}

	public void execute(List<Update> updates) {

		for (Update update : updates) {
			String nome = update.message().chat().firstName();
			String message = update.message().text();
			long chatId = update.message().chat().id();
			try {
				System.out.println(queue++ + " mensagem processada\n" + "ID: " + chatId + "\n" + "Usuário: " + nome
						+ "\n" + "Mensagem: " + message + "\n");
			} catch (Exception e) {
				System.out.println("Log error: " + e);
			}

			if (message.equalsIgnoreCase("Media")) {
				setController(new ExerciseControllerMedia(model, this));
				sendResponse = bot.execute(new SendMessage(chatId,
						"Entendi, media! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.searchBehaviour = true;

			} else if (message.equalsIgnoreCase("Mediana")) {
				setController(new ExerciseControllerMediana(model, this));
				sendResponse = bot.execute(new SendMessage(chatId,
						"Entendi, mediana! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.searchBehaviour = true;

			} else if (message.equalsIgnoreCase("Moda")) {
				setController(new ExerciseControllerModa(model, this));
				sendResponse = bot.execute(new SendMessage(chatId,
						"Entendi, moda! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.searchBehaviour = true;

			} else if (message.equalsIgnoreCase("Carregar")) {
				setController(new HistoricController(model, this));
				sendResponse = bot
						.execute(new SendMessage(chatId, "Estou consultando valores armazenados, já respondo"));
				this.searchBehaviour = true;

			} else if (message.equals("/start")) {
				Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
						new String[] { "Media", "Moda", "Mediana", "Carregar" }).oneTimeKeyboard(false)
								.resizeKeyboard(true).selective(true);
				bot.execute(new SendMessage(update.message().chat().id(), "Ola, " + nome + ", o que deseja calcular?")
						.replyMarkup(replyKeyboardMarkup));
			} else if (this.searchBehaviour == false) {
				bot.execute(new SendMessage(chatId,
						"Não entendi o que você disse " + nome + ", digite ou selecione media, moda ou mediana"));
			}
			if (this.searchBehaviour == true) {
				this.callController(update);
			}
		}
	}

	public void callController(Update update) {
		this.actionController.action(update);
	}

	public void update(long chatId, String studentsData) {
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}

	public void sendTypingMessage(Update update) {
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}
