package br.com.estatistica.estatistica.model;

import br.com.estatistica.estatistica.view.Observer;

public interface Subject {
	public void registerObserver(Observer observer);

	public void notifyObservers(long chatId, String data);
}
