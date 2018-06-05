package br.com.estatistica.estatistica.view;

import java.io.File;

public interface Observer {
	public void update(long chatId, String data, boolean isImage, boolean isDocument, File file);
}
