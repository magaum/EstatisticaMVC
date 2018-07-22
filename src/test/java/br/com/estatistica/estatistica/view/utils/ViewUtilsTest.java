package br.com.estatistica.estatistica.view.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ViewUtilsTest {

	@Test
	public void getTelegramTokenTest() {
		assertNotNull(ViewUtils.getTelegramToken());
	}

}
