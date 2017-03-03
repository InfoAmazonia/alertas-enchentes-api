package br.edu.ufcg.analytics.infoamazonia.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RiverStatusTest {

	private Integer thresholds[] = {1000, 1500, 2000};

	@Test
	public void testGetStatusForNullData() {
		assertEquals(RiverStatus.INDISPONIVEL, RiverStatus.get(null, thresholds));
	}

	@Test
	public void testGetStatusForNullThresholds() {
		assertEquals(RiverStatus.INDISPONIVEL, RiverStatus.get(0));
	}

	@Test
	public void testGetStatus() {
		assertEquals(RiverStatus.NORMAL, RiverStatus.get(0, thresholds));
		assertEquals(RiverStatus.NORMAL, RiverStatus.get(500, thresholds));
		assertEquals(RiverStatus.ATENCAO, RiverStatus.get(1000, thresholds));
		assertEquals(RiverStatus.ATENCAO, RiverStatus.get(1250, thresholds));
		assertEquals(RiverStatus.ALERTA, RiverStatus.get(1500, thresholds));
		assertEquals(RiverStatus.ALERTA, RiverStatus.get(1750, thresholds));
		assertEquals(RiverStatus.INUNDACAO, RiverStatus.get(2000, thresholds));
		assertEquals(RiverStatus.INUNDACAO, RiverStatus.get(2500, thresholds));
	}
}
