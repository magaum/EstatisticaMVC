package br.com.estatistica.estatistica.model;


import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ModelDAOTest {

	@Test
	public void testDelete() throws ParseException {
		
		SimpleDateFormat test = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		Date date2 = new Date();
		date2.setTime(date.getTime());
		assertEquals(0,TimeUnit.DAYS.convert(date.getTime()-date2.getTime(), TimeUnit.MILLISECONDS));
	}
}
