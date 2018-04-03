package br.com.estatistica.estatistica.controller;

import org.telegram.telegrambots.api.objects.Update;

import br.com.estatistica.estatistica.model.AppModel;
import br.com.estatistica.estatistica.view.AppView;

public class ExerciseControllerModa implements ExerciseController {

	private AppModel model;
	private AppView view;

	public ExerciseControllerModa(AppModel model, AppView view) {
		this.model = model;
		this.view = view;
	}

	public void calcular(Update update) {
		// model.calculaMedia(update);
		// view.sendTypingMessage(update);
	}
}
