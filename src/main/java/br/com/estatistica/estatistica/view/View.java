package br.com.estatistica.estatistica.view;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
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

public class View implements Observer{
	

	@SuppressWarnings("deprecation")
	TelegramBot bot = TelegramBotAdapter.build("560936083:AAFbkcvYcWYgkzBrQcR6ufih8Ar_7VzPv2U" );

	//Object that receives messages
	GetUpdatesResponse updatesResponse;
	//Object that send responses
	SendResponse sendResponse;
	//Object that manage chat actions like "typing action"
	BaseResponse baseResponse;
			
	
	int queuesIndex			=0;
	boolean searchBehaviour = false;
	
	ExerciseController exerciseController; //Strategy Pattern -- connection View -> Controller
	private Model model;
	
	public View(Model model){
		this.model = model; 
	}
	
	public void setControllerSearch(ExerciseController exerciseController){ //Strategy Pattern
		this.exerciseController = exerciseController;
	}
	
	public void receiveUsersMessages() {
		
		//infinity loop
		while (true){
		
			//taking the Queue of Messages
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(queuesIndex));
			
			//Queue of messages
			List<Update> updates = updatesResponse.updates();

			//taking each message in the Queue
			for (Update update : updates) {
				executa(update);
			}
		}
	}
	

	public void executa(Update update) {
		//updating queue's index
		queuesIndex = update.updateId()+1;
		
		
		if(this.searchBehaviour==true){
			this.callController(update);
			
		}else if(update.message().text().equals("media")){
			setControllerSearch(new ExerciseControllerMedia(model, this));
			sendResponse 			= bot.execute(new SendMessage(update.message().chat().id(),"Digites os valores de entrada separados por ponto e virgula (;)!"));
			this.searchBehaviour 	= true;
			
		} else if(update.message().text().equals("mediana")){
			setControllerSearch(new ExerciseControllerMediana(model, this));
			sendResponse 			= bot.execute(new SendMessage(update.message().chat().id(),"Digites os valores de entrada separados por ponto e virgula (;)!"));
			this.searchBehaviour 	= true;
			
		} else if (update.message().text().equals("moda")) {
			setControllerSearch(new ExerciseControllerModa(model, this));
			sendResponse 			= bot.execute(new SendMessage(update.message().chat().id(),"Digites os valores de entrada separados por ponto e virgula (;)!"));
			this.searchBehaviour 	= true;
		
		} else {
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"O que deseja calcular:\nMedia \nModa  \nMediana ?"));
		}
	}
	
	
	public void callController(Update update){
		this.exerciseController.calcular(update);
	}
	
	public void update(long chatId, String studentsData){
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}
	
	public void sendTypingMessage(Update update){
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}
