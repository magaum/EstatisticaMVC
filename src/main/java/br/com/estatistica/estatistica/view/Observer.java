package br.com.estatistica.estatistica.view;

import java.io.File;

public interface Observer {
	public void update(long chatId, String data);

	public void sendImage(File img, long chatId);

	public void sendDocument(File doc, long chatId);
}
