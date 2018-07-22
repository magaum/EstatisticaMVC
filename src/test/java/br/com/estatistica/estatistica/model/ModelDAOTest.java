package br.com.estatistica.estatistica.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Ignore;
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
		Historic historic = new Historic();
		assertTrue(dao.addHistoric(historic));
		assertTrue(dao.deleteRequest(historic));
	}

	@Test
	public void getHistoricTest() {
		assertNull(ModelDAO.getHistoric(null));
	}
}
