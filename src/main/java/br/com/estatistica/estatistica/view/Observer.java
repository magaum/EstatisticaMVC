package br.com.estatistica.estatistica.view;

import java.io.File;

public interface Observer {
	public void update(long chatId, String data);

	public void sendDocument(long chatId, File doc);

	public void sendImage(long chatId, File img);
}
