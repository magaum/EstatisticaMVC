package br.com.estatistica.estatistica.controller;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.model.Model;
import br.com.estatistica.estatistica.view.View;

public class ExerciseControllerModa implements ActionController {

	private Model model;
	private View view;

	public ExerciseControllerModa(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public void action(Update update) {
		view.sendTypingMessage(update);
		model.calculaModa(update);
	}

}
