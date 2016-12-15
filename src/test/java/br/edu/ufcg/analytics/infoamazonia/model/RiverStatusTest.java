package br.edu.ufcg.analytics.infoamazonia.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RiverStatusTest {

	private Long thresholds[] = {1000L, 1500L, 2000L};

	@Test
	public void testGetStatusForNullData() {
		assertEquals(RiverStatus.INDISPONIVEL, RiverStatus.get(null, thresholds));
	}

	@Test
	public void testGetStatusForNullThresholds() {
		assertEquals(RiverStatus.INDISPONIVEL, RiverStatus.get(0L));
	}

	@Test
	public void testGetStatus() {
		assertEquals(RiverStatus.NORMAL, RiverStatus.get(0L, thresholds));
		assertEquals(RiverStatus.NORMAL, RiverStatus.get(500L, thresholds));
		assertEquals(RiverStatus.ATENCAO, RiverStatus.get(1000L, thresholds));
		assertEquals(RiverStatus.ATENCAO, RiverStatus.get(1250L, thresholds));
		assertEquals(RiverStatus.ALERTA, RiverStatus.get(1500L, thresholds));
		assertEquals(RiverStatus.ALERTA, RiverStatus.get(1750L, thresholds));
		assertEquals(RiverStatus.INUNDACAO, RiverStatus.get(2000L, thresholds));
		assertEquals(RiverStatus.INUNDACAO, RiverStatus.get(2500L, thresholds));
	}
}
