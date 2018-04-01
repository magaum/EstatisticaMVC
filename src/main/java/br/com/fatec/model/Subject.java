package br.com.fatec.model;

import br.com.fatec.view.Observer;

public interface Subject {
	public void registerObserver(Observer observer);
	public void notifyObservers(long chatId, String data);
}
