package br.com.estatistica.estatistica.model;

import java.util.ArrayList;

public class Historic implements Comparable<Historic>{

	private ArrayList<Double> valores;
	long chatId;
	String tipo;

	public Historic(ArrayList<Double> valores, long chatId, String tipo) {
		this.valores = valores;
		this.chatId = chatId;
		this.tipo = tipo;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Historic historic) {
		return tipo.compareTo(historic.getTipo());
	}

}
