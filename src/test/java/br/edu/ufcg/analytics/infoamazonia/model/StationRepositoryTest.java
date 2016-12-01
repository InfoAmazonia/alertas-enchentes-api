package br.edu.ufcg.analytics.infoamazonia.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository repository;

	@Test
	public void testSaveS() {
		Station station = new Station();
		station.id = 0L;
		station.name = "station";
		repository.save(station);
		assertEquals(station, repository.findOne(0L));
		station.name = "name";
		repository.save(station);
		assertEquals(1, repository.count());
		assertEquals(station, repository.findOne(0L));
	}

}
