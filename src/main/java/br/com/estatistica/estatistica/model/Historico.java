package br.com.estatistica.estatistica.model;

import java.util.ArrayList;

public class Historico {

	private ArrayList<Double> valores;
	long chatId;
	String username;

	public Historico(ArrayList<Double> valores, long chatId, String username) {
		this.valores = valores;
		this.chatId = chatId;
		this.username = username;
	}

	public ArrayList<Double> getvalores() {
		return valores;
	}

	public void setvalores(ArrayList<Double> valores) {
		this.valores = valores;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
