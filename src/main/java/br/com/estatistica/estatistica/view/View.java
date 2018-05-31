package br.com.estatistica.estatistica.view;

import java.io.File;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import br.com.estatistica.estatistica.controller.ActionController;
import br.com.estatistica.estatistica.controller.ExerciseControllerMean;
import br.com.estatistica.estatistica.controller.ExerciseControllerMedian;
import br.com.estatistica.estatistica.controller.ExerciseControllerMode;
import br.com.estatistica.estatistica.controller.HistoricController;
import br.com.estatistica.estatistica.model.Log;
import br.com.estatistica.estatistica.model.Model;

public class View implements Observer {

	private Model model;
	private boolean waitUserInput = true;
	private TelegramBot bot;

	// Object that send responses
	private SendResponse sendResponse;
	// Strategy Pattern -- connection View -> Controller
	private ActionController actionController;

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
					Log.logWarnWriter("Erro ao processar mensagens: " + e);
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
				Log.logInfoWriter("Mensagem processada");
				Log.logInfoWriter("Nome do usuário: " + nome);
				Log.logInfoWriter("Mensagem: " + message);
				Log.logInfoWriter("ChatID: " + chatId);
			} catch (Exception e) {
				Log.logErrorWriter("Algum erro ocorreu ao processar a mensagem!" + e);
			}

			if (message.equalsIgnoreCase("Relatório de requisições")) {
				setController(new HistoricController(model, this));
				sendResponse = bot
						.execute(new SendMessage(chatId, "Estou consultando valores armazenados, já respondo"));
				this.waitUserInput = false;

			}
			if (!this.waitUserInput) {
				this.callController(update);
			} else if (message.equalsIgnoreCase("Media")) {
				setController(new ExerciseControllerMean(model, this));
				sendResponse = bot.execute(new SendMessage(chatId,
						"Entendi, media! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.waitUserInput = false;

			} else if (message.equalsIgnoreCase("Mediana")) {
				setController(new ExerciseControllerMedian(model, this));
				sendResponse = bot.execute(new SendMessage(chatId,
						"Entendi, mediana! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.waitUserInput = false;
			} else if (message.equalsIgnoreCase("Moda")) {
				setController(new ExerciseControllerMode(model, this));
				sendResponse = bot.execute(new SendMessage(chatId,
						"Entendi, moda! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.waitUserInput = false;

			} else if ("/start".equals(message)) {
				Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
						new String[] { "Media", "Moda", "Mediana", "Relatório de requisições" }).oneTimeKeyboard(false)
								.resizeKeyboard(true).selective(true);
				bot.execute(new SendMessage(update.message().chat().id(), "Ola, " + nome + ", o que deseja calcular?")
						.replyMarkup(replyKeyboardMarkup));
			} else {
				bot.execute(new SendMessage(chatId, "Não entendi o que você disse " + nome
						+ " 🤔 digite ou selecione media, moda, mediana ou relatório de resquisições"));
			}
		}

	}

	public void callController(Update update) {
		this.actionController.action(update);
	}

	public void sendTypingMessage(Update update) {
		BaseResponse baseResponse = bot
				.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

	@Override
	public void update(long chatId, String data) {
		sendResponse = bot.execute(new SendMessage(chatId, data));
		this.waitUserInput = true;
	}

	@Override
	public void sendImage(long chatId,File img) {
		sendResponse = bot.execute(new SendPhoto(chatId, img));
		this.waitUserInput = true;
	}

	@Override
	public void sendDocument(long chatId, File file) {
		sendResponse = bot.execute(new SendDocument(chatId, file).fileName("historico.pdf"));
		this.waitUserInput = true;
	}
}
