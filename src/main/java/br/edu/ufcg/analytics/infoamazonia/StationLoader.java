package br.edu.ufcg.analytics.infoamazonia;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;

@Component
public class StationLoader implements ApplicationListener<ApplicationReadyEvent>{
	
	@Autowired
	private StationRepository repository;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		Station xapuri = new Station("Xapuri", 13551000L, -1, -1, "01/12/201400:00:00", false);
		Station madeira = new Station("Rio Madeira", 13600002L, 1350, 1400, "01/12/201400:00:00", true);
		repository.save(Arrays.asList(
				xapuri,
				madeira
				));
		System.out.println("StationLoader.onApplicationEvent()");
	}
}
