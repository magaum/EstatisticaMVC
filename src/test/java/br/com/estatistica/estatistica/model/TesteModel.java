package br.com.estatistica.estatistica.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TesteModel {

	@Test
	public void testRemove() {
		List<Double> list = Arrays.asList(1.0,2.0);
		assertEquals("1.0, 2.0",list.toString().replaceAll("\\[|\\]", ""));
	}

}
