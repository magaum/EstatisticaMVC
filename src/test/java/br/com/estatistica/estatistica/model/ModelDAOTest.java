package br.com.estatistica.estatistica.model;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class ModelDAOTest {

	@Test
	public void deleteTest() throws ParseException {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setLenient(false);
		cal.roll(Calendar.DAY_OF_MONTH, -2);
		assertTrue(cal.before(Calendar.getInstance()));
	}

	@Test
	public void addDataTest() {
		ModelDAO dao = ModelDAO.getInstance();
		assertTrue(dao.addHistoric(new Historic()));
	}

	@Test
	public void getHistoricTest() {
		ModelDAO.getHistoric(null);
	}
}
