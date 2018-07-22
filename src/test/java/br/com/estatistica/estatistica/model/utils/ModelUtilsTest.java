package br.com.estatistica.estatistica.model.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

public class ModelUtilsTest {

	String message;

	@After
	public void setUp() {
		message = null;
	}

	@Test
	public void removeMapDataTest() {
		ArrayList<Double> actual = new ArrayList<Double>();
		actual.addAll(Arrays.asList(1.0, 2.0, 3.0, 3.0, 5.0));
		ModelUtils.removeMapData(actual);
		List expected = Arrays.asList(3.0, 5.0);
		assertEquals(expected, actual);
	}

	@Test
	public void nullMessage() {
		assertNull(ModelUtils.messageToDouble(null));
	}
	
	@Test
	public void messageErrorTest() {
		message = "teste de conversão com mensagem inválida";
		assertNull(ModelUtils.messageToDouble(message));
	}

	@Test
	public void messageToDoubleTest() {
		message = "1;2;3;5;4;6;8;7";
		ArrayList<Double> values = ModelUtils.messageToDouble(message);
		assertNotNull(values);
	}

}
