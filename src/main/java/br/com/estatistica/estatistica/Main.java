package br.com.estatistica.estatistica;

import br.com.estatistica.estatistica.model.Model;
import br.com.estatistica.estatistica.view.View;

public class Main {

	private static Model model;

	public static void main(String[] args) {

		model = Model.getInstance();
		View view = new View(model);
		model.registerObserver(view); // connection Model -> View
		view.receiveUsersMessages();

	}
}
