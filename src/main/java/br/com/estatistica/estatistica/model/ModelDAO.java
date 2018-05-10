package br.com.estatistica.estatistica.model;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.pengrad.telegrambot.model.Update;

public class ModelDAO {

	private static ModelDAO modelDAO;
	static ObjectContainer bancoProblemas;

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

	public boolean addHistoric(Historic historic) {
		bancoProblemas = connect();
		bancoProblemas.store(historic);
		bancoProblemas.commit();
		return true;
	}

	public static List<Historic> getHistoric(Update update) {
		long chatId = update.message().chat().id();
		bancoProblemas = connect();
		Query query = bancoProblemas.query();
		query.constrain(Historic.class);
		ObjectSet<Historic> allHistoric = query.execute();
		List<Historic> userHistoric = new ArrayList<>();
		for (Historic historic : allHistoric) {
			if (historic.getChatId() == chatId) {
				userHistoric.add(historic);
			}
		}
		if(userHistoric.isEmpty()) {
			userHistoric = null;
		}
		return userHistoric;
	}
}
