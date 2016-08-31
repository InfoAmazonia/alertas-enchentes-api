package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.Station;

@Component
public class UpdatePortoVelho extends UpdatePredictionsTask {

	private static final long PORTOVELHO_ID = 15400000;
	private static final long ABUNA_ID = 15320002;
	private static final long MORADA_ID = 15326000;
	private static final long GUAJARA_ID = 15250000;
	
	private static final long RATE = 900000;
	private static final long DELTA = Duration.ofDays(1).getSeconds();
	private static final double A_1 = -0.115;
	private static final double A_2 = 0.994;
	private static final double A_3 = -0.014;
	private static final double A_4 = 0.023;
	
	public UpdatePortoVelho() {
		super(PORTOVELHO_ID, ABUNA_ID, MORADA_ID, GUAJARA_ID);
	}

	@Scheduled(initialDelay=1000, fixedRate = RATE)
	@Override
	@Transactional
	public void update() throws FileNotFoundException, ParseException {
		System.out.println("UpdatePortoVelho.update()");
		super.update();
		System.out.println("Updated PortoVelho");
	}

	@Override
	protected Alert predict(long timestamp) {
		
		Station portoVelho = stationRepository.findOne(PORTOVELHO_ID);
		Station abuna = stationRepository.findOne(ABUNA_ID);
		Station morada = stationRepository.findOne(MORADA_ID);
		Station guajara = stationRepository.findOne(GUAJARA_ID);
		
		Alert future = new Alert(portoVelho, timestamp + DELTA);
		
		Alert current = repository.findFirstByStationAndTimestamp(portoVelho, timestamp);
		Alert past = repository.findFirstByStationAndTimestamp(portoVelho, timestamp - DELTA);

		Alert currentAbuna = repository.findFirstByStationAndTimestamp(abuna, timestamp);
		Alert pastAbuna = repository.findFirstByStationAndTimestamp(abuna, timestamp - DELTA);

		Alert currentMorada = repository.findFirstByStationAndTimestamp(morada, timestamp - DELTA);
		Alert pastMorada = repository.findFirstByStationAndTimestamp(morada, timestamp - 2*DELTA);

		Alert currentGuajara = repository.findFirstByStationAndTimestamp(guajara, timestamp - 2*DELTA);
		Alert pastGuajara = repository.findFirstByStationAndTimestamp(guajara, timestamp - 4*DELTA);

		if (!isAnyAlertNull(current, past, currentAbuna, pastAbuna, currentMorada, pastMorada, currentGuajara, pastGuajara)) {

			long calculated  = (long) (current.measured + 
					A_1 * (current.measured - past.measured) + 
					A_2 * (currentAbuna.measured - pastAbuna.measured) + 
					A_3 * (currentMorada.measured - pastMorada.measured) + 
					A_4 * (currentGuajara.measured - pastGuajara.measured));
			long predicted = calculated + (current.calculated == 0?0:(current.measured - current.calculated));

			future.registerPrediction(calculated, predicted);
		}else{
			future.registerPrediction(0L, 0L);
		}
		
		return future;
	}
}
