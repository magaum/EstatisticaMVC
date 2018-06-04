package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.pengrad.telegrambot.model.Update;

public class ModelDAO {

	private static ModelDAO modelDAO;
	private static ObjectContainer bancoProblemas;

	public static ModelDAO getInstance() {
		if (modelDAO == null) {
			modelDAO = new ModelDAO();
		}
		return modelDAO;
	}

	public static ObjectContainer connect() {
		if (bancoProblemas == null)
			bancoProblemas = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "db/bancoProblemas.db4o");
		return bancoProblemas;
	}

	//Add data
	public boolean addHistoric(Historic historic) {
		Log.logInfoWriter("Dados armazenados no banco: ");
		Log.logInfoWriter("Tipo: " + historic.getType());
		Log.logInfoWriter("ChatID: " + historic.getChatId());
		Log.logInfoWriter("Valores: " + historic.getValues());
		bancoProblemas = connect();
		bancoProblemas.store(historic);
		bancoProblemas.commit();
		return true;
	}

	//Delete data
	public static boolean deleteRequest(Historic historic) {
		bancoProblemas.delete(historic);
		return true;
	}

	//Get data
	public static List<Historic> getHistoric(Update update) {
		long chatId = update.message().chat().id();
		Date date = new Date();
		bancoProblemas = connect();
		Query query = bancoProblemas.query();
		query.constrain(Historic.class);
		ObjectSet<Historic> allHistoric = query.execute();
		List<Historic> userHistoric = new ArrayList<>();
		for (Historic historic : allHistoric) {
			if (historic.getChatId() == chatId) {
				long diffDate = TimeUnit.DAYS.convert(date.getTime() - historic.getDate().getTime(),
						TimeUnit.MILLISECONDS);
				if (diffDate > 2) {
					deleteRequest(historic);
				}
				userHistoric.add(historic);
			}
		}
		if (userHistoric.isEmpty()) {
			userHistoric = null;
		}
		return userHistoric;
	}
}
