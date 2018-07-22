package br.com.estatistica.estatistica.model;

import org.junit.Test;

public class BoxPlotTest {

	@Test (expected = NullPointerException.class)
	public void fileNulltest() {
		BoxPlot.generateBoxPlot(null, "test");
	}

}
