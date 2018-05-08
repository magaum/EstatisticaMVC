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

import br.com.estatistica.estatistica.controller.ExerciseController;
import br.com.estatistica.estatistica.controller.ExerciseControllerMedia;
import br.com.estatistica.estatistica.controller.ExerciseControllerMediana;
import br.com.estatistica.estatistica.controller.ExerciseControllerModa;
import br.com.estatistica.estatistica.model.Model;

public class View implements Observer {

	private Model model;
	int queuesIndex = 0;
	int queue = 0;
	boolean searchBehaviour = false;

	TelegramBot bot; // TelegramBotAdapter.build(token);

	// Object that receives messages
	GetUpdatesResponse updatesResponse;
	// Object that send responses
	SendResponse sendResponse;
	// Object that manage chat actions like "typing action"
	BaseResponse baseResponse;

	ExerciseController exerciseController; // Strategy Pattern -- connection View -> Controller

	public View(String token, Model model) {
		this.model = model;
		bot = new TelegramBot(token);
	}

	public void setController(ExerciseController exerciseController) { // Strategy Pattern
		this.exerciseController = exerciseController;
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
			try {
				System.out.println(queue+++" mensagem processada\n"
						+ "ID: "+update.message().chat().id()+"\n"
						+ "Usuário: "+update.message().chat().username()+"\n"
						+ "Mensagem: "+update.message().text()+"\n"
						);
			}catch (Exception e) {
				System.out.println("Log error: "+e);
			}
			queuesIndex = update.updateId() + 1;

			if (this.searchBehaviour == true) {
				this.callController(update);

			} else if (update.message().text().equalsIgnoreCase("Media")) {
				setController(new ExerciseControllerMedia(model, this));
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
						"Digite os valores de entrada separados por ponto e virgula ;"));
				this.searchBehaviour = true;

			} else if (update.message().text().equalsIgnoreCase("Mediana")) {
				setController(new ExerciseControllerMediana(model, this));
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
						"Digite os valores de entrada separados por ponto e virgula ;"));
				this.searchBehaviour = true;

			} else if (update.message().text().equalsIgnoreCase("Moda")) {
				setController(new ExerciseControllerModa(model, this));
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
						"Digite os valores de entrada separados por ponto e virgula ;"));
				this.searchBehaviour = true;

			} else if (update.message().text().equals("/start")) {
				Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(new String[] { "Media", "Moda", "Mediana" })
						.oneTimeKeyboard(false).resizeKeyboard(true).selective(true);
				bot.execute(new SendMessage(update.message().chat().id(), "O que deseja calcular?")
						.replyMarkup(replyKeyboardMarkup));
			} else {
				bot.execute(new SendMessage(update.message().chat().id(),
						"comando invalido, selecione ou digite media, moda ou mediana"));
			}
		}
	}

	public void callController(Update update) {
		this.exerciseController.calculate(update);
	}

	public void update(long chatId, String studentsData) {
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}

	public void sendTypingMessage(Update update) {
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}
