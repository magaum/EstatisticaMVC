package br.com.estatistica.estatistica.controller;
import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.model.AppModel;
import br.com.estatistica.estatistica.view.AppView;


public class ExerciseControllerMedia implements ExerciseController{

	private AppModel model;
	private AppView view;
	
	public ExerciseControllerMedia(AppModel model, AppView view) {
		this.model = model;
		this.view = view;
	}
	
	public void calcular(Update update) {
		//model.calculaMedia(update);
		//view.sendTypingMessage(update);
	}	
}
