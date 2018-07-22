package br.com.estatistica.estatistica.model;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PdfTest {

	@Test
	public void test() {
		assertNull(Pdf.createPdf(null));
	}

}
