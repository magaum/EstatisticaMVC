package br.com.estatistica.estatistica.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class TesteModel {

	@Test
	@Ignore
	public void testGraphics() throws IOException {
		Model model = Model.getInstance();
		//assertNotNull(model.generateBoxPlot(null));
	}
	
	@Test
	public void testRemove() {
		List<Double> list = Arrays.asList(1.0,2.0);
		assertEquals("1.0, 2.0",list.toString().replaceAll("\\[|\\]", ""));
	}

}
