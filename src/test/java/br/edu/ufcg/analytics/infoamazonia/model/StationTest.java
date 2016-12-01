/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 *
 */
public class StationTest {

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.model.Station#calculateStatus(java.lang.Long)}.
	 */
	@Test
	public void testCalculateStatus() {
		Station station = new Station();
		station.warningThreshold = 500L;
		station.floodThreshold = 1000L;
		
		assertEquals(Station.STATUS_NORMAL, station.calculateStatus(499L));
		assertEquals(Station.STATUS_ALERTA, station.calculateStatus(500L));
		assertEquals(Station.STATUS_ALERTA, station.calculateStatus(501L));
		assertEquals(Station.STATUS_ALERTA, station.calculateStatus(999L));
		assertEquals(Station.STATUS_ENCHENTE, station.calculateStatus(1000L));
		assertEquals(Station.STATUS_ENCHENTE, station.calculateStatus(1001L));
	}

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.model.Station#calculateStatus(java.lang.Long)}.
	 */
	@Test
	public void testCalculateStatusWrongConfiguration() {
		Station station = new Station();
		station.warningThreshold = 500L;
		station.floodThreshold = 500L;
		
		assertEquals(Station.STATUS_NORMAL, station.calculateStatus(499L));
		assertEquals(Station.STATUS_ENCHENTE, station.calculateStatus(500L));
		assertEquals(Station.STATUS_ENCHENTE, station.calculateStatus(501L));
	}

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.model.Station#calculateStatus(java.lang.Long)}.
	 */
	@Test
	public void testCalculateStatusNull() {
		Station station = new Station();
		assertEquals(Station.STATUS_INDISPONIVEL, station.calculateStatus(null));
	}

}
