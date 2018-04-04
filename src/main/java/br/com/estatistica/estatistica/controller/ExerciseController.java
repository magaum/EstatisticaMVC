package br.com.estatistica.estatistica.controller;

import org.telegram.telegrambots.api.objects.Update;

public interface ExerciseController {
	public void calcular(Update update);
}
