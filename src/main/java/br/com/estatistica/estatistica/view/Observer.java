package br.com.estatistica.estatistica.view;

public interface Observer {
	public void update(long chatId, String data);
	public void sendImage(String imgName, long chatId);
	public void sendDocument(long chatId);
}
