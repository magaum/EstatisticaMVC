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

import br.com.estatistica.estatistica.controller.ActionController;
import br.com.estatistica.estatistica.controller.ExerciseControllerMean;
import br.com.estatistica.estatistica.controller.ExerciseControllerMedian;
import br.com.estatistica.estatistica.controller.ExerciseControllerMode;
import br.com.estatistica.estatistica.controller.HistoricController;
import br.com.estatistica.estatistica.model.Log;
import br.com.estatistica.estatistica.model.Model;
import br.com.estatistica.estatistica.view.utils.ViewUtils;

public class View implements Observer {

	private Model model;
	private boolean waitUserInput = true;
	private TelegramBot bot;
	// Strategy Pattern -- connection View -> Controller
	private ActionController actionController;

	public View(Model model) {
		this.model = model;
		bot = new TelegramBot(ViewUtils.getTelegramToken());
	}

	public void receiveUsersMessages() {
		bot.setUpdatesListener(new UpdatesListener() {
			public int process(List<Update> updates) {
				try {
					execute(updates);
				} catch (Exception e) {
					Log.logWarnWriter("Erro ao processar mensagens: " + e);
					Log.logWarnWriter("Classe: " + this.getClass().getSimpleName());
				}
				return UpdatesListener.CONFIRMED_UPDATES_ALL;
			}
		});
	}

	// Action flow for each update
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
				Log.logInfoWriter("Classe: " + this.getClass().getSimpleName());
			} catch (Exception e) {
				Log.logErrorWriter("Algum erro ocorreu ao processar a mensagem!" + e);
				Log.logErrorWriter("Classe: " + this.getClass().getSimpleName());
			}

			if (message.equalsIgnoreCase("Relatório de requisições")) {
				setController(new HistoricController(model, this));
				bot.execute(new SendMessage(chatId, "Estou consultando valores armazenados, já respondo"));
				this.waitUserInput = false;

			}
			if (!this.waitUserInput) {
				this.callController(update);
			} else if (message.equalsIgnoreCase("Media")) {
				setController(new ExerciseControllerMean(model, this));
				bot.execute(new SendMessage(chatId,
						"Entendi, media! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.waitUserInput = false;

			} else if (message.equalsIgnoreCase("Mediana")) {
				setController(new ExerciseControllerMedian(model, this));
				bot.execute(new SendMessage(chatId,
						"Entendi, mediana! Então digita os valores de entrada separados por ponto e virgula ; para eu poder calcular que já respondo"));
				this.waitUserInput = false;
			} else if (message.equalsIgnoreCase("Moda")) {
				setController(new ExerciseControllerMode(model, this));
				bot.execute(new SendMessage(chatId,
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

	// Strategy Pattern
	public void setController(ActionController actionController) {
		this.actionController = actionController;
	}

	public void callController(Update update) {
		this.actionController.action(update);
	}

	// Action of 'Typing' on user telegram
	public void sendTypingMessage(Update update) {
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

	// Send messages to user
	@Override
	public void update(long chatId, String data, boolean isImage, boolean isDocument, File file) {
		if (isDocument) {
			bot.execute(new SendDocument(chatId, file).fileName("historico.pdf"));
			this.waitUserInput = true;
		} else {
			bot.execute(new SendMessage(chatId, data));
			this.waitUserInput = true;
			if (isImage) {
				bot.execute(new SendPhoto(chatId, file));
				this.waitUserInput = true;
			}
		}
	}

}
