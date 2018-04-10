package br.com.estatistica.estatistica.view;

import java.util.List;

import javax.sound.midi.ControllerEventListener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import br.com.estatistica.estatistica.controller.ExerciseController;
import br.com.estatistica.estatistica.controller.ExerciseControllerModa;
import br.com.estatistica.estatistica.model.AppModel;

public class AppView implements Observer {

	private TelegramBot bot;
	private BaseResponse response;
	private SendResponse sendResponse;
	private AppModel appModel;
	private GetUpdatesResponse updateResponse;
	private int queueResponse;
	private Object calculate = null;

	public AppView(AppModel appModel, TelegramBot bot) {
		this.appModel = appModel;
		this.bot = bot;
	}

	ExerciseController exerciseController;
	
	public void setControllerSearch(ExerciseController exerciseController) {
		this.exerciseController = exerciseController;
	}
	
	

	public void receiveUsersMessages() {
		bot.setUpdatesListener(new UpdatesListener() {
			public int process(List<Update> updates) {
				updateResponse = bot.execute(new GetUpdates().limit(200).offset(queueResponse));
				for (Update update : updates) {
					if(calculate != null) {
						callController(update);
					}else if(update.message().text().equalsIgnoreCase("moda")) {
						setControllerSearch(new ExerciseControllerModa(appModel, ));
					}
				}
				return queueResponse;
			}
		});
	}
	
	
	public void update(long chatId, String data) {
		sendResponse = bot.execute(new SendMessage(chatId, "ok"));

	}
	
	public void callController(Update update) {
		this.exerciseController.calcular(update);
	}
	
	public void sendTypingMessage(Update update) {
		response = bot.execute(new SendMessage(update.message().chat().id(), ChatAction.typing.name()));
	}

}